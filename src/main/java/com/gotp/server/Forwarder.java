package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.MessageSubscribe;

public class Forwarder implements Runnable {

    /** Socket to communicate with the client. */
    private final Socket clientSocket;

    /** Input stream to receive messages. */
    private final BlockingQueue<Message> entry;

    /** Output streams (subscribers). */
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
     * Send client's messages to all subscribers.
     */
    @Override
    public void run() {
        try {
            System.out.println("### Forwarder Started! ###");
            Communicator client = new Communicator(clientSocket);
            Message receivedMessage;

            new Thread(() -> manageIncomingMessages(client, subscribers, entry)).start();

            // Receive an object from the client
            while (true) {
                receivedMessage = client.receive();

                for (BlockingQueue<Message> subscriber : subscribers) {
                    subscriber.put(receivedMessage);
                }
            }

            // Close the connection when done
            // client.close();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thread for adding new subscribers
     * and forwarding messages to client.
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

            MessageSubscribe subscribeRequest;

            MessageDebug debugMessage;

            while (true) {
                receivedMessage = entry.take();

                if (receivedMessage instanceof MessageSubscribe) {
                    System.out.println("Forwarder received subscribe request.");
                    subscribeRequest = (MessageSubscribe) receivedMessage;
                    subscribers.add(subscribeRequest.getOut());
                    subscribeRequest.getOut().put(new MessageDebug("Accepted!"));

                } else if (receivedMessage instanceof MessageDebug) {
                    debugMessage = (MessageDebug) receivedMessage;
                    System.out.println("Forwarder received: " + debugMessage.getDebugMessage());
                    clientCommunicator.send(receivedMessage);
                } else {
                    clientCommunicator.send(receivedMessage);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
