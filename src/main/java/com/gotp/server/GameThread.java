package com.gotp.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.server.bot.BotThread;
import com.gotp.server.bot.FirstLegalMoveBot;
import com.gotp.server.bot.PassingBot;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.game_thread_messages.MessageGameOver;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromClient;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromServer;
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
            gameThreadQueue.take();
            player2Queue.put(subscribeToPlayer2);
            gameThreadQueue.take();
        } catch (InterruptedException e) {
            System.out.println("GameThread: Interrupted while subscribing to clients!");
            e.printStackTrace();
        }

        return new GameThread(gameThreadQueue, player1Queue, player2Queue, boardSize);
    }

    /**
     * Build a bot game from a client socket.
     * @param player
     * @param boardSize
     * @return GameThread
     */
    public static GameThread buildBotGameFromSocket(final Socket player, final int boardSize) {
        BlockingQueue<Message> playerQueue = SharedResources.getInstance().getClientQueue(player);
        BlockingQueue<Message> botQueue = new LinkedBlockingQueue<>();

        BlockingQueue<Message> gameThreadQueue = new LinkedBlockingQueue<>();

        MessageSubscribeRequest subscribeToPlayer = new MessageSubscribeRequest(gameThreadQueue);

        try {
            playerQueue.put(subscribeToPlayer);
            gameThreadQueue.take();
        } catch (InterruptedException e) {
            System.out.println("GameThread: Interrupted while subscribing to clients!");
            e.printStackTrace();
        }

        BotThread botThread = new BotThread(new FirstLegalMoveBot(), botQueue, gameThreadQueue);

        new Thread(botThread).start();

        return new GameThread(gameThreadQueue, playerQueue, botQueue, boardSize);
    }

    /** Thread will run while this variable is true. */
    private boolean running = true;

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
    // private int boardSize;

    private enum Player {
        /** Ther are 2 players in the game. */
        PLAYER1, PLAYER2
    }

    /** Which player has which color. */
    private Map<Player, Integer> authenticationKey = new HashMap<>();

    /** Which player has which color. */
    private Map<Player, PieceType> playerPieceType = new HashMap<>();

    /** Game state of this GameThread. */
    private GameState gameState;

    /** Handlers for messages. */
    private Map<MessageType, Function<Message, Void>> messageHandlers = new HashMap<>();


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
        this.gameState = new GameState(boardSize);

        messageHandlers.put(MessageType.DEBUG, this::handleDebugMessage);
        messageHandlers.put(MessageType.MOVE_FROM_CLIENT, this::handleMoveFromClient);
        messageHandlers.put(MessageType.CLIENT_DISCONNECTED, this::handleClientDisconnect);
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

        System.out.println(gameState);

        try {
            while (running) {
                Message receivedMessage = readQueue.take();
                MessageType messageType = receivedMessage.getType();

                // apply a specific handler for the message type.
                if (this.messageHandlers.containsKey(messageType)) {
                    this.messageHandlers
                        .get(messageType)
                        .apply(receivedMessage);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("[GameThread] Interrupted while reading from readQueue!");
            e.printStackTrace();
        }
    }

    // ------------------- helper functions -------------------
    private void finishTheGame() {
        
    }

    // ------------------- Message handlers -------------------
    /**
     * Handle a debug message.
     * @param message
     * @return Void
     */
    private Void handleDebugMessage(final Message message) {
        MessageDebug debugMessage = (MessageDebug) message;

        if (debugMessage.getTarget() == MessageTarget.GAME_THREAD) {
            System.out.println("[GameThread Debug] " + debugMessage.getDebugMessage());
        }

        return null;
    }

    /**
     * Handle move from client.
     * @param message
     * @return Void
     */
    private Void handleMoveFromClient(final Message message) {
        MessageMoveFromClient messageMove = (MessageMoveFromClient) message;

        System.out.println("[GameThread] Received move from client: " + messageMove.getMove());

        // check if the authentication key is valid.
        int authenticationKey = messageMove.getAuthenticationKey();
        if (!properAuthenticationKey(authenticationKey)) {
            System.out.println("[GameThread] Authentication key is not valid!");
            return null;
        }

        // check which player made the move.
        Player player = getPlayerFromAuthenticationKey(authenticationKey);
        PieceType pieceType = playerPieceType.get(player);

        // check if this player is allowed to make a move with this color.
        Move move = messageMove.getMove();
        if (move.getPieceType() != pieceType) {
            System.out.println("[GameThread] Player tried to place a piece of wrong color!");
            return null;
        }

        // Make a move and check if it's legal.
        MoveValidity moveValidity = gameState.makeMove(move);

        if (moveValidity.isLegal() && !gameState.gameOver()) {
            System.out.println(gameState);
            MessageMoveFromServer moveMessage = new MessageMoveFromServer(move);
            try {
                if (player == Player.PLAYER1) {
                    player2.put(moveMessage);
                } else {
                    player1.put(moveMessage);
                }
            } catch (InterruptedException e) {
                System.out.println("[GameThread::handleMoveFromClient] Interrupted while sending move to client!");
                e.printStackTrace();
            }

        } else if (!moveValidity.isLegal()) {
            System.out.println("[GameThread] Player tried to make an invalid move: " + moveValidity.getMessage());
        }

        // Check if the game is over.
        if (gameState.gameOver()) {
            // Get the final score.
            Map<PieceType, Double> overallScore = this.gameState.overallScore();

            // Get the histor to later save it to the database.
            GameHistory history = this.gameState.getHistory();

            // Set the final score.
            history.setFinalScore(overallScore);

            // Who's the winner of the game?
            if (overallScore.get(PieceType.BLACK) > overallScore.get(PieceType.WHITE)) {
                history.setWinner(PieceType.BLACK);
            } else {
                history.setWinner(PieceType.WHITE);
            }


            // Send the game over message to both players.
            MessageGameOver gameOverMessage = new MessageGameOver(history.getWinner(), history.getFinalScore());
            try {
                player1.put(gameOverMessage);
                player2.put(gameOverMessage);
            } catch (InterruptedException e) {
                System.out.println("[GameThread::handleMoveFromClient] Interrupted while sending game over message!");
                e.printStackTrace();
            }

            // Save the game to the database.
            // TODO: save the game to the database.
            // SharedResources.getInstance().getDatabase().insertGameHistory(history);


            // stop the thread.
            this.running = false;
        }

        return null;
    }

    /**
     * Handle client disconnect.
     * @return Void.
     */
    private Void handleClientDisconnect(final Message message) {
        // TODO: either win the game for the opposing player or end the game without conclusion.
        // TODO: send a message to the other player that the game has ended.
        this.running = false;
        return null;
    }


    // ------------------- Helper methods -------------------

    /**
     * Get player from authentication key.
     * @param authenticationKey
     * @return Player
     */
    private Player getPlayerFromAuthenticationKey(final int authenticationKey) {
        if (authenticationKey == this.authenticationKey.get(Player.PLAYER1)) {
            return Player.PLAYER1;
        } else if (authenticationKey == this.authenticationKey.get(Player.PLAYER2)) {
            return Player.PLAYER2;
        } else {
            throw new RuntimeException("[GameThread] Authentication key is not valid!");
        }
    }

    /**
     * Check if the authentication key is valid.
     * @param authenticationKey
     * @return boolean
     */
    private boolean properAuthenticationKey(final int authenticationKey) {
        return authenticationKey == this.authenticationKey.get(Player.PLAYER1)
            || authenticationKey == this.authenticationKey.get(Player.PLAYER2);
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

    /**
     * Notifies both players that the game has started.
     * Sends them the authentication key and the piece type.
     */
    private void sendGameStartedMessages() {
        MessageGameStarted player1Message = new MessageGameStarted(
            this.gameState.getBoardSize(),
            authenticationKey.get(Player.PLAYER1),
            playerPieceType.get(Player.PLAYER1)
        );

        MessageGameStarted player2Message = new MessageGameStarted(
            this.gameState.getBoardSize(),
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
