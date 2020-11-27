package Pieces;

public class Piece {

    /**
     * This represents a piece on the board.
     * Keeps track of which team the piece belongs to.
     *
     * If there is an empty square this class represents it
     * as well by returning a false if the occupied method is called.
     *
     * The children classes extending Piece returns appropriate
     * String according to WHAT the piece is.
     *
     *      Capital letters - Black Team
     *          P - Pawn
     *          N - Knight
     *          B - Bishop
     *          R - Rook
     *          Q - queen
     *          K - King
     */
    private boolean team;
    private boolean nullPiece;

    public Piece(){
        nullPiece=true;
    }

    public Piece(boolean team){

        nullPiece = false;
        this.team = team;

    }

    public boolean getTeam(){
        return team;
    }

    public boolean occupied() {
        return !nullPiece;
    }

    @Override
    public String toString(){
        return "";
    }
}
