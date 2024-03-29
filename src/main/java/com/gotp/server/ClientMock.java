package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MoveGiveUp;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.database_messages.MessageDatabaseRequest;
import com.gotp.server.messages.database_messages.MessageDatabaseRequestSpecific;
import com.gotp.server.messages.database_messages.MessageDatabaseResponse;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.game_thread_messages.MessageGameOver;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromClient;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromServer;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestBot;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;

/**
 * Client.
 */
public final class ClientMock {

    /** Board size of the game to play. */
    private static GameState gameState;

    /** Piece type of the game to play. */
    private static PieceType myPieceType;

    /** This client's authentication key to send moves. */
    private static int myAuthenticationKey;

    /** Communication with the server. Send moves through this. */
    private static Communicator server;

    /** If false, client exits. */
    private static boolean status = true;

    /** Private constructor. Disallow instantiation. */
    private ClientMock() { }

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        initializeComands();
        initializeMessageHandlers();

        String serverAddress = "localhost";
        Scanner scanner = new Scanner(System.in);
        final int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            server = new ClientSideCommunicator(socket);

            String input;

            while (status) {
                input = "";
                String[] intputTokens;

                scanner = new Scanner(System.in);
                System.out.print("Enter a message: ");
                input = scanner.nextLine();
                intputTokens = input.split(" ");

                if (commands.containsKey(intputTokens[0])) {
                    commands.get(intputTokens[0]).apply(intputTokens);
                } else {
                    System.out.println("Unknown command!");
                }
            }

