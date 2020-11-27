package Logic;

import Pieces.*;
import javafx.util.Pair;

import static java.lang.Math.abs;

/**
 *  Represents a move being made.
 *  Keeps track of which piece is being moved and what team that piece belongs to.
 *  Also keeps track of the starting position and the end position of said piece.
 *  When a player decides what piece to move, this class also contains all
 *  the validation methods to ensure a legal move is made.
 */
public class Move {

    private int startX, startY;
    private int endX, endY;
    private Piece piece;
    boolean team;
    boolean valid;
    boolean captured;
    boolean promoted;

    public Move(){
        valid = false;
        captured = false;
        promoted = false;
    }

    public void setStart(int xPos, int yPos, Piece piece){
        startX = xPos;
        startY = yPos;
        team = piece.getTeam();
        this.piece = piece;
    }

    public void setEnd(int xPos, int yPos){
        endX = xPos;
        endY= yPos;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Pair<Integer,Integer> getStart(){
        return new Pair<>(startX,startY);
    }

    public Pair<Integer,Integer> getEnd(){
        return new Pair<>(endX,endY);
    }

    public Piece getPiece(){
        return piece;
    }

    /**
     *  Used to validate the moves
     */
    public boolean isValid(Board board){

        if(!board.get(endY,endX).occupied() || board.get(endY,endX).getTeam()!=piece.getTeam()){
            valid = true;
        } else {
            return false;
        }

        if(piece instanceof Pawn){

            valid = false;
            /**
             * Checking if it moves too far in both directions
             */
            int max = ((Pawn) piece).ifFirst() ? 2 : 1;
            if(abs(startY-endY)>max)return false;
            if(abs(startX-endX)>1)return false;
            if(startX!=endX && abs(startY-endY)>1)return false;

            /**
             * Because the direction of Pawns movement depends on which team it belongs
             * the team is validated beforehand to determine if it's moving up or down the board.
             * This method also detects if a Pawn is capturing a piece or if it's being promoted.
             */
            if(!piece.getTeam()){
                if(startY<endY)return false;
                if(startX==endX){
                    for(int y = startY-1; y>=endY; y--) {
                        if (board.get(y, startX).occupied())
                            return false;
                    }
                    if(endY==0) promote();
                    return true;
                }else {
                    if(board.get(endY, endX).occupied()) {
                        if ((startX-endX)==-1 && board.get(endY,endX).getTeam()!=piece.getTeam()){
                            if(endY==0) promote();
                            captured = true;
                            return true;
                        }
                        if ((startX-endX)== 1 && board.get(endY,endX).getTeam()!=piece.getTeam()){
                            if(endY==0) promote();
                            captured = true;
                            return true;
                        }
                        return false;
                    } else return false;
                }
            } else {
                if(startY>endY)return false;
                if(startX==endX){
                    for(int y = startY+1; y<=endY; y++) {
                        if (board.get(y, startX).occupied())
                            return false;
                    }
                    if(endY==7) promote();
                    return true;
                }else {
                    if(board.get(endY, endX).occupied()) {
                        if ((startX-endX)==-1 && board.get(endY,endX).getTeam()!=piece.getTeam()){
                            if(endY==7) promote();
                            captured = true;
                            return true;
                        }
                        if ((startX-endX)== 1 && board.get(endY,endX).getTeam()!=piece.getTeam()){
                            if(endY==7) promote();
                            captured = true;
                            return true;
                        }
                        return false;
                    } else return false;
                }
            }
        }


        if(piece instanceof Rook){

            /**
             *  If it's a rook moving in each of the four directions
             *  this method validates whether there's a piece in it's way
             */
            if(startX==endX && startY>endY){
                for(int y = (startY-1); y>endY; y--)
                    if(board.get(y,startX).occupied())return false;

            }else if(startX==endX && startY<endY){
                for(int y = (startY+1); y<endY; y++)
                    if(board.get(y,startX).occupied())return false;

            }else if(startY==endY && startX>endX){
                for(int x = (startX-1); x>endX; x--)
                    if (board.get(startY, x).occupied()) return false;

            }else if(startY==endY && startX<endX){
                for(int x = (startX+1); x<endX; x++)
                    if (board.get(startY, x).occupied()) return false;
            }else{
                return false;
            }
        }


        if(piece instanceof Knight) {

            /**
             * Validating knights moves.
             * Since Knight can jump over any piece we just
             * validate whether the end position can be reached without
             * breaking rules relative to it's starting position.
             */
            valid = false;
            if(((startX-endX)==-1) && ((startY-endY)==2))valid = true;
            if(((startX-endX)==-2) && ((startY-endY)==1))valid = true;
            if(((startX-endX)==-2) && ((startY-endY)==-1))valid = true;
            if(((startX-endX)==-1) && ((startY-endY)==-2))valid = true;
            if(((startX-endX)==1) && ((startY-endY)==-2))valid = true;
            if(((startX-endX)==2) && ((startY-endY)==-1))valid = true;
            if(((startX-endX)==2) && ((startY-endY)==1))valid = true;
            if(((startX-endX)==1) && ((startY-endY)==2))valid = true;

        }


        if(piece instanceof Bishop){

            /**
             * Checking bishops movement involves diagonal array traversal
             * and checking whether there is any piece in between bishops
             * starting position and it's end position.
             */
            int x = startX;
            boolean inLine = false;
            if (startX < endX && startY > endY) {
                for (int y = (startY - 1); y >= endY; y--) {
                    x++;
                    if (x == endX && y == endY) inLine = true;
                    if ((x<8) && y != endY && board.get(y, x).occupied()) return false;
                }
            } else if (startX < endX && startY < endY) {
                for (int y = (startY + 1); y <= endY; y++) {
                    x++;
                    if (x == endX && y == endY) inLine = true;
                    if ((x<8) && y != endY && board.get(y, x).occupied()) return false;
                }
            } else if (startX > endX && startY < endY) {
                for (int y = (startY + 1); y <= endY; y++) {
                    x--;
                    if (x == endX && y == endY) inLine = true;
                    if ((x>-1) && y != endY && board.get(y, x).occupied()) return false;
                }
            } else if (startX > endX && startY > endY) {
                for (int y = (startY - 1); y >= endY; y--) {
                    x--;
                    if (x == endX && y == endY) inLine = true;
                    if ((x>-1) && y != endY && board.get(y, x).occupied()) return false;
                }
            } else {
                return false;
            }
            if (!inLine) return false;
        }


        if(piece instanceof  Queen){
            /**
             * Queen movement is the combination of a Rook's and Bishops movement
             * so code is an identical combination of both.
             */
            int x = startX;
            boolean inLine = false;
            if(startX==endX && startY>endY){
                inLine = true;
                for(int y = (startY-1); y>endY; y--)
                    if(board.get(y,startX).occupied()) return false;

            }else if(startX==endX && startY<endY){
                inLine = true;
                for(int y = (startY+1); y<endY; y++)
                    if(board.get(y,startX).occupied()) return false;

            }else if(startY==endY && startX>endX){
                inLine = true;
                for(int x2 = (startX-1); x2>endX; x2--)
                    if (board.get(startY, x2).occupied()) return false;

            }else if(startY==endY && startX<endX){
                inLine = true;
                for(int x2 = (startX+1); x2<endX; x2++)
                    if (board.get(startY, x2).occupied()) return false;

            }else if(startX < endX && startY > endY){
                for(int y = (startY-1); y>=endY; y--){
                    x++;
                    if (x==endX && y==endY) inLine=true;
                    if ((x<8) && y!=endY && board.get(y, x).occupied()) return false;
                }
            }else if(startX<endX && startY<endY){
                for(int y = (startY+1); y<=endY; y++){
                    x++;
                    if (x==endX && y==endY) inLine=true;
                    if ((x<8) && y!=endY && board.get(y, x).occupied()) return false;
                }
            }else if(startX>endX && startY<endY){
                for(int y = (startY+1); y<=endY; y++) {
                    x--;
                    if (x==endX && y==endY) inLine=true;
                    if ((x>-1) && y!=endY && board.get(y, x).occupied()) return false;
                }
            }else if(startX>endX && startY>endY){
                for(int y = (startY-1); y>=endY; y--) {
                    x--;
                    if (x==endX && y==endY) inLine=true;
                    if ((x>-1) && y!=endY && board.get(y, x).occupied()) return false;
                }
            }else{
                return false;
            }
            if(!inLine)return false;
        }

        /**
         * Kings movement is simple since it can move just 1 square in any direction,
         * so the end position is validate
         * relative to it's starting position.
         */
        if(piece instanceof  King){

            valid = false;
            if(((startX-endX)==-1) && ((startY-endY)==1))valid = true;
            if(((startX-endX)==-1) && ((startY-endY)==0))valid = true;
            if(((startX-endX)==-1) && ((startY-endY)==-1))valid = true;
            if(((startX-endX)==0) && ((startY-endY)==-1))valid = true;
            if(((startX-endX)==1) && ((startY-endY)==-1))valid = true;
            if(((startX-endX)==1) && ((startY-endY)==0))valid = true;
            if(((startX-endX)==1) && ((startY-endY)==1))valid = true;
            if(((startX-endX)==0) && ((startY-endY)==1))valid = true;
            valid = (valid && (!board.get(endY,endX).occupied() || board.get(endY,endX).getTeam()!=piece.getTeam()));

        }

        if(board.get(endY,endX).occupied() && valid)captured=true;
        return valid;
    }

    public void promote(){
        promoted = true;
    }

    public boolean isPromoted(){
        return promoted;
    }


    public boolean isCaptured(){
        return captured;
    }

    /**
     * Used to format the start and end positions into algebraic notation
     * Converts the numeric representation of a move
     * into a proper algebraic notation of chess moves.
     */
    @Override
    public String toString() {
        char x = (char)(97-getEnd().getKey());
        int y = (8-(getEnd().getValue()));
        return x+""+y;
    }
}
