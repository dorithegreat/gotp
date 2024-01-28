package com.gotp.server;

public class Listener implements Runnable {

    /** Reads from this queue.  */
    private final BlockingQueue<Message> in;

    /** Writes to this queue */
    private final BlockingQueue<Message> out;
}
