package com.gotp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.gotp.server.messages.Message;

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
        if (objectInput == null) {
            return null;
        }
        return (Message) objectInput.readObject();
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
}
