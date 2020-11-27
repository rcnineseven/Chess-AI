import Logic.Match;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Chess {

    /**
     
     * Developed by Rahul Chawla 
     */

    public static void main(String[] args) throws IOException {

        System.out.println("Hello World! COSC 3p71 - Chess");

        while(true){

            Scanner in = new Scanner(System.in);
            int choice1 = 0;
            int choice2 = 0;
            int depth = 0;
            System.out.println( "Please select the Game mode\n"+
                                "  1. Play against minimax AI\n"+
                                "  2. Play against random AI\n"+
                                "  3. Exit Program");
            try {
                choice1 = in.nextInt();
            } catch(InputMismatchException e){choice1=-1;}

            if(choice1<1 || choice1>3){

                System.out.println("Please enter a valid selection!!!");

            }else if (choice1==3) {

                System.exit(0);

            }else{
                System.out.println( "Please select board layout:\n" +
                                    "  1. Standard new game board\n" +
                                    "  2. Custom game board");
                try {
                    choice2 = in.nextInt();
                } catch(InputMismatchException e){choice2=-1;}

                if(choice2<1 || choice2>3)System.out.println("Please enter a valid selection!!!");

                if (choice1 == 1) {
                    //int param1, param2, param3;
                    System.out.println("Please set AI traversal depth:\n");
                    while(true) {
                        try {
                            depth = in.nextInt();
                        } catch (InputMismatchException e) {}
                        if(depth<=1)System.out.println("Traversal depth has to more than 1!");
                        else break;
                    }
                }
                Match game = new Match(choice1, choice2, depth);
                game.start();
            }
        }

    }
}
