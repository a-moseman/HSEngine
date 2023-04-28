package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

public class EvaluationFunction {
    public static double apply(Board board) {
        return materialBalance(board);
    }

    private static final int[] PIECE_VALUES = new int[]{1, 3, 3, 5, 9, 0};
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
        return (double) (white - black) / 39;
    }
}
