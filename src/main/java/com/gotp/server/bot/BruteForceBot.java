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

public class BruteForceBot implements Bot {

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
        for ()
    }
}