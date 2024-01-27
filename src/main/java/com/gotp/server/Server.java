package com.gotp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.MessageSubscribe;

/**
 * Main server.
 */
public final class Server {

    /**
     * Private constructor to prevent instantiation.
     */
    private Server() { }

    /**
     * The main method.
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        final int portNumber = 12345;
        ServerThread serverThread;
        ServerThread serverThread2;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("### Server Started! ###");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // This queue will be used to send data to and from the forwarder
                BlockingQueue<Message> forwarderEntry = new LinkedBlockingQueue<>();

                // out queue for forwarder to send messages with.
                SharedResources.getInstance().addClient(clientSocket, forwarderEntry);
                Forwarder clientForwarder = new Forwarder(clientSocket, forwarderEntry);


                // Handle the client in a new thread
                // serverThread = new ServerThread(clientSocket);

                // new Thread(serverThread).start();
                new Thread(clientForwarder).start();

                forwarderEntry.put(new MessageDebug("Hello from server!"));

                new Thread(() -> threadPrintWhatYouGet(clientSocket)).start();
                // new Thread(() -> sendOneDebugMessage(clientSocket)).start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to a client.
     * @param destinationSocket
     * @throws IOException
     */
    public static void threadPrintWhatYouGet(final Socket destinationSocket) {
        try {
            SharedResources
                .getInstance()
                .getClientEntry(destinationSocket)
                .put(new MessageDebug("Hello another Thread!"));

            BlockingQueue<Message> myQueue = new LinkedBlockingQueue<>();

            MessageSubscribe subscribeRequest = new MessageSubscribe(myQueue);

            SharedResources
                .getInstance()
                .getClientEntry(destinationSocket)
                .put(subscribeRequest);

            Message response = myQueue.take();

            if (response instanceof MessageDebug) {
                System.out.println("Subscribe request was: " + ((MessageDebug) response).getDebugMessage());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
