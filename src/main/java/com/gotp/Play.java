package com.gotp;

import java.util.Scanner;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

/**
 * TODO: Delete this class in production.
 * You can play the text version of the game here.
 * Just run the main method.
 */
public final class Play {
    /**
     * Program starts executing here.
     * @param args
     */
    public static void main(final String[] args) {
        final int gameSize = 5;
        GameState gameState = new GameState(gameSize);

        Scanner scanner = new Scanner(System.in);

        String move;
        String response;
        int x;
        int y;

        boolean status = true;
        while (status) {
            System.out.println(gameState);
            System.out.println("Enter your move: ");

            // move = System.console().readLine();
            move = scanner.nextLine();

            if ("exit".equals(move)) {
                status = false;
                continue;
            }

            x = Integer.parseInt(move.strip().split(",")[0].strip());
            y = Integer.parseInt(move.strip().split(",")[1].strip());

            response = gameState.isLegalMove(gameState.getTurn(), new Vector(x, y)).getMessage();
            gameState.makeMove(new MovePlace(new Vector(x, y), gameState.getTurn()));

            System.out.println("\n\n\n" + response);
        }

        scanner.close();
    }

    private Play() { }
}
