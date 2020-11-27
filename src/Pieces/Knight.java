package Pieces;

public class Knight extends Piece {

    public Knight(boolean team){
        super(team);
    }

    @Override
    public String toString() {
        return (getTeam()) ? "N" : "n";
    }
}
