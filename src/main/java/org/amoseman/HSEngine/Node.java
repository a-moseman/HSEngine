package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public final String POSITION;
    public final Node PARENT;
    public final Move MOVE;
    public final List<Node> CHILDREN;
    private int visits;
    private double reward;

    public Node(String position, Node parent, Move move) {
        POSITION = position;
        PARENT = parent;
        MOVE = move;
        CHILDREN = new ArrayList<>();
    }

    public void update(double reward) {
        this.reward += reward;
        this.visits++;
    }

    public int getVisits() {
        return visits;
    }

    public double getReward() {
        return reward;
    }
}
