package com.gotp.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.database_messages.MessageDatabaseResponse;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestBot;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;
import com.gotp.server.messages.subscription_messages.MessageSubscribeAccept;
import com.gotp.server.messages.subscription_messages.MessageSubscribeRequest;

public class ServerThread implements Runnable {

    /** Thread will run while this variable is true. */
    private boolean running = true;

    /** Message handlers for different type of messages. */
    private Map<MessageType, Function<Message, Void>> messageHandlers = new HashMap<>();

    /** Socket to communicate with the client. */
    private BlockingQueue<Message> clientQueue;

    /** This thread's queue to read data from other threads. */
    private BlockingQueue<Message> threadQueue;

    /** Socket of the client whoose thread this is. */
    private Socket clientSocket;

    /**
     * Constructor.
     * @param clientSocket
     */
    public ServerThread(final Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientQueue = SharedResources.getInstance().getClientQueue(clientSocket);
        this.threadQueue = new LinkedBlockingQueue<>();

        messageHandlers.put(MessageType.DEBUG, this::handleDebugMessage);
        messageHandlers.put(MessageType.GAME_REQUEST_PVP, this::handleGameRequestPVP);
        messageHandlers.put(MessageType.GAME_REQUEST_BOT, this::handleGameRequestBot);
        messageHandlers.put(MessageType.DATABASE_REQUEST, this::handleDatabaseRequest);
        messageHandlers.put(MessageType.CLIENT_DISCONNECTED, this::handleClientDisconnect);
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        try {
            subscribeToClient();

            // Listen for messages.
            Message receivedMessage;
            MessageType messageType;
            while (running) {
                // for now just print the message.
                receivedMessage = threadQueue.take();
                messageType = receivedMessage.getType();

                // apply a specific handler for the message type.
                if (this.messageHandlers.containsKey(messageType)) {
                    this.messageHandlers
                        .get(messageType)
                        .apply(receivedMessage);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("ServerThread interrupted!");
            e.printStackTrace();
        }

        System.out.println("[ServerThread] Stopped.");
    }

    /**
     * Subscribe to the client.
     * @throws InterruptedException
     */
    public void subscribeToClient() throws InterruptedException {
        MessageSubscribeRequest subscribeRequest = new MessageSubscribeRequest(threadQueue);
        clientQueue.put(subscribeRequest);

        Message receivedMessage = threadQueue.take();

        if (!(receivedMessage instanceof MessageSubscribeAccept)) {
            throw new RuntimeException("Expected MessageSubscribeAccept, got " + receivedMessage.getClass().getName());
        }
    }

    /**
     * Handle a debug message.
     * @param message
     * @return Void
     */
    public Void handleDebugMessage(final Message message) {
        MessageDebug debugMessage = (MessageDebug) message;

        if (debugMessage.getTarget() == MessageTarget.SERVER_THREAD) {
            System.out.println("[ServerThread Debug] " + debugMessage.getDebugMessage());
        }

        return null;
    }

    /**
     * Handle a PVP game request.
     * @param message
     * @throws InterruptedException
     * @return Void
     */
    public Void handleGameRequestPVP(final Message message) {
        // Cast
        MessageGameRequestPVP gameRequestMessage = (MessageGameRequestPVP) message;

        // New game request object.
        GameRequest gameRequest = new GameRequest(this.clientSocket, gameRequestMessage.getBoardSize());

        // Get the wait list.
        Set<GameRequest> waitList = SharedResources.getInstance().getWaitList();

        // loop over every request in the wait list.
        // We need to break from the loop and than delete the requests from the wait list
        // because otherwise we will get a ConcurrentModificationException.
        GameRequest matchingRequest = null;
        for (GameRequest request : waitList) {
            // This request is already in the wait list.
            if (request.equals(gameRequest)) {
                return null;

            } else if (request.getBoardSize() == gameRequest.getBoardSize()) {
                matchingRequest = request;
                break;
            }
        }

        if (matchingRequest != null) {
            // Delete all of both client's requests from the wait list.
            SharedResources.getInstance().deleteClientFromWaitList(matchingRequest.getRequestingClient());
            SharedResources.getInstance().deleteClientFromWaitList(gameRequest.getRequestingClient());

            // Create a new game thread.
            GameThread gameThread = GameThread.buildPVPFromSockets(
                matchingRequest.getRequestingClient(),
                gameRequest.getRequestingClient(),
                matchingRequest.getBoardSize()
            );

            // run the game thread.
            Thread thread = new Thread(gameThread);
            thread.start();

            return null;
        }

        // If none of the requests in the wait list matched, add this request to the wait list.
        waitList.add(gameRequest);
        return null;
    }

    /**
     * Handle a PVP game request.
     * @param message
     * @throws InterruptedException
     * @return Void
     */
    public Void handleGameRequestBot(final Message message) {
        // Cast
        MessageGameRequestBot gameRequestMessage = (MessageGameRequestBot) message;

        // Remove the client from the wait list.
        SharedResources.getInstance().deleteClientFromWaitList(clientSocket);

        // Create a new game thread.
        GameThread gameThread = GameThread.buildBotGameFromSocket(clientSocket, gameRequestMessage.getBoardSize());

        // run the game thread.
        Thread thread = new Thread(gameThread);
        thread.start();
        return null;
    }

    /**
     * Handle a database request.
     * @param message
     * @return Void
     */
    public Void handleDatabaseRequest(final Message message) {
        GameHistory gameHistory = SharedResources.getInstance().getDatabase().getLastGameHistory();
        MessageDatabaseResponse response = new MessageDatabaseResponse(gameHistory);
        try {
            clientQueue.put(response);
        } catch (InterruptedException e) {
            System.out.println("[ServerThread::handleDatabaseResponse] Interrupted while sending database response.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handle a client disconnect.
     * @param message
     * @return Void
     */
    public Void handleClientDisconnect(final Message message) {
        try {
            // Notify the forwarder to disconnect.
            clientQueue.put(message);

            // Remove the client from the wait list.
            SharedResources.getInstance().deleteClientFromWaitList(this.clientSocket);

            // Stop the thread.
            this.running = false;

        } catch (InterruptedException e) {
            System.out.println("[ServerThread] Interrupted while notyfing Forwarder to disconnect.");
            e.printStackTrace();
        }
        return null;
    }
}
