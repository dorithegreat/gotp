package com.gotp.server.bot;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class BruteForceBot implements Bot {

    public static void main(String[] args) {
        GameState gameState = new GameState("BWBEE;EEEEE;EEEEE;EEEEE;EEEEE", PieceType.BLACK);
        System.out.println(gameState);
        BruteForceBot bot = new BruteForceBot();
        System.out.println(bot.bestMove(gameState, PieceType.BLACK));
    }

    @Override
    public Move getMove(final GameState gameState) {
        return null;
    }

    // TODO: finish implementing this.
    public double evaluate(GameState gameState, PieceType ourPieceType) {
        Map<PieceType, Double> score = gameState.overallScore();
        Double whiteScore = score.get(PieceType.WHITE);
        Double blackScore = score.get(PieceType.BLACK);

        if (ourPieceType == PieceType.WHITE) {
            return whiteScore - blackScore;
        } else {
            return blackScore - whiteScore;
        }
    }

    /**
     * Returns a set of all legal moves for a given piece type.
     * @param gameState the current game state
     * @param pieceType the piece type to get legal moves for
     * @return a set of all legal moves for a given piece type
     */
    public Set<Vector> legalMoves(final GameState gameState, final PieceType pieceType) {
        Board board = gameState.getBoardCopy();
        Set<Vector> allEmpty = board.emptyFields();
        Set<Vector> result = new HashSet<>();

        for (Vector emptyField : allEmpty) {
            if (gameState.isLegalMove(pieceType, emptyField).isLegal()) {
                result.add(emptyField);
            }
        }

        return result;
    }

    /**
     * Returns the best move for a given piece type.
     * @param gameState
     * @param pieceType
     * @return Move
     */
    public Move bestMove(final GameState gameState, final PieceType pieceType) {
        Set<Vector> legalMoves = legalMoves(gameState, pieceType);
        Move currentBestMove = new MovePass(pieceType);

        GameState newGameState;
        newGameState = gameState.copy();
        newGameState.makeMove(currentBestMove);
        double currentBestScore = evaluate(newGameState, pieceType);

        for (Vector field : legalMoves) {
            newGameState = gameState.copy();
            newGameState.makeMove(new MovePlace(field, pieceType));
            double evaluation = evaluate(newGameState, pieceType);
            if (evaluation > currentBestScore) {
                currentBestScore = evaluation;
                currentBestMove = new MovePlace(field, pieceType);
            }
        }

        return currentBestMove;
    }

    public Move searchForBestMove(GameState gameState, PieceType pieceType, int deph) {
        if (deph == 0) {
            return bestMove(gameState, pieceType);
        }

        Move currentBestMove = new MovePass(pieceType);

        GameState newGameState;
        newGameState = gameState.copy();
        newGameState.makeMove(currentBestMove);
        double currentBestScore = evaluate(newGameState, pieceType);

        Set<Vector> legalMoves = legalMoves(gameState, pieceType);
        for (Vector field : legalMoves) {
            // copy the game state
            newGameState = gameState.copy();
            
            // do our move

            // calculate best response
            Move bestResponse = searchForBestMove(newGameState, pieceType.opposite(), deph - 1);
            
            
            newGameState.makeMove(bestResponse);
            double evaluation = evaluate(newGameState, pieceType);
            if (evaluation > currentBestScore) {
                currentBestScore = evaluation;
                currentBestMove = new MovePlace(field, pieceType);
            }
        }

    }
}