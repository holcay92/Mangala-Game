package com.company;

import java.util.*;

public class Mangala {

         public static int[] gameBoard;
         public static User who;
         public static User[] users = new User[2];

        public static class User {

            public final int walletLocation;
            public final int holeSkip;
            public final int turn;

            public User(int turn, int walletLocation, int holeSkip) {
                this.turn = turn;
                this.walletLocation = walletLocation;
                this.holeSkip = holeSkip;
            }

        }

    public static void main(String[] args) {
        Mangala game = new Mangala();
        int user1WalletLocation = (gameBoard.length - 2) / 2;
        int user2WalletLocation = (gameBoard.length - 1);
        User user1 = new User(1,user1WalletLocation, gameBoard.length - 1);
        User user2 = new User(2,user2WalletLocation, (gameBoard.length - 2) / 2);
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

                            System.out.print("User " + who.turn+ ", enter a  hole number (1-6):");
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
            who = users[0];
        }

        // Get the current turn
        // Returns the User who has the current turn
        public User getTurn() {
            return who;
        }


        // Switches the turn from User One to User two
        public void switchTurn() {
            if (who == users[0]) {
                who = users[1];
            } else {
                who = users[0];
            }
        }

        // Resets the game board
        public void reset() {
            int STARTING_AMOUNT = 4;
            Arrays.fill(gameBoard, STARTING_AMOUNT);
            for (User user : users) {
                gameBoard[user.walletLocation] = 0;
            }
            who = users[0];
        }

        // Prints the current game state
        public void printBoard() {
            System.out.println("             (1) (2) (3) (4) (5) (6) ");

            System.out.print("            ");
            for (int i = gameBoard.length - 2; i >= gameBoard.length / 2; i--) {
                System.out.print("| ");
                System.out.printf("%-2s", gameBoard[i]);
            }
            System.out.print("|  \nUser 2> |");
            System.out.printf("%-3d|<--------------------->|%3d| <User 1\n",
                    gameBoard[users[1].walletLocation], gameBoard[users[0].walletLocation]);

            System.out.print("            ");
            for (int i = 0; i < (gameBoard.length / 2) - 1; i++) {
                System.out.print("| ");
                System.out.printf("%-2s", gameBoard[i]);
            }
            System.out.println("|\n             (1) (2) (3) (4) (5) (6) ");

        }

        // Returns true if the game is over (one side has no pieces)
        public boolean isOver() {

            return sum(users[0]) == 0 || sum(users[1]) == 0;
        }

        // Returns an integer of the number of pieces on User person's side of the board
        public int sum(User person) {
            int sum = 0;
            int start = (person.holeSkip + 1) % gameBoard.length;
            for (int i = start; i < start + (gameBoard.length - 1) / 2; i++) {
                sum += gameBoard[i];
            }
            return sum;
        }

        // Accepts an integer position of the index of the board (0 based index)
        // Carries out a move for the User of the current turn
        // Returns true if the User gets to go again (landed in the wallet)
        public boolean markBoard(int pos) {
            int pieceAmount = gameBoard[pos];

      // if there is more than 1 piece it distributes normally

            if (pieceAmount>1){
                gameBoard[pos] = 1;
                pieceAmount = pieceAmount - 1;
            }
       // if there is only 1 piece it moves the piece in to the next hole

            else gameBoard[pos] = 0;

            while (pieceAmount > 0) {
                pos = (pos + 1) % 14;
                pieceAmount = pieceAmount - 1;
               if (pos == who.holeSkip) {
                   pos = (pos + 1) % 14;
              }
                gameBoard[pos] = gameBoard[pos] + 1;
            }
            boolean taken = false;
    // if last piece is put opponent's hole and make it even then you get your rival's pieces in to your wallet doesn't work for player 2
            if(pos + pieceAmount > 5 && gameBoard[pos] % 2 ==0){
               gameBoard[who.walletLocation] += gameBoard[pos];
               gameBoard[pos] = 0;
            }
    // if last piece is put in an empty hole of yours then you get other user's opposite hole pieces in to your wallet
            if (pos != who.walletLocation && gameBoard[pos] == 1 && gameBoard[getOpposite(pos)] != 0) {
                gameBoard[who.walletLocation] += gameBoard[pos] + gameBoard[getOpposite(pos)];
                gameBoard[pos] = 0;
                gameBoard[getOpposite(pos)] = 0;
                taken = true;
            }

            printBoard();
            if (!isOver() && pos == who.walletLocation) {
                System.out.println("Play again User " + who.turn);
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
            Scanner userInput = new Scanner(System.in);
            int position = 2;
            boolean isValid = false;
             do{
                try {
                    position = userInput.nextInt();
                    if (position < 1 || position > 6) {
                        System.out.println("Input must be between 1 and 6:");
                    } else {
                        if (who == users[0]) {
                            position--;
                        } else {
                            position = gameBoard.length - 1 - position;
                        }
                        if (gameBoard[position] == 0) {
                            System.out.print("position is empty!");
                        } else {
                            isValid = true;
                        }
                    }
                }
                catch (Exception e) {
                    userInput.next(); // Throw away the offending input
                    System.out.println("Invalid Position, input again (1-6) :");
                }
            }while (!isValid);
            return position;
        }
    }

