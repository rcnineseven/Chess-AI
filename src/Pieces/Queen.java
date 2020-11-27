package Pieces;

public class Queen extends Piece {

    public Queen(boolean team){
        super(team);
    }

    @Override
    public String toString() {
        return (getTeam()) ? "Q" : "q";
    }

}
