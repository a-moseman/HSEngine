package org.amoseman.HSEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MCTS {
    private Node root;
    private Side side;
    private int steps;

    public MCTS(String position) {
        this.root = new Node(position, null, null);
        Board board = new Board();
        board.loadFromFen(position);
        this.side = board.getSideToMove();
        this.steps = 0;
    }


    public void step() {
        Node selectedNode = selection();
        Node expandedNode = expansion(selectedNode);
        int depth = (int) Math.log(steps + 1) + 1;
        double reward = simulation(expandedNode, depth);
        backpropagation(expandedNode, reward);
        steps++;
    }

    //---Stages---\\

    private Node selection() {
        Node node = root;
        boolean asOpponent = false;
        while (isFullyExpanded(node)) {
            node = treePolicy(node, asOpponent);
            asOpponent = !asOpponent;
        }
        return node;
    }

    private Node expansion(Node node) {
        Board board = new Board();
        board.loadFromFen(node.POSITION);
        List<Move> legalMoves = board.legalMoves();
        if (legalMoves.size() == 0) {
            return node;
        }
        Move move = legalMoves.get(node.CHILDREN.size());
        board.doMove(move);
        Node child = new Node(board.getFen(), node, move);
        node.CHILDREN.add(child);
        return child;
    }

    private double simulation(Node node, int depth) {
        Board board = new Board();
        board.loadFromFen(node.POSITION);
        while (depth > 0 && !isTerminal(board)) {
            Move move = SimulationPolicy.apply(board);
            board.doMove(move);
            depth--;
        }
        return evaluate(board);
    }

    private void backpropagation(Node node, double reward) {
        node.update(reward);
        if (node.PARENT == null) {
            return;
        }
        backpropagation(node.PARENT, reward);
    }

    //---Policies---\\

    private Node treePolicy(Node node, boolean asOpponent) {
        int best = 0;
        for (int i = 0; i < node.CHILDREN.size(); i++) {
            if (!asOpponent) {
                if (ucb(node.CHILDREN.get(i)) > ucb(node.CHILDREN.get(best))) {
                    best = i;
                }
            }
            else {
                if (ucb(node.CHILDREN.get(i)) < ucb(node.CHILDREN.get(best))) {
                    best = i;
                }
            }
        }
        return node.CHILDREN.get(best);
    }

    private double ucb(Node node) {
        double c = Math.sqrt(2);
        double exploitationExpression = node.getReward() / node.getVisits();
        double explorationExpression = Math.sqrt(Math.log(node.PARENT.getVisits() + 1) / node.getVisits());
        return exploitationExpression + c * explorationExpression;
    }

    private double evaluate(Board board) {
        if (board.isMated()) {
            if (board.getSideToMove() == side) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else if (board.isDraw()) {
            return 0;
        }
        else {
            double evalWeight = 0.1;
            double reward = EvaluationFunction.apply(board);
            reward = reward * (side == Side.WHITE ? 1 : -1);
            return reward * evalWeight;
        }
    }

    //---Helper Methods---\\

    private boolean isFullyExpanded(Node node) {
        Board board = new Board();
        board.loadFromFen(node.POSITION);
        List<Move> legalMoves = board.legalMoves();
        if (legalMoves.size() == 0 || node.CHILDREN.size() < legalMoves.size()) {
            return false;
        }
        return true;
    }

    private boolean isTerminal(Board board) {
        return board.isMated() || board.isDraw();
    }

    public String bestMove() {
        int best = 0;
        for (int i = 0; i < root.CHILDREN.size(); i++) {
            if (root.CHILDREN.get(i).getReward() > root.CHILDREN.get(best).getReward()) {
                best = i;
            }
        }
        return root.CHILDREN.get(best).MOVE.toString();
    }

    public void print() {
        String string = "";
        for (int i = 0; i < root.CHILDREN.size(); i++) {
            Node child = root.CHILDREN.get(i);
            string += '\n' + child.MOVE.toString() + " - " + child.getReward() + ", " + child.getVisits();
        }
        System.out.println(string);
    }

}
