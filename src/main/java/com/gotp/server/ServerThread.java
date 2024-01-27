package com.gotp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

// import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
// import com.gotp.server.messages.MessageGameRequestPVP;

public class ServerThread implements Runnable {

    /** Socket to communicate with the client. */
    private final Socket clientSocket;

    /**
     * Constructor.
     * @param clientSocket
     */
    public ServerThread(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        try {
            Communicator client = new Communicator(clientSocket);

            MessageDebug receivedMessage;
            MessageDebug responseMessage;

            // Receive an object from the client
            while (true) {
                receivedMessage = (MessageDebug) client.receive();
                System.out.println("Received from client: " + receivedMessage.getDebugMessage());

                if ("exit".equals(receivedMessage.getDebugMessage())) {
                    break;
                }

                responseMessage = new MessageDebug("*** " + receivedMessage.getDebugMessage() + " ***");
                client.send(responseMessage);
            }

            // Close the connection when done
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
