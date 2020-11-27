package Logic;

import AI.AlphaBetaPruning;
import AI.RandomAI;
import Pieces.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *  Represents a match, keeping track of user and AI moves.
 *  Once the game is done, the winner and total moves made
 *  is announced.
 *  The moves are stored in a text file output.txt once
 *  a game is over. The file gets re-written after every
 *  New Match so be sure to copy results for preservation/
 */
public class Match {

    Scanner in = new Scanner(System.in);
    Board board;
    private boolean gameOver;
    boolean randomAI;
    int depth;
    StringBuilder output;
    FileWriter file;

    public Match(int choice1, int choice2, int depth) {
        gameOver = false;
        board = new Board();
        this.depth = depth;
        output = new StringBuilder();
        if(choice1==2)randomAI=true;
        if(choice2==1)standardLayout();
        else customLayout();
    }

    /**
     * Standard layout
     */
    public void standardLayout() {

        for (int i = 0; i < 8; i++) {
            board.set(new Pawn(false), i, 6);
            board.set(new Pawn(true), i, 1);
        }

        board.set(new Rook(false), 0, 7);
        board.set(new Rook(false), 7, 7);
        board.set(new Rook(true), 0, 0);
        board.set(new Rook(true), 7, 0);

        board.set(new Knight(false), 1, 7);
        board.set(new Knight(false), 6, 7);
        board.set(new Knight(true), 1, 0);
        board.set(new Knight(true), 6, 0);

        board.set(new Bishop(false), 2, 7);
        board.set(new Bishop(false), 5, 7);
        board.set(new Bishop(true), 2, 0);
        board.set(new Bishop(true), 5, 0);

        board.set(new Queen(false), 3, 7);
        board.set(new Queen(true), 3, 0);

        board.set(new King(false), 4, 7);
        board.set(new King(true), 4, 0);

    }

    /**
     * Custom layout
     */
    public void customLayout() {
        Scanner in = new Scanner(System.in);
        String s = new String();
        boolean team = true;
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++) {
                System.out.println(board.toString());
                if (team) {
                    System.out.println("Black piece to place at file " + ((char) (97 + x))
                            + " rank " + (8 - y) + " (p, r, n, b, q, k, - for empty)"
                            + " c to change team, x to start game");
                } else {
                    System.out.println("White piece to place at file " + ((char) (97 + x))
                            + " rank " + (8 - y) + " (p, r, n, b, q, k, - for empty)"
                            + " c to change team, x to start game");
                }
                try {
                    s = in.next();
                } catch (InputMismatchException e) {}
                if (s.equals("c")) {
                    team = !team;
                    x--;
                } else if(s.equals("x")) {
                    x=8;
                    y=8;
                }else if(s.equals("p")){
                    board.set(new Pawn(team), x, y);
                }else if(s.equals("r")){
                    board.set(new Rook(team), x, y);
                }else if(s.equals("n")){
                    board.set(new Knight(team), x, y);
                }else if(s.equals("b")){
                    board.set(new Bishop(team), x, y);
                }else if(s.equals("q")){
                    board.set(new Queen(team), x, y);
                }else if(s.equals("k")){
                    board.set(new King(team), x, y);
                } else if(s.equals("-")){
                    board.set(null, x, y);
                }
            }
        }
    }

    /**
     * At the start of a match a loop is ran which terminates when there
     * is a game end scenario. Else it proceeds to ask user for input and
     * retrieves the AI movements while updating the game sate and board.
     */
    public void start() throws IOException {
        int currentMove = 0;
        file = new FileWriter("./output.txt");
        System.out.println(board.toString());
        Move move;
        while (!gameOver) {
            currentMove++;
            output.append(currentMove+". ");
            move = getPlayerMove();
            updateBoard(move, false);
            output.append(move.toString()+" ");
            updateGameOver();
            System.out.println("\n"+board.toString());
            if (gameOver) break;
            move = getBotMove();
            updateBoard(move, true);
            output.append(move.toString()+"\n");
            updateGameOver();
            System.out.println("\n"+board.toString());
        }
        System.out.println(board.toString());
        System.out.println("\nGame over!");
        System.out.println("Total moves: "+board.getTotalMoves());
        System.out.println((board.getWinner())? "Black wins!" : "White wins!");
        file.write(output.toString());
        file.close();
    }

    /**
     * Validating end game
     */
    public void updateGameOver() {
        gameOver = board.isGameOver();
    }

    /**
     * Prompts user to input a starting position followed by an end position.
     * After which the move is validated and if it passes gets returned to be
     * placed on a board.
     */
    public Move getPlayerMove() {

        Move move;
        while (true) {
            move = getInput();
            if (!move.isValid(board)) {
                System.out.println("Illegal move!");
            } else {
                if (move.isCaptured()) {
                    board.capture(board.get(move.getStart().getKey(), move.getStart().getValue()));
                } else if(move.isPromoted()){
                    move.setStart(move.getStart().getKey(), move.getStart().getValue(),new Queen(false));
                }
                break;
            }
        }
        return move;
    }

    public Move getInput() {
        String s;
        int tempX = 0;
        int tempY = 0;
        Move move = new Move();
        boolean accepted = false;
        System.out.println("Your move.");
        while (!accepted) {
            //ACCEPT PIECE SELECTION AND VALIDATE
            try {
                while (true) {
                    System.out.println("Select piece to move:");
                    s = in.nextLine();
                    if (s.length()>2 || s.length()<2) {
                        System.out.println("Enter input using algebraic notation (e5, a2, etc.)!");
                    } else{
                        tempX = (s.charAt(0) - 97);
                        tempY = (7 - (s.charAt(1) - 49));
                        if (!board.can(tempX, tempY)) {
                            System.out.println("Please select a square that's on the board!");
                            break;
                        }else if(!board.has(tempX, tempY)){
                            System.out.println("Please select a piece that belongs to you!");
                            break;
                        } else {
                            move.setStart(tempX, tempY, board.get(tempY, tempX));
                        }
                        //ACCEPT PIECE DESTINATION AND VALIDATE
                        System.out.println("Select where to move it:");
                        s = in.nextLine();
                        if (s.length()>2 || s.length()<2) {
                            System.out.println("Enter input using algebraic notation (e5, a2, etc.)!");
                        } else {
                            tempX = (s.charAt(0) - 97);
                            tempY = (7 - (s.charAt(1) - 49));
                            if (!board.can(tempX, tempY)) {
                                System.out.println("Please select a square that's on the board!");
                                break;
                            } else {
                                move.setEnd(tempX, tempY);
                                accepted = true;
                                break;
                            }
                        }
                    }
                }
            } catch (InputMismatchException e) {}
        }
        return move;
    }

    /**
     * Retrieving next MOVE from AI
     */
    public Move getBotMove() {
        Move move;
        if(randomAI){
            RandomAI bot = new RandomAI();
            move = bot.getMove(board);
        } else {
            AlphaBetaPruning bot = new AlphaBetaPruning(board, 4);
            move = bot.getMove();
        }
        return move;
    }

    public void updateBoard(Move move, boolean team){

        if(move.getPiece() instanceof Pawn){
            ((Pawn) move.getPiece()).notFirst();
        }

        board.update(move, team);

    }
}
