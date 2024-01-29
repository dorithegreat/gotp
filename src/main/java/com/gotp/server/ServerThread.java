package com.gotp.server;

import java.net.Socket;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;
import com.gotp.server.messages.subscription_messages.MessageSubscribeAccept;
import com.gotp.server.messages.subscription_messages.MessageSubscribeRequest;

public class ServerThread implements Runnable {

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
            while (true) {
                // for now just print the message.
                receivedMessage = threadQueue.take();
                if (
                    receivedMessage instanceof MessageDebug
                    && receivedMessage.getTarget() == MessageTarget.SERVER_THREAD
                ) {
                    System.out.println("[ServerThread Debug] " + ((MessageDebug) receivedMessage).getDebugMessage());
                }

                if (receivedMessage instanceof MessageGameRequestPVP) {
                    handleGameRequestPVP((MessageGameRequestPVP) receivedMessage);
                }
                // threadQueue.put(new MessageDebug("Server Response: Well cum!"));
            }
        } catch (InterruptedException e) {
            System.out.println("ServerThread interrupted!");
            e.printStackTrace();
        }
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
     * Handle a PVP game request.
     * @param gameRequestMessage
     * @throws InterruptedException
     */
    public void handleGameRequestPVP(final MessageGameRequestPVP gameRequestMessage) throws InterruptedException {
        System.out.println("ServerThread: Received a PVP game request.");

        // New game request object.
        GameRequest gameRequest = new GameRequest(this.clientSocket, gameRequestMessage.getBoardSize());

        // Get the wait list.
        Set<GameRequest> waitList = SharedResources.getInstance().getWaitList();

        // loop over every request in the wait list.
        for (GameRequest request : waitList) {
            // This request is already in the wait list.
            if (
                request.getBoardSize() == gameRequest.getBoardSize()
                && request.getRequestingClient() == gameRequest.getRequestingClient()
            ) {
                return;

            // Found a match!
            } else if (request.getBoardSize() == gameRequest.getBoardSize()) {
                System.out.println("Found a match!");

                // Remove these and every other request from these 2, from the wait list.
                for (GameRequest seachedRequest : waitList) {
                    if (seachedRequest.getRequestingClient() == request.getRequestingClient()) {
                        waitList.remove(seachedRequest);
                    }

                    if (seachedRequest.getRequestingClient() == gameRequest.getRequestingClient()) {
                        waitList.remove(seachedRequest);
                    }
                }

                // Create a pvp game.
                GameThread gameThread = GameThread.buildPVPFromSockets(
                    request.getRequestingClient(),
                    gameRequest.getRequestingClient(),
                    request.getBoardSize()
                );

                // run the game thread.
                Thread thread = new Thread(gameThread);
                thread.start();
                return;
            }
        }

        // If none of the requests in the wait list matched, add this request to the wait list.
        waitList.add(gameRequest);
        System.out.println("Added to wait list.");
    }
}
