package com.gotp.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;

import java.net.Socket;

/**
 * Shared resources for the server and its threads.
 * ! This class is a singleton. !
 */
public final class SharedResources {
    /** Class is a singleton that instantiates once. */
    private static SharedResources instance = new SharedResources();

    /** Players waiting to join a PVP game. */
    private Map<Socket, BlockingQueue<Message>> clients = Collections.synchronizedMap(new HashMap<>());

    /** Players waiting to join a PVP game. */
    private List<Integer> waitList = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor is private to prevent instantiation.
     * This class is a singleton.
     */
    private SharedResources() { }

    /**
     * Get the instance of the singleton.
     * @return SharedResources instance.
     */
    public static SharedResources getInstance() {
        return instance;
    }

    /**
     * Get the wait list.
     * @return Wait list.
     */
    public List<Integer> getWaitList() {
        return waitList;
    }

    /**
     * Get Entrypoint to send a message to a client
     * or subscribe to client's messages.
     * @param socket
     * @return ObjectOutputStream to write() to a client.
     */
    public BlockingQueue<Message> getClientEntry(final Socket socket) {
        return this.clients.get(socket);
    }

    /**
     * Add a client to the wait list.
     * @param socket
     * @param entry
     */
    public void addClient(final Socket socket, final BlockingQueue<Message> entry) {
        this.clients.put(socket, entry);
    }
}
