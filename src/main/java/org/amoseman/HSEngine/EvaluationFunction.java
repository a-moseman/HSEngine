package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

public class EvaluationFunction {

    public static double apply(Board board) {
        double score =
                materialBalance(board) * 75
                + explosiveSquare(board) * 10
                + checkLove(board) * 15;
        return score / 100;
    }

    private static final int[] PIECE_VALUES = new int[]{100, 300, 300, 500, 900, 0};
    private static double materialBalance(Board board) {
        int white = 0;
        int black = 0;
        for (int i = 0; i < 64; i++) {
            Square square = Square.squareAt(i);
            Piece piece = board.getPiece(square);
            if (piece == Piece.NONE) {
                continue;
            }
            if (piece.getPieceSide() == Side.WHITE) {
                white += PIECE_VALUES[piece.getPieceType().ordinal()];
            }
            else {
                black += PIECE_VALUES[piece.getPieceType().ordinal()];
            }
        }
        return (double) (white - black) / 3900;
    }

    private static double explosiveSquare(Board board) {
        Piece piece = board.getPiece(Square.C4);
        if (piece == Piece.NONE) {
            return 0;
        }
        if (piece.getPieceSide() == Side.WHITE) {
            return -1;
        }
        else {
            return 1;
        }
    }

    private static double checkLove(Board board) {
        if (board.isKingAttacked()) {
            if (board.getSideToMove() == Side.BLACK) {
                return 1;
            }
            else {
                return -1;
            }
        }
        return 0;
    }
}
