package com.gotp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gotp.server.messages.Message;

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
        Socket clientSocket;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("### Server Started! ###");

            while (true) {
                clientSocket = serverSocket.accept();
                createClientOnServer(clientSocket);

                serverThread = new ServerThread(clientSocket);
                new Thread(serverThread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a client on the server.
     * Put his quue in the shared resources
     * so that other threads can send them messages.
     * @param clientSocket
     * @throws IOException
     */
    public static void createClientOnServer(final Socket clientSocket) throws IOException {

        System.out.println("Client connected: " + clientSocket.getInetAddress());

        // This queue will be used to send data to the client.
        BlockingQueue<Message> forwarderEntry = new LinkedBlockingQueue<>();
        SharedResources.getInstance().addClient(clientSocket, forwarderEntry);

        // Forwarder is used to send data to the client and forward messages from client to many threads.
        Forwarder clientForwarder = new Forwarder(clientSocket, forwarderEntry);

        new Thread(clientForwarder).start();
    }
}
