
import java.util.*;
import java.util.ArrayList;

public class User {

    private final int HoleLocation;
    private final int HoleSkip;

    public User(int location, int skip) {
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
    public static void main(String[] args) {
        Mangala game = new Mangala();
        ArrayList<User> users = new ArrayList<User>();
        User One = new User((board.length - 2) / 2, board.length - 1);
        users.add(One);
        User Two = new User((board.length - 2) / 2, board.length - 1);

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
