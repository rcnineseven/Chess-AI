package AI;

import Logic.Board;
import Logic.Move;

import java.util.Random;

public class RandomAI {

    Random rand;

    public RandomAI(){
        rand = new Random();
    }

    public Move getMove(Board board){
        Move move = new Move();

        while(true) {

            int startX = rand.nextInt(8);
            int startY = rand.nextInt(8);
            int endX = rand.nextInt(8);
            int endY = rand.nextInt(8);

            if(board.get(startY,startX).occupied() && board.get(startY,startX).getTeam()) {
                move.setStart(startX, startY, board.get(startY, startX));
                move.setEnd(endX, endY);
                if(move.isValid(board)) break;
            }
        }
        return move;
    }

}
