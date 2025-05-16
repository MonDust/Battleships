package pg.edu.pl.game_mechanics.GUIs;

import pg.edu.pl.entities.Ship;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IField;
import pg.edu.pl.entities.interfaces.IShip;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.utils.Player_choice;
import pg.edu.pl.utils.Ship_type;

import java.util.Scanner;

/**
 * Class for CLI printing the game.
 * (CLI GUI)
 */
public class CLIPrintingGame {
    private final IGame game;

    public CLIPrintingGame(IGame game) {
        this.game = game;
    }

    /**
     * Prints your own board.
     */
    public void printPlayerView(IBoard board) {
        System.out.println("Your Board:");
        for (int y = 0; y < game.getHeightOfTheBoard(); y++) {
            for (int x = 0; x < game.getWidthOfTheBoard(); x++) {
                IField field = board.getField(x, y);
                if (field.getShip() != null) {
                    if (field.isHit()) {
                        System.out.print("X "); // hit ship
                    } else {
                        System.out.print("S "); // ship not hit
                    }
                } else {
                    if (field.isRevealed()) {
                        System.out.print("O "); // revealed empty (miss)
                    } else {
                        System.out.print(". "); // unrevealed empty
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints the board as seen by the opponent.
     */
    public void printOpponentView(IBoard board) {
        System.out.println("Opponent View:");
        for (int y = 0; y < game.getHeightOfTheBoard(); y++) {
            for (int x = 0; x < game.getWidthOfTheBoard(); x++) {
                IField field = board.getField(x, y);
                if (!field.isRevealed()) {
                    System.out.print(". "); // unrevealed
                } else if (field.getShip() != null && field.getShip().isSunk()) {
                    System.out.print("# "); // sunk
                } else if (field.getShip() != null && field.isHit()) {
                    System.out.print("X "); // hit
                } else {
                    System.out.print("O "); // miss
                }
            }
            System.out.println();
        }
    }

    public void getConsoleInputAndDoTurn(int x, int y) {
        boolean runGame = true;

        while (runGame) {
            printOpponentView(game.getCurrentOpponentBoard());
            System.out.printf("%s, entering coordinates to shoot ( x y ): (%d, %d)%n", game.getCurrentPlayer().getPlayerName(), x, y);

            if (x < 0 || x >= game.getWidthOfTheBoard() || y < 0 || y >= game.getHeightOfTheBoard()) {
                System.out.println("Coordinates out of bounds. Try again.");
                continue;
            }

            boolean success = game.doTurn(x, y);

            if (game.isGameOver()) {
                runGame = false;
                continue;
            }

            if (!success) {
                System.out.println("Field already revealed. Try again.");
            }
        }
    }

    /**
     * Prints the board as seen by the opponent.
     */
    public String getOpponentView(IBoard board) {
        StringBuilder sb = new StringBuilder();
        sb.append("Opponent board:\n");
        for (int y = 0; y < game.getHeightOfTheBoard(); y++) {
            for (int x = 0; x < game.getWidthOfTheBoard(); x++) {
                IField field = board.getField(x,y);
                if (!field.isRevealed()) {
                    sb.append(". "); // unrevealed
                } else if (field.getShip() != null && field.getShip().isSunk()) {
                    sb.append("# "); // sunk
                } else if (field.getShip() != null && field.isHit()) {
                    sb.append("X "); // hit
                } else {
                    sb.append("O "); // miss
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Prints your own board.
     */
    public String getPlayerView(IBoard board) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your board:\n");
        for (int y = 0; y < game.getHeightOfTheBoard(); y++) {
            for (int x = 0; x < game.getWidthOfTheBoard(); x++) {
                IField field = board.getField(x, y);
                if (field.getShip() != null) {
                    if (field.isHit()) {
                        sb.append("X "); // hit ship
                    } else {
                        sb.append("S "); // ship not hit
                    }
                } else {
                    if (field.isRevealed()) {
                        sb.append("O "); // revealed empty (miss)
                    } else {
                        sb.append(". "); // unrevealed empty
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void placeShipsOnBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%s, place your ships on the board.%n", game.getCurrentPlayer().getPlayerName());

        IBoard currentPlayerBoard = game.getCurrentPlayerBoard();

        for (Ship_type type : Ship_type.values()) {
            boolean placed = false;

            while (!placed) {
                System.out.printf("Placing %s (size %d).%n", type.name(), type.getSize());
                System.out.print("Enter starting coordinates (x y): ");

                int x, y;
                try {
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter two integers.");
                    scanner.nextLine();
                    continue;
                }

                System.out.print("Horizontal? (true/false): ");
                boolean horizontal;
                try {
                    horizontal = scanner.nextBoolean();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter true or false.");
                    scanner.nextLine();
                    continue;
                }

                IShip ship = new Ship(type.getSize());
                boolean success = currentPlayerBoard.placeShip(ship, x, y, horizontal);

                if (success) {
                    System.out.println("Ship placed successfully.");
                    placed = true;
                } else {
                    System.out.println("Could not place ship at that location. Try again.");
                }

                // Show board
                printPlayerView(currentPlayerBoard);
            }
        }
    }

    private void getConsoleInputAndDoTurn() {
        Scanner scanner = new Scanner(System.in);

        boolean runGame = true;

        while (runGame) {
            printOpponentView(game.getCurrentOpponentBoard());
            System.out.printf("%s, enter coordinates to shoot ( x y ): ", game.getCurrentPlayer().getPlayerName());

            int x, y;
            try {
                x = scanner.nextInt();
                y = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter two integers.");
                scanner.nextLine();
                continue;
            }

            if (x < 0 || x >= game.getWidthOfTheBoard() || y < 0 || y >= game.getHeightOfTheBoard()) {
                System.out.println("Coordinates out of bounds. Try again.");
                continue;
            }

            boolean success = game.doTurn(x, y);

            if (game.isGameOver()) {
                runGame = false;
                continue;
            }

            if (!success) {
                System.out.println("Field already revealed. Try again.");
            }
        }
    }

    public void startGameCLI() {
        for (Player_choice ignored : Player_choice.values()) {
            placeShipsOnBoard();
            game.switchPlayer();
        }
        getConsoleInputAndDoTurn();
    }

}
