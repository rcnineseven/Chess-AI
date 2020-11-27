package Pieces;

public class Rook extends Piece {

    public Rook(boolean team){
        super(team);
    }

    @Override
    public String toString() {
        return (getTeam()) ? "R" : "r";
    }

}
