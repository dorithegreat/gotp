package com.gotp.server;

public class ClientSideCommunicator extends Communicator {
    @Override
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
}