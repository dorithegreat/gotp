package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromClient;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;

/**
 * Client.
 */
public final class ClientMock {

    /** Board size of the game to play. */
    private static int boardSize;

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

        String serverAddress = "localhost";
        Scanner scanner = new Scanner(System.in);
        final int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            server = new Communicator(socket);

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

    /** Depending on what state ClientMock is, it will handle messages differently. */
    private static Map<String, Function<String[], Void>> commands = new HashMap<>();

    private static void initializeComands() {
        commands.put("pvp", ClientMock::commandPVP);
        commands.put("debug", ClientMock::commandDebug);
        commands.put("read", ClientMock::commandRead);
        commands.put("move", ClientMock::commandMove);
        commands.put("exit", ClientMock::commandExit);
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

    private static Void commandRead(final String[] inputTokens) {
        Message receivedMessage;
        try {
            receivedMessage = server.receive();
            if (receivedMessage instanceof MessageGameStarted) {
                MessageGameStarted messageGameStarted = (MessageGameStarted) receivedMessage;
                System.out.println("[<-] Board size: " + messageGameStarted.getBoardSize());
                System.out.println("[<-] Authentication Key: " + messageGameStarted.getAuthenticationKey());
                System.out.println("[<-] Color: " + messageGameStarted.getPlayerPieceType());

                boardSize = messageGameStarted.getBoardSize();
                myPieceType = messageGameStarted.getPlayerPieceType();
                myAuthenticationKey = messageGameStarted.getAuthenticationKey();
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
        } else {
            int x = Integer.parseInt(inputTokens[1]);
            int y = Integer.parseInt(inputTokens[2]);
            move = new MovePlace(new Vector(x, y), myPieceType);
        }
        final MessageMoveFromClient moveMessage = new MessageMoveFromClient(myAuthenticationKey, move);
        try {
            server.send(moveMessage);
        } catch (IOException e) {
            System.out.println("[ClientMock::commandMove] Can't send message!");
            e.printStackTrace();
        }
        return null;
    }

    private static Void commandExit(final String[] inputTokens) {
        status = false;
        return null;
    }
}
