package com.gotp.server;

import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;

/**
 * Thread to run the game.
 */
public class GameThread implements Runnable {

    /**
     * Queue from which thread will read.
     * ! readQueue should already be subscribed to BOTH clients, before object instatiation. !
     */
    private BlockingQueue<Message> readQueue;

    /** player1 write queue. */
    private BlockingQueue<Message> player1;

    /** player2 write queue. */
    private BlockingQueue<Message> player2;

    /**
     * Constructor.
     * @param readQueue
     * @param player1
     * @param player2
     */
    public GameThread(
        final BlockingQueue<Message> readQueue,
        final BlockingQueue<Message> player1,
        final BlockingQueue<Message> player2
    ) {
        this.readQueue = readQueue;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        MessageDebug debug = new MessageDebug("GameThread started!", MessageTarget.FORWARDER);

        try {
            player1.put(debug);
            player2.put(debug);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
