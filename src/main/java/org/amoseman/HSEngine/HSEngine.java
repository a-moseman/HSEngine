package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.Board;

public class HSEngine {
    private Board board;

    public HSEngine() {
        board = new Board();
    }

    public String go(String position, int movetime) {
        board.loadFromFen(position);
        MCTS mcts = new MCTS(position);
        long startTime = System.nanoTime();
        while (System.nanoTime() - startTime < 1_000_000_000L * movetime) {
            mcts.step();
        }
        mcts.print(); // DEBUG
        return mcts.bestMove();
    }
}
