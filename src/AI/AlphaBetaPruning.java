package AI;

import Logic.Board;
import Logic.Move;
import Pieces.Piece;
import javafx.util.Pair;
import java.util.ArrayList;

/**
 * Represents the minimax algorithm which returns the best move
 * available depending on the depth of the search.
 */
public class AlphaBetaPruning {

    private boolean team;
    private Board board;
    private int depth;
    private int bestStartX, bestStartY;
    private int bestEndX, bestEndY;
    private Values values;
    private int alpha, beta;

    public AlphaBetaPruning(Board board, int depth) {
        team = false;
        this.depth = depth;
        this.board = board;
        this.values = new Values();
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
    }

    /**
     * Used to retrieve a move by the match class.
     */
    public Move getMove(){
        Move move = new Move();
        pruning();
        move.setStart(bestStartX, bestStartY, board.get(bestStartY, bestStartX));
        move.setEnd(bestEndX, bestEndY);
        return move;
    }

    /**
     * Pruning is done by comparing moves, followed by undoing them until
     * an end game state or maximum traversal depth is reached.
     * Infinite values are represented by either MAX_VALUE or MIN_VALUE which are
     * the largest or smallest possible int values in java.
     */
    private int pruning(){

        if(depth == 0 || board.isGameOver())
            return values.getValue(board, team, depth);

        depth--;
        if(board.getTurn() == team)return getMax();
        else return getMin();

    }

    private int getMax(){

        int bestStartX = -1;
        int bestStartY = -1;
        int bestEndX = -1;
        int bestEndY = -1;

        Piece piece;
        ArrayList<Pair<Integer,Integer>> possibleMoves;
        Move move = new Move();
        int score;
        int moves = board.getTotalMoves();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board.get(i, j).occupied() && board.get(i, j).getTeam()){

                    piece = board.get(i, j);
                    move.setStart(j,i, piece);
                    possibleMoves = getPossibleMoves(move);

                    for(int k=0; k<possibleMoves.size(); k++){

                        move.setEnd(possibleMoves.get(k).getKey(),possibleMoves.get(k).getValue());
                        board.update(move, true);
                        //System.out.println(board.toString());
                        board.nextTurn();
                        score = pruning();
                        if(moves != board.getTotalMoves())board.undoLastMove();

                        if(score > alpha){
                            alpha = score;
                            bestStartX = move.getStart().getKey();
                            bestStartY = move.getStart().getValue();
                            bestEndX = move.getEnd().getKey();
                            bestEndY = move.getEnd().getValue();
                        }

                        if(alpha >= beta)break;
                    }
                }
            }
        }

        if(bestStartX!=-1 && bestStartY!=-1 && bestEndX!=-1 && bestEndY!=-1){
            this.bestStartX = bestStartX;
            this.bestStartY = bestStartY;
            this.bestEndX = bestEndX;
            this.bestEndY = bestEndY;
        }

        return alpha;
    }

    private int getMin(){

        Piece piece;
        ArrayList<Pair<Integer,Integer>> possibleMoves;
        Move move = new Move();
        int score;
        int moves = board.getTotalMoves();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board.get(i, j).occupied() && !board.get(i, j).getTeam()){

                    piece = board.get(i, j);
                    move.setStart(j,i, piece);
                    possibleMoves = getPossibleMoves(move);

                    for(int k=0; k<possibleMoves.size(); k++){

                        move.setEnd(possibleMoves.get(k).getKey(),possibleMoves.get(k).getValue());
                        board.update(move, false);
                        //System.out.println(board.toString());
                        board.nextTurn();
                        score = pruning();

                        if(moves != board.getTotalMoves())board.undoLastMove();

                        beta = (score < beta) ? score : beta;

                        if(alpha >= beta)break;
                    }
                }
            }
        }

        return beta;
    }

    /**
     * Used to retrieve a list of all possible endpoints for currently selected
     * Piece. If the endpoints pass validation, they are added to the list.
     */
    private ArrayList<Pair<Integer,Integer>> getPossibleMoves(Move move){
        ArrayList<Pair<Integer,Integer>> moves = new ArrayList<>();

        for(int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                move.setEnd(j, i);
                if(move.isValid(board))moves.add(new Pair<>(j,i));
            }
        }

        return moves;
    }

}
