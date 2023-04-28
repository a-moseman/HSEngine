package org.amoseman;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import org.amoseman.HSEngine.HSEngine;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        HSEngine hsEngine = new HSEngine();
        Random random = new Random();

        Board board = new Board();
        while (!board.isMated() && !board.isDraw()) {
            String move;
            if (board.getSideToMove() == Side.WHITE) {
                move = hsEngine.go(board.getFen(), 10);
           }
            else {
                List<Move> legalMoves = board.legalMoves();
                move = legalMoves.get(random.nextInt(legalMoves.size())).toString();
            }
            board.doMove(move);
            System.out.println(board);
        }
    }
}
