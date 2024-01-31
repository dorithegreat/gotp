package com.gotp.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.other_messages.MessageServerDisconnected;

public class ClientSideCommunicator extends Communicator {
    /**
     * Constructor.
     * @param socket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ClientSideCommunicator(final Socket socket) throws IOException, ClassNotFoundException {
        super(socket);
    }

    /**
     * Receive a message from the server.
     * @return Message received.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Message receive() throws IOException, ClassNotFoundException {
        try {
            if (objectInput == null) {
                return null;
            }
            System.out.println("received");
            return (Message) objectInput.readObject();

        } catch (EOFException | SocketException e) {
            System.out.println("[Communicator] Server disconnected (java.io.EOFException)");
            return new MessageServerDisconnected();
        }
    }
}