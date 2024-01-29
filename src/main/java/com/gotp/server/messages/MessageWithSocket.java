package com.gotp.server.messages;

import java.net.Socket;

/**
 * Implement this interface if you want
 * the Forwarder to add Client's Socket to the message.
 * ! When you send it from the client's side, set client to null. !
 */
public interface MessageWithSocket {
    /**
     * Get the socket of the client who send this message.
     * @return Socket
     */
    Socket getClientSocket();

    /**
     * Set the socket of the client who send this message.
     * @param clientSocket
     * @return Message
     */
    Message addClientSocket(Socket clientSocket);
}
