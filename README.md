# Chess-AI
Chess Artificial Intelligence Console Application written in Java 8 utilizing Alpha-Beta pruning optimization


The game begins with a standard construction of the board and asks the user for the co-ordinate of the piece they wish to move and following, the square’s coordinates to move the piece too. After the user has made his move the opponent’s will carefully process possible moves and this includes retrieving values for each piece and constructing a table for each piece which contains the best next position, and these are all added with the depth to determine the next best move, the deeper the depth the longer the ai will take in making a move but it will take more possible instances into account. A very important part of our game-tree search is the concept of the Min-Max algorithm and using Alpha-Beta pruning to optimize it. Firstly, the min-max strategy is used in-order to determine the next move however, the strategy has a severe flaw. The number of game states is exponential to the number of moves and would thus take too long to computationally evaluate each node, this is where Alpha-Beta pruning was utilized, and it basically provided a solution which was to avoid expanding certain subtrees and therefore not examining every node. Alpha is the best choice value found on the MAX-path and Beta is the best choice found along the MIN path. Evidently, the essence of pruning is to save time by avoiding unproductive searching. It is important to note that in the best case, alpha-beta pruning can cut exponential growth by ½. The ideology used in Alpha-Beta pruning is simple – as mentioned above Alpha is the best move along the max path, if v is worse than alpha, max will avoid it and that branch would thus be pruned and no longer searched through. Upsides to alpha-beta pruning include that it doesn’t affect the final results and can be used to look twice as far in the same amount of time as the classical min-max strategy without pruning.




      This represents a piece on the board.
      Keeps track of which team the piece belongs to.
     
      If there is an empty square this class represents it
      as well by returning a false if the occupied method is called.
     
      The children classes extending Piece returns appropriate
      String according to WHAT the piece is.
     
           Capital letters - Black Team
               P - Pawn
               N - Knight
               B - Bishop
               R - Rook
               Q - queen
               K - King
     
     
     

Execution Instructions: Run project and follow instructions printed to console
