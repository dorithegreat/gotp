package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;

/**
 * Client.
 */
public final class Client {

    /** Private constructor. Disallow instantiation. */
    private Client() { }

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

                scanner = new Scanner(System.in);
                System.out.print("Enter a message: ");
                input = scanner.nextLine();
                if ("pvp".equals(input)) {
                    final MessageGameRequestPVP message = new MessageGameRequestPVP(null, 19);
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
                    }
                }

                // Receive a message from the server
                // response = server.receive();
                // if (response instanceof MessageDebug) {
                //     System.out.println("[<-] " + ((MessageDebug) response).getDebugMessage());
                // }
            }

            // Close the connection
            // server.close();
            // scanner.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
