package com.company;
import java.io.IOException;
import java.util.*;

public class Mangala {
        // Game board is composed of 14 places. 12 for the holes for the pieces and 2 for the users wallet
         public static int[] mangalaGameBoard;
        // Current player
         public static User who;
        // There are two users, and we keep them in this array
         public static User[] users = new User[2];
        // At the beginning player presses the enter button and flips a coin to determine who is going to start
         public static int flipUser;
         public static int user1Sum;
         public static int user2Sum;
         // User defining class
        public static class User {
            // To determine the wallet location of the user. mangalaGameBoard[6] for the first player and
            // mangalaGameBoard[13] for the second player
            public final int userWalletLocation;
            // while distributing the pieces to skip the rival's wallet we define this
            public final int rivalWalletLocation;
            // To determine whose turn is this
            public final int whoseTurn;

      // User class constructor
            public User( int turn, int playerWalletLocation, int rivalWalletLocation) {
                this.userWalletLocation = playerWalletLocation;
                this.rivalWalletLocation = rivalWalletLocation;
                this.whoseTurn = turn;
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
                game.printCurrentBoardStatus();
// Game loop
                while (!game.isGameOver()) {
                    boolean playGame = true;
                    while (playGame) {
                        int position;
                        System.out.print("User " + who.whoseTurn + ", enter a  hole number (1-6):");
                        position = game.readPositionValueFromUser();
                        System.out.println("----------------------------------------------------------------");
                        playGame = game.gameMechanic(position);
                    }
                    game.switchTurn();
                }
                //if all the holes of user1 are empty then it collects the remaining stones of user2.
                if(user1Sum == 0){
                    for(int i = 0 ; i < 6 ; i++ ){
                        mangalaGameBoard[users[0].userWalletLocation] += mangalaGameBoard[i+7];
                    }
                }
                //if all the holes of user2 are empty then it collects the remaining stones of user1.
                else if(user2Sum == 0){
                    for(int i = 0 ; i < 6 ; i++ ){
                        mangalaGameBoard[users[1].userWalletLocation] += mangalaGameBoard[i];
                    }
                }
                //default random winner value
                int winner = 0;
                // who has the more stones is the winner
                if(mangalaGameBoard[users[1].userWalletLocation] < mangalaGameBoard[users[0].userWalletLocation]){
                    winner = 1;
                }else if (mangalaGameBoard[users[0].userWalletLocation] < mangalaGameBoard[users[1].userWalletLocation]){
                    winner = 2;
                }else{
                    System.out.println("The game is over! No winner :)  ");System.exit(0);
                }
                System.out.println("The game is over! The winner is User "+ winner);
                System.out.println("Scores:\nUser 1: " + mangalaGameBoard[users[0].userWalletLocation]+
                        " User 2: " + mangalaGameBoard[users[1].userWalletLocation]);
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

        // Starting the game
        public void startGame() {
            int STARTING_AMOUNT_OF_PIECES = 4;
            Arrays.fill(mangalaGameBoard, STARTING_AMOUNT_OF_PIECES);
            for (User user : users) {
                mangalaGameBoard[user.userWalletLocation] = 0;
            }
            who = users[flipUser-1];
        }

        // Prints the current game state
        public void printCurrentBoardStatus() {
            System.out.println("             (1) (2) (3) (4) (5) (6) ");

            System.out.print("            ");
            for (int i = 12; i >= 7; i--) {
                System.out.print("| ");
                System.out.printf("%-2s", mangalaGameBoard[i]);
            }
            System.out.print("|\n             <<<<<<<<<<<<<<<<<<<<<<<");
            System.out.print("   \nUser 2> |");
            System.out.printf("%-3d|-----------------------|%3d| <User 1",
                    mangalaGameBoard[users[1].userWalletLocation], mangalaGameBoard[users[0].userWalletLocation]);
            System.out.println("\n             >>>>>>>>>>>>>>>>>>>>>>>");
            System.out.print("            ");
            for (int i = 0; i < 6; i++) {
                System.out.print("| ");
                System.out.printf("%-2s", mangalaGameBoard[i]);
            }
            System.out.println("|\n             (1) (2) (3) (4) (5) (6) ");

        }

        // Returns true if the game is over (one side has no pieces)
        public boolean isGameOver() {
            boolean isEmpty = false;
            user1Sum = sum(users[0]);
            user2Sum = sum(users[1]);
            if(user1Sum == 0 || user2Sum == 0){
                isEmpty = true;
            }
            return isEmpty;
        }
        // calculates the number of pieces on player's side of the board
        public int sum(User player) {
            int sum = 0;
            int start = (player.rivalWalletLocation + 1) % 14;
            for (int i = start; i < start + 13 / 2; i++) {
                sum += mangalaGameBoard[i];
            }
            return sum;
        }

        public boolean gameMechanic(int chosenPosition) {
            int pieceAmount = mangalaGameBoard[chosenPosition];
            int initChosenPosition = chosenPosition;
            int initHandAmount = pieceAmount;
      // if there is more than 1 piece it distributes normally
            if (pieceAmount>1){
                mangalaGameBoard[chosenPosition] = 1;
                pieceAmount = pieceAmount - 1;
            }
       // if there is only 1 piece it moves the piece in to the next hole
            else mangalaGameBoard[chosenPosition] = 0;

            while (pieceAmount > 0) {
               // System.out.println("chosen position track :"+chosenPosition);
                chosenPosition = (chosenPosition + 1) % 14;
                pieceAmount = pieceAmount - 1;
                if (chosenPosition == who.rivalWalletLocation) {
                   chosenPosition = (chosenPosition + 1) % 14;
                }
                mangalaGameBoard[chosenPosition] = mangalaGameBoard[chosenPosition] + 1;
            }

            // if last piece is put in an empty hole of yours then
            // you get other user's opposite hole pieces into your wallet
            if(who.whoseTurn == users[0].whoseTurn && chosenPosition != who.userWalletLocation && chosenPosition < 6 &&
                    mangalaGameBoard[chosenPosition] == 1 && mangalaGameBoard[12- chosenPosition] != 0){
                mangalaGameBoard[who.userWalletLocation] += 1 + mangalaGameBoard[12 - chosenPosition];
                mangalaGameBoard[chosenPosition] = 0; mangalaGameBoard[12 -chosenPosition] = 0;
                System.out.println("That's an empty hole. Great job User "+users[0].whoseTurn +", you got your rival's stones");
            }
            else if (who.whoseTurn == users[1].whoseTurn && chosenPosition != who.userWalletLocation && chosenPosition > 6  &&
                    mangalaGameBoard[chosenPosition] == 1 && mangalaGameBoard[12 - chosenPosition] != 0){
                mangalaGameBoard[who.userWalletLocation] += 1 + mangalaGameBoard[12 - chosenPosition];
                mangalaGameBoard[chosenPosition] = 0; mangalaGameBoard[12 - chosenPosition] = 0;
                System.out.println("That's an empty hole. Great job User "+users[1].whoseTurn +", you got your rival's stones");
            }

            // if last piece is put opponent's hole and make it even
            // then you get your rival's stones into your wallet
            if(who.whoseTurn == users[0].whoseTurn &&(initChosenPosition+initHandAmount)>7
                    && mangalaGameBoard[chosenPosition] % 2 ==0){
                mangalaGameBoard[who.userWalletLocation] += mangalaGameBoard[chosenPosition];
                mangalaGameBoard[chosenPosition] = 0;
                System.out.println("That's an even number. Great job "+users[0].whoseTurn +", you got your rival's stones");
            }
            if(who.whoseTurn == users[1].whoseTurn &&(initChosenPosition+initHandAmount)>14
                    && mangalaGameBoard[chosenPosition] % 2 ==0){
                mangalaGameBoard[who.userWalletLocation] += mangalaGameBoard[chosenPosition];
                mangalaGameBoard[chosenPosition] = 0;
                System.out.println("That's an even number. Great job "+users[1].whoseTurn +", you got your rival's stones");
            }
            printCurrentBoardStatus();
            // if last piece is put in the player's wallet then this player can play one more round
            if (!isGameOver() && chosenPosition == who.userWalletLocation) {
                System.out.println("The last one is in your wallet. Great!! Play again User " + who.whoseTurn);
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
                    //Error message if input is out of borders
                    if (position < 1 || position > 6) {
                        System.out.println("Input must be between 1 and 6:");
                    } else {
                        // Adapting the hole position if the current player is user1
                        // by subtracting 1 from input value to match the index correctly
                        if (who == users[0]) {
                            position = position -1;
                          // Adapting the hole position if the current player is user2
                          // by subtracting the input from 13 to match the index correctly
                        } else {
                            position = 13 - position;
                        }
                        // if entered position has no stones in it then warning message is shown to user
                        // in order to enter input again
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

