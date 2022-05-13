package com.company;

import java.io.IOException;
import java.util.*;

public class Mangala {
  // Game board is composed of 14 places. 12 for the holes for the pieces and 2 for the users wallet
         public static int[] mangalaGameBoard;
         public static User who;
  //There are two users, and we keep them in this array
         public static User[] users = new User[2];
  //At the beginning player presses the enter button and flips a coin to determine who is going to start
         public static int flipUser;
  //User defining class
        public static class User {
    // To determine the wallet location of the user. mangalaGameBoard[6] for the first player and
    // mangalaGameBoard[13] for the second player
            public final int userWalletLocation;
    // while distributing the pieces to skip the rival's wallet we define this
            public final int rivalsWalletLocation;
    // To determine whose turn is this
            public final int whoseTurn;
    // User class constructor
            public User(int turn, int walletLocation, int rivalsWalletLocation) {
                this.whoseTurn = turn;
                this.userWalletLocation = walletLocation;
                this.rivalsWalletLocation = rivalsWalletLocation;
            }

        }

    public static void main(String[] args) throws IOException {
        Mangala game = new Mangala();
        int user1WalletLocation = 6;
        int user2WalletLocation = 13;
        User user1 = new User(1,user1WalletLocation, 13);
        User user2 = new User(2,user2WalletLocation, 6);
        users[0] = user1 ;
        users[1] = user2 ;

            boolean playAgain = true;
            while (playAgain) {
                System.out.println("To start the game please press enter and flip a coin..");
                Scanner s = new Scanner(System.in);
                System.in.read();
                flipUser = game.flipCoin();
                System.out.println("User "+ flipUser + " will start..\n");

                game.startGame();
                game.printBoard();
// Game loop
                while (!game.isOver()) {
                    boolean again = true;
                    while (again) {
                        int position;
                        System.out.print("User " + who.whoseTurn + ", enter a  hole number (1-6):");
                        position = game.readPositionValueFromUser();
                        System.out.println("----------------------------------------------------------------");
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
            mangalaGameBoard = new int[14];
            who = users[flipUser];
        }

        // Switches the turn from User One to User two
        public void switchTurn() {
            if (who == users[0]) {
                who = users[1];
            } else {
                who = users[0];
            }
        }
    public int flipCoin() {
        Random randomNum = new Random();
        int result = randomNum.nextInt(2);
        if (result == 0) {
            return 1;
        } else {
            return 2;
        }
    }

        // Resets the game board
        public void startGame() {
            int STARTING_AMOUNT_OF_PIECES = 4;
            Arrays.fill(mangalaGameBoard, STARTING_AMOUNT_OF_PIECES);
            for (User user : users) {
                mangalaGameBoard[user.userWalletLocation] = 0;
            }
            who = users[flipUser-1];
        }

        // Prints the current game state
        public void printBoard() {
            System.out.println("             (1) (2) (3) (4) (5) (6) ");

            System.out.print("            ");
            for (int i = mangalaGameBoard.length - 2; i >= mangalaGameBoard.length / 2; i--) {
                System.out.print("| ");
                System.out.printf("%-2s", mangalaGameBoard[i]);
            }
            System.out.print("|  \nUser 2> |");
            System.out.printf("%-3d|<--------------------->|%3d| <User 1\n",
                    mangalaGameBoard[users[1].userWalletLocation], mangalaGameBoard[users[0].userWalletLocation]);

            System.out.print("            ");
            for (int i = 0; i < (mangalaGameBoard.length / 2) - 1; i++) {
                System.out.print("| ");
                System.out.printf("%-2s", mangalaGameBoard[i]);
            }
            System.out.println("|\n             (1) (2) (3) (4) (5) (6) ");

        }

        // Returns true if the game is over (one side has no pieces)
        public boolean isOver() {

            return sum(users[0]) == 0 || sum(users[1]) == 0;
        }

        // Returns an Integer of the number of pieces on User player's side of the board
        public int sum(User player) {
            int sum = 0;
            int start = (player.rivalsWalletLocation + 1) % 14;
            for (int i = start; i < start + 13 / 2; i++) {
                sum += mangalaGameBoard[i];
            }
            return sum;
        }


        public boolean markBoard(int chosenPosition) {
            int pieceAmount = mangalaGameBoard[chosenPosition];
      // if there is more than 1 piece it distributes normally
            if (pieceAmount>1){
                mangalaGameBoard[chosenPosition] = 1;
                pieceAmount = pieceAmount - 1;
            }
       // if there is only 1 piece it moves the piece in to the next hole

            else mangalaGameBoard[chosenPosition] = 0;

            while (pieceAmount > 0) {
                chosenPosition = (chosenPosition + 1) % 14;
                pieceAmount = pieceAmount - 1;
               if (chosenPosition == who.rivalsWalletLocation) {
                   chosenPosition = (chosenPosition + 1) % 14;
              }
                mangalaGameBoard[chosenPosition] = mangalaGameBoard[chosenPosition] + 1;
            }

    // if last piece is put opponent's hole and make it even
    // then you get your rival's pieces in to your wallet doesn't work for player 2
            if( mangalaGameBoard[chosenPosition] % 2 ==0){
               mangalaGameBoard[who.userWalletLocation] += mangalaGameBoard[chosenPosition];
               mangalaGameBoard[chosenPosition] = 0;
            }
    // if last piece is put in an empty hole of yours then
    // you get other user's opposite hole pieces in to your wallet
            if (chosenPosition != who.userWalletLocation && mangalaGameBoard[chosenPosition] == 1 && mangalaGameBoard[mangalaGameBoard.length - 2 -chosenPosition] != 0) {
                mangalaGameBoard[who.userWalletLocation] += mangalaGameBoard[chosenPosition] + mangalaGameBoard[mangalaGameBoard.length - 2 -chosenPosition];
                mangalaGameBoard[chosenPosition] = 0; mangalaGameBoard[12 -chosenPosition] = 0;

            }

            printBoard();
    // if last piece is put in the player's wallet then this player can play one more round
            if (!isOver() && chosenPosition == who.userWalletLocation) {
                System.out.println("Play again User " + who.whoseTurn);
                return true;
            }
            return false;
        }

        // Reads a value from a user in the console
        public int readPositionValueFromUser() {
            Scanner userInput = new Scanner(System.in);
            int position = 0;
            boolean isValid = false;
             do{
                try {
                    position = userInput.nextInt();
                    if (position < 1 || position > 6) {
                        System.out.println("Input must be between 1 and 6:");
                    } else {
                        if (who == users[0]) {
                            position = position -1;
                        } else {
                            position = 13 - position;
                        }
                        if (mangalaGameBoard[position] == 0) {
                            System.out.print("position is empty!");
                        } else {
                            isValid = true;
                        }
                    }
                }
                catch (Exception e) {
                    userInput.next();
                    System.out.println("Invalid Position, Input again (1-6) :");
                }
            }while (!isValid);
            return position;
        }
    }

