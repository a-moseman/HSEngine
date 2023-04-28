package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Collections;
import java.util.List;

public class SimulationPolicy {
    public static Move apply(Board board) {
        List<Move> legalMoves = board.legalMoves();
        Collections.shuffle(legalMoves);
        Move mateMove = findFirstMate(board, legalMoves);
        if (mateMove != null) {
            return mateMove;
        }
        Move captureMove = findFirstCapture(board, legalMoves);
        if (captureMove != null) {
            return captureMove;
        }
        return legalMoves.get(0);
    }

    private static Move findFirstMate(Board board, List<Move> legalMoves) {
        for (Move move : legalMoves) {
            board.doMove(move);
            boolean isMate = board.isMated();
            board.undoMove();
            if (isMate) {
                return move;
            }
        }
        return null;
    }

    private static Move findFirstCapture(Board board, List<Move> legalMoves) {
        for (Move move : legalMoves) {
            if (board.getPiece(move.getTo()) != Piece.NONE) {
                return move;
            }
        }
        return null;
    }
}
