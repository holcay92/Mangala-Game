package com.company;

import java.util.*;

public class Mangala {


        private enum User {
            One ((board.length - 2) / 2, board.length - 1),
            Two (board.length - 1, (board.length - 2) / 2);
            private final int HoleLocation;
            private final int HoleSkip;

            User(int location, int skip) {
                HoleLocation = location;
                HoleSkip = skip;
            }
            int getHole() {
                return board[HoleLocation];
            }
            int getHoleLoc() {
                return HoleLocation;
            }
            int getSkip() {
                return HoleSkip;
            }

        }
        private static int[] board;
        private User turn;

        public static void main(String[] args) {
            Mangala game = new Mangala();

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
            board = new int[14];
            turn = User.One;
        }

        // Get the current turn
        // Returns the User who has the current turn
        public User getTurn() {
            return turn;
        }


        // Switches the turn from User One to User two
        public void switchTurn() {
            if (turn == User.One) {
                turn = User.Two;
            } else {
                turn = User.One;
            }
        }

        // Resets the game board
        public void reset() {
            int STARTING_AMOUNT = 4;
            Arrays.fill(board, STARTING_AMOUNT);
            for (User p : User.values()) {
                board[p.getHoleLoc()] = 0;
            }
            turn = User.One;
        }

        // Prints the current game state
        public void printBoard() {
            System.out.println("    (1) (2) (3) (4) (5) (6) ");
            System.out.println("");
           // System.out.print("|  ");
            for (int i = board.length - 2; i >= board.length / 2; i--) {
                System.out.print("| ");
                System.out.printf("%-2s",board[i]);
            }
            System.out.print("|  |\n|");
            System.out.printf("%-2d|-----------------------|%2d|\n", User.Two.getHole(), User.One.getHole());
           // System.out.print("|  ");
            for (int i = 0; i < (board.length / 2) - 1; i++) {
                System.out.print("| ");
                System.out.printf("%-2s",board[i]);
            }
            System.out.println("|  |");
            System.out.println("");
            System.out.println("    (1) (2) (3) (4) (5) (6) ");
        }

        // Returns true if the game is over (one side has no pieces)
        public boolean isOver() {
            return sum(User.One) == 0 || sum(User.Two) == 0;
        }

        // Returns an integer of the number of pieces on User m's side of the board
        public int sum(User m) {
            int sum = 0;
            int start = (m.getSkip() + 1) % board.length;
            for (int i = start; i < start + (board.length - 1) / 2; i++) {
                sum += board[i];
            }
            return sum;
        }

        // Accepts an integer position of the index of the board (0 based index)
        // Carries out a move for the User of the current turn
        // Returns true if the User gets to go again (landed in the kalah)
        public boolean markBoard(int pos) {
            int handAmount = board[pos];
            board[pos] = 0;
            while (handAmount > 0) {
                pos = (pos + 1) % board.length;
                handAmount--;
                if (pos == turn.getSkip()) {
                    pos = (pos + 1) % board.length;
                }
                board[pos]++;
            }
            boolean taken = false;
            if (pos != turn.getHoleLoc() && board[pos] == 1 && board[getOpposite(pos)] != 0) {
                board[turn.getHoleLoc()] += board[pos] + board[getOpposite(pos)];
                board[pos] = 0;
                board[getOpposite(pos)] = 0;
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
            return board.length - 2 - pos;
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
                        if (turn == User.One) {
                            position--;
                        } else {
                            position = board.length - 1 - position;
                        }
                        if (board[position] == 0) {
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

