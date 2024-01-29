package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.PieceType;
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

    private static int boardSize;

    private static PieceType myPieceType;

    private static int myAuthenticationKey;

    /** Private constructor. Disallow instantiation. */
    private ClientMock() { }

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        String serverAddress = "localhost";
        Scanner scanner = new Scanner(System.in);
        final int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            Communicator server = new Communicator(socket);

            String input;
            Message receivedMessage;

            while (true) {
                input = "";
                String[] intputTokens;

                scanner = new Scanner(System.in);
                System.out.print("Enter a message: ");
                input = scanner.nextLine();
                intputTokens = input.split(" ");
                if ("pvp".equals(input)) {
                    final MessageGameRequestPVP message = new MessageGameRequestPVP(5);
                    server.send(message);
                }

                if ("game".equals(input)) {
                    final MessageDebug message = new MessageDebug(
                        "From client to game thread!", MessageTarget.GAME_THREAD
                    );
                    server.send(message);
                }

                if ("read".equals(input)) {
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
                }

                if ("move".equals(intputTokens[0])) {
                    // server.send(new MessageDebug("Move from client!", MessageTarget.GAME_THREAD));
                    int x = Integer.parseInt(intputTokens[1]);
                    int y = Integer.parseInt(intputTokens[2]);
                    final MovePlace move = new MovePlace(new Vector(x, y), myPieceType);
                    final MessageMoveFromClient moveMessage = new MessageMoveFromClient(myAuthenticationKey, move);
                    server.send(moveMessage);
                }
            }

            // Close the connection
            // server.close();
            // scanner.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
