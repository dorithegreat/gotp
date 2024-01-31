package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;

public class ReceiverThread implements Runnable{
    /**
     * queue to forward messages to.
     */
    private final BlockingQueue<Message> writQueue;

    /**
     * Constructor.
     * @param readQueue
     */
    public ReceiverThread(final BlockingQueue<Message> writeQueue) {
        this.writQueue = writeQueue;
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

            // Main loop.
            while (true) {
                Message receivedMessage = server.receive();
                writQueue.put(receivedMessage);
                System.out.println("received");
            }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("can't communicate with the server");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("can't read from queue");
            e.printStackTrace();
        }
    }

    private void readFromServer(Communicator serverCommunicator){

    }
}
