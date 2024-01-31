package com.gotp.server.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromClient;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromServer;

public class BotThread implements Runnable {
    /** Bot to get moves from. */
    private final Bot bot;

    /** Thread runs while this is true. */
    private boolean running = true;

    /** Queue to read messages from. */
    private BlockingQueue<Message> readQueue;

    /** Queue to write messages to. */
    private BlockingQueue<Message> writeQueue;

    /** State of the game. */
    private GameState gameState;

    /** Bot's piece color. */
    private PieceType myPieceType;

    /** Bot's authentication key. */
    private int myAuthenticationKey;

    private Map<MessageType, Function<Message, Void>> messageHandlers = new HashMap<>();


    public BotThread(Bot bot, BlockingQueue<Message> readQueue, BlockingQueue<Message> writeQueue) {
        this.bot = bot;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;

        this.messageHandlers.put(MessageType.GAME_STARTED, this::handleGameStart);
        this.messageHandlers.put(MessageType.MOVE_FROM_SERVER, this::handleMoveFromServer);
        this.messageHandlers.put(MessageType.GAME_OVER, this::handleGameOver);
    }

    @Override
    public void run() {
        try {
            Message receivedMessage;
            MessageType messageType;
            while (running) {
                // for now just print the message.
                receivedMessage = readQueue.take();
                messageType = receivedMessage.getType();

                // apply a specific handler for the message type.
                if (this.messageHandlers.containsKey(messageType)) {
                    this.messageHandlers
                        .get(messageType)
                        .apply(receivedMessage);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Void handleGameStart(final Message message) {
        MessageGameStarted messageGameStarted = (MessageGameStarted) message;

        // initialize the game state.
        gameState = new GameState(messageGameStarted.getBoardSize());
        myPieceType = messageGameStarted.getPlayerPieceType();
        myAuthenticationKey = messageGameStarted.getAuthenticationKey();

        // if the bot is black, it should make the first move.
        if (myPieceType == PieceType.BLACK) {
            // get the move from the bot.
            Move responseMove = this.bot.getMove(gameState);

            gameState.makeMove(responseMove);

            // send the move to the server.
            MessageMoveFromClient responseMessage = new MessageMoveFromClient(myAuthenticationKey, responseMove);

            try {
                writeQueue.put(responseMessage);
            } catch (InterruptedException e) {
                System.out.println(
                    "[BotThread::handleGameStart] Interrupted while trying to write to the writeQueue."
                );
                e.printStackTrace();
            }
        }

        return null;
    }

    private Void handleMoveFromServer(final Message message) {
        MessageMoveFromServer messageMoveFromClient = (MessageMoveFromServer) message;

        // update the game state with the move from the server.
        gameState.makeMove(messageMoveFromClient.getMove());

        // get the move from the bot.
        Move responseMove = this.bot.getMove(gameState);

        // update the game state with the move from the bot.
        gameState.makeMove(responseMove);

        // send the move to the server.
        MessageMoveFromClient responseMessage = new MessageMoveFromClient(myAuthenticationKey, responseMove);

        try {
            writeQueue.put(responseMessage);
        } catch (InterruptedException e) {
            System.out.println(
                "[BotThread::handleMoveFromServer] Interrupted while trying to write to the writeQueue."
            );
            e.printStackTrace();
        }

        return null;
    }

    private Void handleGameOver(final Message message) {
        this.running = false;
        return null;
    }
}
