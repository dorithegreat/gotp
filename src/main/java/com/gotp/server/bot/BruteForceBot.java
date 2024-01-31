// package com.gotp.server.bot;

// import java.util.Map;
// import java.util.Set;

// import com.gotp.game_mechanics.board.Board;
// import com.gotp.game_mechanics.board.GameState;
// import com.gotp.game_mechanics.board.PieceType;
// import com.gotp.game_mechanics.board.move.Move;
// import com.gotp.game_mechanics.utilities.Vector;

// public class BruteForceBot implements Bot {

//     public static void main(String[] args) {
//         BruteForceBot bot = new BruteForceBot();

//         System.out.println("value: " + value);
//     }

//     @Override
//     public Move getMove(final GameState gameState) {
//         return null;
//     }

//     // TODO: finish implementing this.
//     public double evaluate(GameState gameState, PieceType ourPieceType) {
//         Map<PieceType, Double> score = gameState.overallScore();
//         Double whiteScore = score.get(PieceType.WHITE);
//         Double blackScore = score.get(PieceType.BLACK);

//         if (ourPieceType == PieceType.WHITE) {
//             return whiteScore - blackScore;
//         } else {
//             return blackScore - whiteScore;
//         }
//     }

//     public Set<Vector> legalMoves(final GameState gameState, final PieceType pieceType) {
//         Board board = gameState.getBoardCopy();
//         Set<Vector> allEmpty = board.emptyFields();

//         for (Vector emptyField : allEmpty) {
//             gameState.isLegalMove(pieceType, emptyField);
//         }
//     }
// }
