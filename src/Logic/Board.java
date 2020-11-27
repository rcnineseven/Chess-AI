package Logic;

import Pieces.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *  Used to keep track of each piece and their relative position
 *  Contains methods for adding, removing and other utilities
 *
 *  capturedPieces and moves arrays are used to keep track
 *  of changes when AlphaBetaPruning is traversing the tree
 *  and updating the board
 *
 *  turn and winner indicate the teams (false - white, true - black)
 *
 */
public class Board{

    private Piece[][] board;
    private int totalMoves;
    private PriorityQueue<String> captured;
    private ArrayList<Piece> capturedPieces;
    private ArrayList<Move> moves;
    private boolean turn;
    private boolean gameOver;
    private boolean winner;
    private boolean draw;

    /**
     * Default board constructor with standard piece placement
     */
    public Board() {
        board = new Piece[8][8];
        captured = new PriorityQueue<>();
        turn = false;
        totalMoves = 0;
        gameOver = false;
        draw = false;
        moves = new ArrayList<>();
        capturedPieces = new ArrayList<>();
    }

    public Piece get(int xPos, int yPos){

        if(board[xPos][yPos]==null)
            return new Piece();
        return board[xPos][yPos];

    }

    public void set(Piece p, int xPos, int yPos){

        //if(!p.occupied())board[yPos][xPos] = null;
        board[yPos][xPos] = p;

    }

    public int getTotalMoves(){
        return totalMoves;
    }

    public void setWinner(boolean team){
        winner = team;
    }

    public boolean getWinner(){
        return winner;
    }

    public boolean getTurn(){
        return turn;
    }

    public void nextTurn(){
        turn = !turn;
    }

    public boolean isDraw(){
        return draw;
    }

    /**
     * Method for checking if a starting position is valid
     * i.e. if it contains a player's piece
     */
    public boolean has(int posY, int posX){

        if(posX>7 || posX<0 || posY>7 || posY<0)return false;

        String temp = get(posX, posY).toString();
        return (temp=="" || Character.isUpperCase(temp.charAt(0))) ? false : true;

    }

    /**
     * Method for checking if an end position is valid
     * i.e. if it isn't out of bounds
     */
    public boolean can(int posY, int posX){

        if(posX>7 || posX<0 || posY>7 || posY<0)return false;
        else return true;
    }

    /**
     * Updating the board - moving a piece over to a new position.
     * If capturing occurs during a move, captured piece is added to an array for bookkeeping.
     * If a move results in pawn promotion, a queen is placed in the new position instead of a pawn.
     */
    public void update(Move move, boolean team){
        if(move.captured)capturedPieces.add(board[move.getEnd().getValue()][move.getEnd().getKey()]);
        if(move.promoted)move.setPiece(new Queen(team));
        moves.add(move);
        set(null,move.getStart().getKey(),move.getStart().getValue());
        set(move.getPiece(),move.getEnd().getKey(),move.getEnd().getValue());
        totalMoves++;

        boolean whiteKing = false;
        boolean blackKing = false;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j]!= null && board[i][j].toString()=="k"){
                    whiteKing = true;
                }
            }
        }
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j]!= null && board[i][j].toString()=="K"){
                    blackKing = true;
                }
            }
        }
        gameOver = (!whiteKing || !blackKing);
        if(gameOver)setWinner(!whiteKing);
    }

    /**
     * Undoing a move moves a piece last moved to its original position
     * If capturing occurred the captured piece is placed back, and moved piece is placed
     * in it's original position.
     * If promotion occurred, it's reverted back to a pawn from a queen.
     */
    public void undoLastMove(){
        totalMoves--;
        Move move = moves.remove(moves.size()-1);
        if(move.promoted)move.setPiece(new Pawn(move.team));
        if(move.captured)set(capturedPieces.remove(capturedPieces.size()-1),move.getEnd().getKey(),move.getEnd().getValue());
        else set(null,move.getEnd().getKey(),move.getEnd().getValue());
        set(move.getPiece(),move.getStart().getKey(),move.getStart().getValue());
    }

    public void capture(Piece piece){
        captured.add(piece.toString());
    }

    public boolean isGameOver(){
        return gameOver;
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();

        out.append(" ╔════════════════════╗\n");
        for(int i=0; i<8; i++){

            out.append((8-i)+"║");

            for(int j=0; j<8; j++){

                Piece temp = board[i][j];
                if(temp==null){
                    out.append(" -");
                } else {
                    if(temp instanceof Pawn && temp.getTeam()){
                        out.append(" P");
                    } else if(temp instanceof Pawn && !temp.getTeam()){
                        out.append(" p");
                    } else if(temp instanceof Rook && temp.getTeam()){
                        out.append(" R");
                    } else if(temp instanceof Rook && !temp.getTeam()){
                        out.append(" r");
                    } else if(temp instanceof Knight && temp.getTeam()){
                        out.append(" N");
                    } else if(temp instanceof Knight && !temp.getTeam()){
                        out.append(" n");
                    } else if(temp instanceof Bishop && temp.getTeam()){
                        out.append(" B");
                    } else if(temp instanceof Bishop && !temp.getTeam()){
                        out.append(" b");
                    } else if(temp instanceof Queen && temp.getTeam()){
                        out.append(" Q");
                    } else if(temp instanceof Queen && !temp.getTeam()){
                        out.append(" q");
                    } else if(temp instanceof King && temp.getTeam()){
                        out.append(" K");
                    } else if(temp instanceof King && !temp.getTeam()){
                        out.append(" k");
                    }
                }

            }
            out.append(" ║\n");
        }
        out.append(" ╚════════════════════╝\n");
        out.append("   a b c d e f g h");
        return out.toString();
    }

}
