package AI;

import Logic.Board;
import Pieces.*;

/**
 * This class represents the heuristic value of each piece, i.e. it's worth
 * It also contains arrays of each positions value for each piece,
 * to determine a desirable position for the minimax search tree.
 */
public class Values {

    private final int PAWN = 100;
    private final int KNIGHT = 320;
    private final int BISHOP = 330;
    private final int ROOK = 500;
    private final int QUEEN = 900;
    private final int KING = 20000;

    private final int[][] PawnValues = new int[][]{
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
            {  5,  5, 10, 25, 25, 10,  5,  5 },
            {  0,  0,  0, 20, 20,  0,  0,  0 },
            {  5, -5,-10,  0,  0,-10, -5,  5 },
            {  5, 10, 10,-20,-20, 10, 10,  5 },
            {  0,  0,  0,  0,  0,  0,  0,  0 }
    };

    private static int[][] KnightValues = new int[][] {
            { -50,-40,-30,-30,-30,-30,-40,-50 },
            { -40,-20,  0,  0,  0,  0,-20,-40 },
            { -30,  0, 10, 15, 15, 10,  0,-30 },
            { -30,  5, 15, 20, 20, 15,  5,-30 },
            { -30,  0, 15, 20, 20, 15,  0,-30 },
            { -30,  5, 10, 15, 15, 10,  5,-30 },
            { -40,-20,  0,  5,  5,  0,-20,-40 },
            { -50,-40,-30,-30,-30,-30,-40,-50 }
    };

    private static int[][] BishopValues = new int[][] {
            { -20,-10,-10,-10,-10,-10,-10,-20 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -10,  0,  5, 10, 10,  5,  0,-10 },
            { -10,  5,  5, 10, 10,  5,  5,-10 },
            { -10,  0, 10, 10, 10, 10,  0,-10 },
            { -10, 10, 10, 10, 10, 10, 10,-10 },
            { -10,  5,  0,  0,  0,  0,  5,-10 },
            { -20,-10,-10,-10,-10,-10,-10,-20 }
    };

    private static int[][] RookValues = new int[][] {
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  5, 10, 10, 10, 10, 10, 10,  5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            {  0,  0,  0,  5,  5,  0,  0,  0 }
    };

    private static int[][] QueenValues = new int[][] {
            { -20,-10,-10, -5, -5,-10,-10,-20 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -10,  0,  5,  5,  5,  5,  0,-10 },
            {  -5,  0,  5,  5,  5,  5,  0, -5 },
            {   0,  0,  5,  5,  5,  5,  0, -5 },
            { -10,  5,  5,  5,  5,  5,  0,-10 },
            { -10,  0,  5,  0,  0,  0,  0,-10 },
            { -20,-10,-10, -5, -5,-10,-10,-20 }
    };

    private static int[][] KingValues = new int[][] {
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -20,-30,-30,-40,-40,-30,-30,-20 },
            { -10,-20,-20,-20,-20,-20,-20,-10 },
            {  20, 20,  0,  0,  0,  0, 20, 20 },
            {  20, 30, 10,  0,  0, 10, 30, 20 }
    };

    /**
     * Used to retrieve the relative score according to each piece
     * value and their best position. As depth increases the
     * score is reduced to prevent pruning from considering
     * equally valued moves as the same, thus getting a result
     * quicker.
     */

    public int getValue(Board board, boolean team, int depth){

        int score;

        if(board.isGameOver() && board.getWinner()==team){
            score = Integer.MAX_VALUE;
        } else if(board.isGameOver() && board.getWinner()!=team){
            score = Integer.MIN_VALUE;
        } else if(board.isGameOver() && board.isDraw()){
            score = 0;
        } else {
            score = 0;
            for(int i=0; i<8; i++){
                for(int j=0; j<8; j++){
                    if(board.get(j,i).occupied() && board.get(j,i).getTeam()){
                        score += getPosValue(board.get(j,i),i, j);
                        score += getPieceValue(board.get(j,i));
                        score += depth;
                    } else if (board.get(j,i).occupied()) {
                        score -= getPosValue(board.get(j,i),i, j);
                        score -= getPieceValue(board.get(j,i));
                        score += depth;
                    }
                }
            }
        }
        return score;
    }

    private int getPosValue(Piece piece, int x, int y){

        if (piece instanceof Pawn){
            return (!piece.getTeam()) ? PawnValues[y][x] : PawnValues[7-y][x];

        } else if (piece instanceof Rook){
            return (!piece.getTeam()) ? RookValues[y][x] : RookValues[7-y][x];

        } else if (piece instanceof Bishop){
            return (!piece.getTeam()) ? BishopValues[y][x] : BishopValues[7-y][x];

        } else if (piece instanceof Knight){
            return (!piece.getTeam()) ? KnightValues[y][x] : KnightValues[7-y][x];

        } else if (piece instanceof Queen){
            return (!piece.getTeam()) ? QueenValues[y][x] : QueenValues[7-y][x];

        } else {
            return (!piece.getTeam()) ? KingValues[y][x] : KingValues[7-y][x];
        }
    }

    private int getPieceValue(Piece piece){
        if (piece instanceof Pawn){
            return PAWN;
        } else if (piece instanceof Rook){
            return ROOK;
        } else if (piece instanceof Bishop){
            return BISHOP;
        } else if (piece instanceof Knight){
            return KNIGHT;
        } else if (piece instanceof Queen){
            return QUEEN;
        } else {
            return KING;
        }
    }
}
