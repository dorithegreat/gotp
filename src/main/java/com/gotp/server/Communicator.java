package com.gotp.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.other_messages.MessageClientDisconnected;

public class Communicator {
    /** Socket to communicate with server. */
    private final Socket socket;

    /** Input stream to receive messages. */
    private final ObjectInputStream objectInput;

    /** Output stream to send messages. */
    private final ObjectOutputStream objectOutput;

    /**
     * Constructor.
     * @param socket Socket to communicate with server.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Communicator(final Socket socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
        this.objectInput = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Send a message to the server.
     * @param message Message to send.
     * @throws IOException
     */
    public void send(final Message message) throws IOException {
        objectOutput.writeObject(message);
    }

    /**
     * Receive a message from the server.
     * @return Message received.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message receive() throws IOException, ClassNotFoundException {
        try {
            if (objectInput == null) {
                return null;
            }
            return (Message) objectInput.readObject();

        } catch (EOFException | SocketException e) {
            System.out.println("[Communicator] Client disconnected (java.io.EOFException)");
            return new MessageClientDisconnected();
        }
    }

    /**
     * Close the connection.
     * @throws IOException
     */
    public void close() throws IOException {
        socket.close();
        objectInput.close();
        objectOutput.close();
    }

    /**
     * Check if there are any messages available.
     * @return int
     */
    public int available() {
        try {
            return objectInput.available();
        } catch (IOException e) {
            System.out.println("[Communicator::available] IOException");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get input stream.
     * @return Input stream.
     */
    public ObjectInputStream getObjectInput() {
        return objectInput;
    }

    /**
     * Get output stream.
     * @return Output stream.
     */
    public ObjectOutputStream getObjectOutput() {
        return objectOutput;
    }

    /**
     * Get socket.
     * @return Socket.
     */
    public Socket getSocket() {
        return socket;
    }
}
