package Pieces;

public class Bishop extends Piece{

    public Bishop(boolean team){
        super(team);
    }

    @Override
    public String toString() {
        return (getTeam()) ? "B" : "b";
    }

}
