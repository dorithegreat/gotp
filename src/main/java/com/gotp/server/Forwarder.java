package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.MessageWithSocket;
import com.gotp.server.messages.subscription_messages.MessageSubscribeAccept;
import com.gotp.server.messages.subscription_messages.MessageSubscribeRequest;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.other_messages.MessageClientDisconnected;


/**
 * This class is responsible for forwarding messages
 * to and from the client. It allows to be subcribed
 * to in order for multiple threads to be able to
 * receive messages from the client.
 */
public class Forwarder implements Runnable {

    /** Thread will run while this variable is true. */
    private boolean running = true;

    /** Socket to communicate with the client. */
    private final Socket clientSocket;

    /**
     * Queue to which every thread writes to, and which gets
     * either forwarded to client or if it's a subscribe request,
     * added to subscribers.
     */
    private final BlockingQueue<Message> entry;

    /** List of subscribed threads. */
    private List<BlockingQueue<Message>> subscribers = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor.
     * @param clientSocket
     * @param entry
     */
    public Forwarder(final Socket clientSocket, final BlockingQueue<Message> entry) {
        this.clientSocket = clientSocket;
        this.entry = entry;
    }

    /**
     * This thread is responsibble for forwarding messages
     * FROM the client to every subscribed thread.
     */
    @Override
    public void run() {
        try {
            // Create a new communicator for the client
            Communicator client = new Communicator(clientSocket);
            Message receivedMessage;

            // Run a reverse thread. More info in the method below.
            new Thread(() -> manageIncomingMessages(client, subscribers, entry)).start();

            // Listen for messages from the client.
            while (running) {
                receivedMessage = client.receive();

                // If the message needs clientSocket, we need to add it.
                if (receivedMessage instanceof MessageWithSocket) {
                    receivedMessage = ((MessageWithSocket) receivedMessage).addClientSocket(clientSocket);
                }

                if (receivedMessage instanceof MessageClientDisconnected) {
                    running = false;
                }

                // send the message to every subscriber.
                for (BlockingQueue<Message> subscriber : subscribers) {
                    subscriber.put(receivedMessage);
                }
            }

            // Close the connection when done
            client.close();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This thread forwards messages from THREADS
     * to the client. It also handles subscribe requests.
     * @param clientCommunicator
     * @param subscribers
     * @param entry
     */
    public static void manageIncomingMessages(
        final Communicator clientCommunicator,
        final List<BlockingQueue<Message>> subscribers,
        final BlockingQueue<Message> entry
    ) {
        try {
            Message receivedMessage;

            // Listen for messages from threads.
            boolean running = true;
            while (running) {
                receivedMessage = entry.take();

                // Handle subcribe requests.
                if (receivedMessage instanceof MessageSubscribeRequest) {
                    MessageSubscribeRequest subscribeRequest = (MessageSubscribeRequest) receivedMessage;
                    subscribers.add(subscribeRequest.getThreadQueue());
                    subscribeRequest.getThreadQueue().put(new MessageSubscribeAccept());

                // Handle debug messages.
                } else if (receivedMessage instanceof MessageDebug) {
                    MessageDebug debugMessage = (MessageDebug) receivedMessage;

                    // Determine for whom the debug message is.
                    if (debugMessage.getTarget() == MessageTarget.FORWARDER) {
                        System.out.println("Forwarder debug: " + debugMessage.getDebugMessage());

                    } else if (debugMessage.getTarget() == MessageTarget.CLIENT) {
                        clientCommunicator.send(receivedMessage);
                    }

                // Close this thread if the client disconnected.
                } else if (receivedMessage instanceof MessageClientDisconnected) {
                    MessageClientDisconnected disconnectedMessage = (MessageClientDisconnected) receivedMessage;
                    boolean ourClientDisconnected = disconnectedMessage
                        .getClientSocket()
                        .equals(clientCommunicator.getSocket());
                        running = !ourClientDisconnected;

                // Forward rest of the messages to the client.
                } else {
                    clientCommunicator.send(receivedMessage);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
