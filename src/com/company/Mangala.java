package com.company;

import java.util.*;

public class Mangala {


        public static class User {

            public final int HoleLocation;
            public final int HoleSkip;

           public User(int location, int skip) {
                HoleLocation = location;
                HoleSkip = skip;
            }

            int getHole() {
                return gameBoard[HoleLocation];
            }
            int getHoleLoc() {
                return HoleLocation;
            }
            int getSkip() {
                return HoleSkip;
            }
        }
        private static int[] gameBoard;
        private User turn;
        public static User[] users = new User[2];
       
        
    public static void main(String[] args) {
        Mangala game = new Mangala();

        User user1 = new User((gameBoard.length - 2) / 2, gameBoard.length - 1);
        User user2 = new User((gameBoard.length - 1), (gameBoard.length - 2) / 2);
        users[0] = user1 ;
        users[1] = user2 ;

            boolean playAgain = true;
            while (playAgain) {
                Scanner s = new Scanner(System.in);
                game.reset();
                game.printBoard();
// Game loop
                while (!game.isOver()) {
                    boolean again = true;
                    while (again) {
                        int position = 0;
                            System.out.print("User " + game.getTurn() + ", enter a  hole number (1-6):");
                            position = game.readValue();

                        again = game.markBoard(position);
                    }
                    game.switchTurn();

                }
                System.out.println("The game is over!");
                System.exit(0);

            }
        }

        // Create a game object
        public Mangala() {
            gameBoard = new int[14];
            turn = users[0];
        }

        // Get the current turn
        // Returns the User who has the current turn
        public User getTurn() {
            return turn;
        }


        // Switches the turn from User One to User two
        public void switchTurn() {
            if (turn == users[0]) {
                turn = users[1];
            } else {
                turn = users[0];
            }
        }

        // Resets the game board
        public void reset() {
            int STARTING_AMOUNT = 4;
            Arrays.fill(gameBoard, STARTING_AMOUNT);
            for (User p : users) {
                gameBoard[p.getHoleLoc()] = 0;
            }
            turn = users[0];
        }

        // Prints the current game state
        public void printBoard() {
            System.out.println(" (1) (2) (3) (4) (5) (6) ");
            System.out.println("");
           // System.out.print("|  ");
            for (int i = gameBoard.length - 2; i >= gameBoard.length / 2; i--) {
                System.out.print("| ");
                System.out.printf("%-2s", gameBoard[i]);
            }
            System.out.print("|  |\n|");
            System.out.printf("%-2d|-----------------------|%2d|\n", users[1].getHole(), users[0].getHole());
           // System.out.print("|  ");
            for (int i = 0; i < (gameBoard.length / 2) - 1; i++) {
                System.out.print("| ");
                System.out.printf("%-2s", gameBoard[i]);
            }
            System.out.println("|  |");
            System.out.println("");
            System.out.println(" (1) (2) (3) (4) (5) (6) ");
        }

        // Returns true if the game is over (one side has no pieces)
        public boolean isOver() {
            return sum(users[0]) == 0 || sum(users[1]) == 0;
        }

        // Returns an integer of the number of pieces on User person's side of the board
        public int sum(User person) {
            int sum = 0;
            int start = (person.getSkip() + 1) % gameBoard.length;
            for (int i = start; i < start + (gameBoard.length - 1) / 2; i++) {
                sum += gameBoard[i];
            }
            return sum;
        }

        // Accepts an integer position of the index of the board (0 based index)
        // Carries out a move for the User of the current turn
        // Returns true if the User gets to go again (landed in the kalah)
        public boolean markBoard(int pos) {
            int handAmount = gameBoard[pos];
            gameBoard[pos] = 0;
            while (handAmount > 0) {
                pos = (pos + 1) % gameBoard.length;
                handAmount--;
                if (pos == turn.getSkip()) {
                    pos = (pos + 1) % gameBoard.length;
                }
                gameBoard[pos]++;
            }
            boolean taken = false;
            if (pos != turn.getHoleLoc() && gameBoard[pos] == 1 && gameBoard[getOpposite(pos)] != 0) {
                gameBoard[turn.getHoleLoc()] += gameBoard[pos] + gameBoard[getOpposite(pos)];
                gameBoard[pos] = 0;
                gameBoard[getOpposite(pos)] = 0;
                taken = true;
            }
            printBoard();
            if (!isOver() && pos == turn.getHoleLoc()) {
                System.out.println("Play again "+turn);
                return true;
            }
            return false;
        }

        // Returns index of the opposite position on the board
        private int getOpposite(int pos) {
            return gameBoard.length - 2 - pos;
        }

        // Reads a value from a scanner in the console
        public int readValue() {
            Scanner s = new Scanner(System.in);
            int position = 2;
            boolean valid = false;
            while (!valid) {
                try {
                    position = s.nextInt();
                    if (position < 1 || position > 6) {
                        System.out.println("Input must be between 1 and 6:");
                    } else {
                        if (turn == users[0]) {
                            position--;
                        } else {
                            position = gameBoard.length - 1 - position;
                        }
                        if (gameBoard[position] == 0) {
                            System.out.print("Hole is empty!");
                        } else {
                            valid = true;
                        }
                    }
                }
                catch (Exception e) {
                    s.next(); // Throw away the offending input
                    System.out.println("Invalid Position, input again:");
                }
            }
            return position;
        }
    }

