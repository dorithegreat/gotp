package com.gotp.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.gotp.database.Database;
import com.gotp.database.DatabaseMock;
import com.gotp.database.SQLiteDatabase;
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
    private Set<GameRequest> waitList = Collections.synchronizedSet(new HashSet<>());

    /** Database implementation. */
    private Database database = new DatabaseMock();

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
    public Set<GameRequest> getWaitList() {
        return waitList;
    }

    /**
     * Delete all client's requests from waitList.
     * @param socket
     * @return boolean
     */
    public boolean deleteClientFromWaitList(final Socket socket) {
        return waitList.removeIf(request -> request.getRequestingClient().equals(socket));
    }

    /**
     * Get Entrypoint to send a message to a client
     * or subscribe to client's messages.
     * @param socket
     * @return ObjectOutputStream to write() to a client.
     */
    public BlockingQueue<Message> getClientQueue(final Socket socket) {
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

    /**
     * Get the database implementation.
     * @return Database implementation.
     */
    public Database getDatabase() {
        return database;
    }
}