            // Close resources.
            server.close();
            scanner.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ClientMock::main] Can't connect to server!");
            e.printStackTrace();
        }
    }

    /** Handlers for different commands. */
    private static Map<String, Function<String[], Void>> commands = new HashMap<>();

    /** Handler for different messages.  */
    private static Map<MessageType, Function<Message, Void>> messageHandlers = new HashMap<>();

    /**
     * Initialize commands.
     * Commands are stored in a map, where the key is the command name
     * and the value is a function that takes an array of strings as
     * input and returns null.
     */
    private static void initializeComands() {
        commands.put("pvp", ClientMock::commandPVP);
        commands.put("bot", ClientMock::commandBot);
        commands.put("debug", ClientMock::commandDebug);
        commands.put("read", ClientMock::commandRead);
        commands.put("move", ClientMock::commandMove);
        commands.put("database", ClientMock::commandDatabase);
        commands.put("exit", ClientMock::commandExit);
    }

    /**
     * Initialize message handlers.
     * Message handlers are stored in a map, where the key is the
     * message type and the value is a function that takes a message
     * as input and returns null.
     */
    private static void initializeMessageHandlers() {
        messageHandlers.put(MessageType.GAME_STARTED, ClientMock::handleGameStarted);
        messageHandlers.put(MessageType.MOVE_FROM_SERVER, ClientMock::handleMoveFromServer);
        messageHandlers.put(MessageType.DATABASE_RESPONSE, ClientMock::handleDatabaseResponse);
        messageHandlers.put(MessageType.GAME_OVER, ClientMock::handleGameOver);
        messageHandlers.put(MessageType.SERVER_DISCONNECTED, ClientMock::handleServerDisconnected);
    }

    /**
     * Handle a game started message.
     * @param message the message
     * @return null
     */
    private static Void handleGameStarted(final Message message) {
        MessageGameStarted messageGameStarted = (MessageGameStarted) message;

        System.out.println("[<-]Board size: " + messageGameStarted.getBoardSize());
        System.out.println("[<-] Authentication Key: " + messageGameStarted.getAuthenticationKey());
        System.out.println("[<-] Color: " + messageGameStarted.getPlayerPieceType());

        gameState = new GameState(messageGameStarted.getBoardSize());
        myPieceType = messageGameStarted.getPlayerPieceType();
        myAuthenticationKey = messageGameStarted.getAuthenticationKey();

        System.out.println(gameState);
        return null;
    }

    /**
     * Handle a move from server message.
     * @param message
     * @return Void
     */
    public static Void handleMoveFromServer(final Message message) {
        MessageMoveFromServer messageMoveFromClient = (MessageMoveFromServer) message;

        System.out.println("[<-] Move from server: " + messageMoveFromClient.getMove());

        MoveValidity validity = gameState.makeMove(messageMoveFromClient.getMove());
        if (!validity.isLegal()) {
            System.out.println("[!] Move is not legal!");
            return null;
        }

        System.out.println(gameState);
        return null;
    }

    /**
     * Handle a gameHistory response message.
     * @param message
     * @return Void
     */
    public static Void handleDatabaseResponse(final Message message) {
        MessageDatabaseResponse databaseResponse = (MessageDatabaseResponse) message;
        GameHistory gameHistory = databaseResponse.getGameHistory();

        // System.out.println(gameHistory.getStartingPosition());
        // System.out.println(gameHistory.getStartingTurn());


        GameState gameState = new GameState(gameHistory.getStartingPosition(), gameHistory.getStartingTurn());

        System.out.println(gameState);
        for (Move move : gameHistory) {
            gameState.makeMove(move);
            System.out.print("\033[H\033[2J");
            System.out.println(gameState);
            try {
                final long oneSecond = 1000;
                Thread.sleep(oneSecond);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double whiteScore = gameState.overallScore().get(PieceType.WHITE);
        double blackScore = gameState.overallScore().get(PieceType.BLACK);
        System.out.println(gameState.overallScore());

        if (whiteScore > blackScore) {
            System.out.println("White wins!");
        } else {
            System.out.println("Black wins!");
        }


        return null;
    }

    /**
     * Handle game over.
     * @param message
     * @return Void
     */
    public static Void handleGameOver(final Message message) {
        MessageGameOver messageGameOver = (MessageGameOver) message;

        // Display score
        System.out.println("[<-] Final score: ");
        messageGameOver.getFinalScore().forEach((key, value) -> {
            System.out.println("[<-] " + key + ": " + value);
        });

        // Display winner
        if (messageGameOver.getWinner() == myPieceType) {
            System.out.println("[<-] You won!");
        } else {
            System.out.println("[<-] You lost!");
        }

        return null;
    }

    /**
     * Handle server disconnected.
     * @param message
     * @return Void
     */
    public static Void handleServerDisconnected(final Message message) {
        System.out.println("[<-] Server disconnected!");
        System.exit(0);
        return null;
    }


    private static Void commandPVP(final String[] inputTokens) {
        int localBoardSize = Integer.parseInt(inputTokens[1]);
        final MessageGameRequestPVP message = new MessageGameRequestPVP(localBoardSize);
        try {
            server.send(message);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandPVP] Can't send message!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handle a bot game request.
     * @param inputTokens
     * @return Void
     */
    private static Void commandBot(final String[] inputTokens) {
        int localBoardSize = Integer.parseInt(inputTokens[1]);
        final MessageGameRequestBot message = new MessageGameRequestBot(localBoardSize);
        try {
            server.send(message);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandBot] Can't send message!");
            e.printStackTrace();
        }
        return null;
    }

    private static Void commandDebug(final String[] inputTokens) {
        MessageTarget target = "server"
            .equals(inputTokens[1]) ? MessageTarget.SERVER_THREAD : MessageTarget.GAME_THREAD;

        final MessageDebug message = new MessageDebug("Debug from client", target);
        try {
            server.send(message);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandDebug] Can't send message!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handle a read command.
     * @param inputTokens
     * @return Void
     */
    private static Void commandRead(final String[] inputTokens) {
        Message receivedMessage;

        try {
            receivedMessage = server.receive();
            if (messageHandlers.containsKey(receivedMessage.getType())) {
                messageHandlers.get(receivedMessage.getType()).apply(receivedMessage);
            }

        } catch (ClassNotFoundException | IOException e) {
            System.out.println("[ClientMock::commandRead] Can't receive message!");
            e.printStackTrace();
        }
        return null;
    }

    private static Void commandMove(final String[] inputTokens) {
        final Move move;
        if ("pass".equals(inputTokens[1])) {
            move = new MovePass(myPieceType);
        } else if ("resign".equals(inputTokens[1])) {
            move = new MoveGiveUp(myPieceType);
        } else {
            int x = Integer.parseInt(inputTokens[1]);
            int y = Integer.parseInt(inputTokens[2]);
            move = new MovePlace(new Vector(x, y), myPieceType);
        }

        MoveValidity validity = gameState.makeMove(move);
        if (!validity.isLegal()) {
            System.out.println("[!] Move is not legal!");
            return null;
        }

        final MessageMoveFromClient moveMessage = new MessageMoveFromClient(myAuthenticationKey, move);
        try {
            server.send(moveMessage);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandMove] Can't send message!");
            e.printStackTrace();
        }

        System.out.println(gameState);
        return null;
    }

    /**
     * Handle a database command.
     * @param inputTokens
     * @return Void
     */
    private static Void commandDatabase(final String[] inputTokens) {
        Message message;
        if (inputTokens.length == 1) {
            message = new MessageDatabaseRequest();
        } else {
            int id = Integer.parseInt(inputTokens[1]);
            message = new MessageDatabaseRequestSpecific(id);
        }

        try {
            server.send(message);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandDatabase] Can't send message!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handle an exit command.
     * @param inputTokens
     * @return Void
     */
    private static Void commandExit(final String[] inputTokens) {
        status = false;
        return null;
    }
}
