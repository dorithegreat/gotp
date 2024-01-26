package com.gotp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        // List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("### Server Started! ###");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle the client in a new thread
                serverThread = new ServerThread(clientSocket);

                new Thread(serverThread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
