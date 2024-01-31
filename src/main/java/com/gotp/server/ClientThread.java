package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;

public class ClientThread implements Runnable {
    /** Queue to read from. */
    private final BlockingQueue<Message> readQueue;
    private final BlockingQueue<Message> writeQueue;

    /**
     * Constructor.
     * @param readQueue
     */
    public ClientThread(final BlockingQueue<Message> readQueue, final BlockingQueue<Message> writeQueue) {
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
    }

    /**
     * This thread is responsible for keeping Socket open
     * and sending messages from GUI to the server.
     */
    @Override
    public void run() {
        String serverAddress = "localhost";
        final int serverPort = 12345;

        // Open the socket with the server.
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");

            // Create a Stream to communicate with the server.
            Communicator server = new Communicator(socket);

            //lambda expression that makes a new thread executing the private method
            //wrapped in try-catch because java is sometimes stupid (it doesn't work otherwise)
            new Thread(() -> {
                try {
                    readFromServer(server);
                } catch (Exception e) {
                    System.out.println("could not read from server");
                }
            }).start();

            // Main loop.
            while (true) {
                Message receivedMessage = readQueue.take();
                server.send(receivedMessage);
                //System.out.println("sent");
            }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("can't communicate with the server");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("can't read from queue");
            e.printStackTrace();
        }
    }

    private void readFromServer(Communicator communicator) throws IOException, ClassNotFoundException,InterruptedException{
        while (true) {
            Message received = communicator.receive();
            writeQueue.put(received);
        }
    }

}
