package Pieces;

public class King extends Piece {

    public King(boolean team){
        super(team);
    }

    @Override
    public String toString() {
        return (getTeam()) ? "K" : "k";
    }

}
