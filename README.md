# HSEngine
A chess engine using the Monte Carlo Tree Search as its framework.

TODO:
- Enpassant Love - Evaluation Function Feature
- Forced Enpassant - General
- UCI Protocol - General

## Monte Carlo Tree Search
The engine uses a variation of the MCTS algorithm, specifically the UCT algorithm.
### Selection Stage
The tree policy used in the selection stage is the UCB formula. Specifically, r / v + c * sqrt(ln(V + 1) / v), where r is the accumulated reward of the node, v is the number of visits to the node, V is the number of visits to the node's parent, and c is the exploration parameter. The value of c used is sqrt(2). If the current node during selection is of the engine's side, then the child with the best UCB value is selected. Otherwise, the one with the worst UCB is used.
### Expansion Stage
The expanded node is always chosen as the first one not selected in the list of legal moves generated by chesslib.
### Simulation Stage
The simulation policy is random move selection. For the simulation stage, simulation is limited to a certain depth based on how many cycles of the MCTS have occured. The formula for depth is floor(ln(x + 1)) + 1, where x is the number of cycles. If the board state at the end of simulation is terminal, the reward is 1 for a win and -1 for a loss. Otherwise, the output of the evaluation function is used. The output is negated if the engine is playing for black, so that the reward is specific to the engine. Rewards from the evaluation function are also weighted by 0.1.
### Backpropagation
For the engine, backpropagation is just the basic implementation.
## Evaluation Function
Returns a value between -1 and 1. It is a linear combination of several handcrafted features.
### Material Balance
Piece values: 100 for pawns, 300 for knights and bishops, 500 for rooks, 900 for queens, and 0 for kings.

Returns: The difference of the white's material value and black's material value, divided by 3900.

Weight: 0.75
### The Explosive Square
Returns: 1 is black has any piece on the square C4, -1 if white does, and 0 otherwise. 

Weight: 0.1
### Check Love
Returns: 1 if black's king is in check, -1 if white's king is in check, and 0 otherwise.

Weight: 0.15
