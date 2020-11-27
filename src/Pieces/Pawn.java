package Pieces;

public class Pawn extends Piece {

    private boolean firstMove;

    /**
     * This is the only child class of Piece that differs
     * by keeping track whether it's first move or not.
     * As movement depends on it.
     */
    public Pawn(boolean team){
        super(team);
        firstMove = true;
    }

    public boolean ifFirst(){
        return firstMove;
    }

    public void notFirst(){
        firstMove = false;
    }

    @Override
    public String toString() {
        return (getTeam()) ? "P" : "p";
    }
}
