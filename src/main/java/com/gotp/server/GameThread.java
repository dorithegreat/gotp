package com.gotp.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.subscription_messages.MessageSubscribeRequest;

/**
 * Thread to run the game.
 */
public class GameThread implements Runnable {

    /**
     * Build a PVP gameThred from 2 client sockets.
     * @param player1
     * @param player2
     * @param boardSize
     * @return GameThread
     */
    public static GameThread buildPVPFromSockets(final Socket player1, final Socket player2, final int boardSize) {
        BlockingQueue<Message> player1Queue = SharedResources.getInstance().getClientQueue(player1);
        BlockingQueue<Message> player2Queue = SharedResources.getInstance().getClientQueue(player2);

        BlockingQueue<Message> gameThreadQueue = new LinkedBlockingQueue<>();

        MessageSubscribeRequest subscribeToPlayer1 = new MessageSubscribeRequest(gameThreadQueue);
        MessageSubscribeRequest subscribeToPlayer2 = new MessageSubscribeRequest(gameThreadQueue);

        try {
            player1Queue.put(subscribeToPlayer1);
            player1Queue.take();
            player2Queue.put(subscribeToPlayer2);
            player2Queue.take();
        } catch (InterruptedException e) {
            System.out.println("GameThread: Interrupted while subscribing to clients!");
            e.printStackTrace();
        }

        return new GameThread(gameThreadQueue, player1Queue, player2Queue, boardSize);
    }

    /**
     * Queue from which thread will read.
     * ! readQueue should already be subscribed to BOTH clients, before object instatiation. !
     */
    private BlockingQueue<Message> readQueue;

    /** player1 write queue. */
    private BlockingQueue<Message> player1;

    /** player2 write queue. */
    private BlockingQueue<Message> player2;

    /** Board size. */
    private int boardSize;

    private enum Player {
        /** Ther are 2 players in the game. */
        PLAYER1, PLAYER2
    }

    /** Which player has which color. */
    private Map<Player, Integer> authenticationKey = new HashMap<>();

    /** Which player has which color. */
    private Map<Player, PieceType> playerPieceType = new HashMap<>();

    /**
     * Constructor.
     * @param readQueue
     * @param player1
     * @param player2
     * @param boardSize
     */
    public GameThread(
        final BlockingQueue<Message> readQueue,
        final BlockingQueue<Message> player1,
        final BlockingQueue<Message> player2,
        final int boardSize
    ) {
        this.readQueue = readQueue;
        this.player1 = player1;
        this.player2 = player2;
        this.boardSize = boardSize;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        // MessageDebug debug = new MessageDebug("GameThread started!", MessageTarget.FORWARDER);
        initializeKeys();
        initializePieceTypes();
        sendGameStartedMessages();

        GameState gameState = new GameState(boardSize);

        while (true) {
            try {
                Message message = readQueue.take();
                if (message instanceof MessageDebug) {
                    MessageDebug messageDebug = (MessageDebug) message;
                    System.out.println("[GameThread] " + messageDebug.getDebugMessage());
                }
            } catch (InterruptedException e) {
                System.out.println("GameThread: Interrupted while reading from readQueue!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Generate authentication keys for the players.
     * and send them to the clients.
     */
    private void initializeKeys() {
        Random random = new Random();

        this.authenticationKey.put(Player.PLAYER1, random.nextInt());
        this.authenticationKey.put(Player.PLAYER2, random.nextInt());
    }

    /**
     * Generate piece types for the players.
     * and send them to the clients.
     */
    private void initializePieceTypes() {
        Random random = new Random();

        if (random.nextBoolean()) {
            this.playerPieceType.put(Player.PLAYER1, PieceType.WHITE);
            this.playerPieceType.put(Player.PLAYER2, PieceType.BLACK);
        } else {
            this.playerPieceType.put(Player.PLAYER1, PieceType.BLACK);
            this.playerPieceType.put(Player.PLAYER2, PieceType.WHITE);
        }
    }

    private void sendGameStartedMessages() {
        MessageGameStarted player1Message = new MessageGameStarted(
            boardSize,
            authenticationKey.get(Player.PLAYER1),
            playerPieceType.get(Player.PLAYER1)
        );

        MessageGameStarted player2Message = new MessageGameStarted(
            boardSize,
            authenticationKey.get(Player.PLAYER2),
            playerPieceType.get(Player.PLAYER2)
        );

        try {
            player1.put(player1Message);
            player2.put(player2Message);
        } catch (InterruptedException e) {
            System.out.println("GameThread: Interrupted while sending game started messages!");
            e.printStackTrace();
        }
    }
}
