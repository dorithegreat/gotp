package com.gotp.server.bot;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class CopilotBot implements Bot {

    @Override
    public Move getMove(final GameState gameState) {
        return new MovePlace(bestMove(gameState, gameState.getTurn()), gameState.getTurn());
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

    public Vector bestMove(final GameState gameState, final PieceType pieceType) {
        Set<Vector> legalMoves = legalMoves(gameState, pieceType);
        Vector bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (Vector move : legalMoves) {
            GameState copy = gameState.copy();
            copy.makeMove(new MovePlace(move, pieceType));
            double value = minimax(copy, pieceType, 10, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax function
     */
    public double minimax(final GameState gameState, final PieceType pieceType, final int depth, double alpha, double beta) {
        if (depth == 0) {
            return evaluate(gameState, pieceType);
        }

        if (gameState.getTurn() == pieceType) {
            double bestValue = Double.NEGATIVE_INFINITY;
            Set<Vector> legalMoves = legalMoves(gameState, pieceType);

            for (Vector move : legalMoves) {
                GameState copy = gameState.copy();
                copy.makeMove(new MovePlace(move, pieceType));
                double value = minimax(copy, pieceType, depth - 1, alpha, beta);
                bestValue = Math.max(bestValue, value);
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }

            return bestValue;
        } else {
            double bestValue = Double.POSITIVE_INFINITY;
            Set<Vector> legalMoves = legalMoves(gameState, pieceType);

            for (Vector move : legalMoves) {
                GameState copy = gameState.copy();
                copy.makeMove(new MovePlace(move, pieceType));
                double value = minimax(copy, pieceType, depth - 1, alpha, beta);
                bestValue = Math.min(bestValue, value);
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }

            return bestValue;
        }
    }
}
