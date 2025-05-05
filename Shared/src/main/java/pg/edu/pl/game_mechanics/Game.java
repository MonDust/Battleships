package pg.edu.pl.game_mechanics;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.Board;
import pg.edu.pl.entities.Field;
import pg.edu.pl.entities.Player;
import pg.edu.pl.entities.Ship;
import pg.edu.pl.entities.interfaces.IShip;
import pg.edu.pl.utils.Ship_type;
import pg.edu.pl.utils.ShotResult;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IPlayer;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.utils.Player_choice;

import java.util.Scanner;

import static pg.edu.pl.utils.Constants.*;

@Getter
@Setter
public class Game implements IGame {
    private IPlayer player_1;
    private IPlayer player_2;

    private int widthOfTheBoard;
    private int heightOfTheBoard;

    private Player_choice currentPlayer;

    private boolean playerHasWon = false ;

    public Game() {
        this.widthOfTheBoard = DEFAULT_BOARD_SIZE;
        this.heightOfTheBoard = DEFAULT_BOARD_SIZE;
        initializeGame();
    }

    public Game(int widthOfTheBoard, int heightOfTheBoard) {
        if(widthOfTheBoard <= MIN_BOARD_SIZE || heightOfTheBoard <= MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("Board width and height must be greater than " + MIN_BOARD_SIZE);
        }

        this.widthOfTheBoard = widthOfTheBoard;
        this.heightOfTheBoard = heightOfTheBoard;

        initializeGame();
    }

    private void initializeGame() {
        playerHasWon = false;

        IBoard player_1_Board = new Board(widthOfTheBoard, heightOfTheBoard);
        IBoard player_2_Board = new Board(widthOfTheBoard, heightOfTheBoard);

        player_1 = new Player(Player_choice.PLAYER_ONE, player_1_Board);
        player_2 = new Player(Player_choice.PLAYER_TWO, player_2_Board);

        currentPlayer = Player_choice.PLAYER_ONE;
    }

    @Override
    public void restart() {
        initializeGame();
    }

    private void switchPlayer() {
        if(currentPlayer == Player_choice.PLAYER_ONE) {
            currentPlayer = Player_choice.PLAYER_TWO;
        } else {
            currentPlayer = Player_choice.PLAYER_ONE;
        }
    }

    private IBoard getCurrenOpponentBoard() {
        if (currentPlayer == Player_choice.PLAYER_ONE) {
            return player_2.getBoard();
        }
        return player_1.getBoard();
    }

    private IBoard getCurrentPlayerBoard() {
        if (currentPlayer == Player_choice.PLAYER_ONE) {
            return player_1.getBoard();
        }
        return player_2.getBoard();
    }

    @Override
    public boolean doTurn(int x, int y) {
        IBoard targetBoard = getCurrenOpponentBoard();
        ShotResult result = targetBoard.shoot(x,y);

        moveResult(result);

        if(targetBoard.areAllShipsSunk()) {
            win(currentPlayer);
            playerHasWon = true;
            return true;
        }

        // Only switch if valid move
        if(result == ShotResult.HIT || result == ShotResult.MISS) {
            switchPlayer();
            return true;
        }

        // Invalid turn - ALREADY REVEALED
        return false;
    }


    // Implemented in CLI way:
    // TODO - GUI

    private void moveResult(ShotResult result) {
        System.out.println("Result: " + result);
    }

    @Override
    public void win(Player_choice player) {
        System.out.printf(GAME_OVER_MESSAGE + "%n", player.getPlayerName());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exit();
    }

    @Override
    public void exit() {
        System.out.println("Exiting game...");
        System.exit(0);
    }


    // CLI printing

    // TODO - Delete and implement GUI
    /**
     * Prints your own board.
     */
    public void printPlayerView(IBoard board) {
        System.out.println("Your Board:");
        for (int y = 0; y < heightOfTheBoard; y++) {
            for (int x = 0; x < widthOfTheBoard; x++) {
                Field field = board.getField(x, y);
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

    // TODO - Delete and implement GUI
    /**
     * Prints the board as seen by the opponent.
     */
    public void printOpponentView(IBoard board) {
        System.out.println("Opponent View:");
        for (int y = 0; y < heightOfTheBoard; y++) {
            for (int x = 0; x < widthOfTheBoard; x++) {
                Field field = board.getField(x,y);
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


    // TODO - delete and implement GUI (later)
    public void getConsoleInputAndDoTurn(int x, int y) {
        boolean runGame = true;

        while (runGame) {
            printOpponentView(getCurrenOpponentBoard());
            System.out.printf("%s, entering coordinates to shoot ( x y ): (%d, %d)%n", currentPlayer.getPlayerName(), x, y);

            if (x < 0 || x >= widthOfTheBoard || y < 0 || y >= heightOfTheBoard) {
                System.out.println("Coordinates out of bounds. Try again.");
                continue;
            }

            boolean success = doTurn(x, y);

            if (playerHasWon) {
                runGame = false;
                continue;
            }

            if (!success) {
                System.out.println("Field already revealed. Try again.");
            }
        }
    }

    // TODO - GUI
    public void placeShipsOnBoard(int x, int y, boolean horizontal) {
        System.out.printf("%s, placing ships on the board.%n", currentPlayer.getPlayerName());

        IBoard currentPlayerBoard = getCurrentPlayerBoard();

        for (Ship_type type : Ship_type.values()) {
            boolean placed = false;

            while (!placed) {
                System.out.printf("Placing %s (size %d).%n", type.name(), type.getSize());
                System.out.printf("Coordinates: (%d, %d), Horizontal: %b%n", x, y, horizontal);

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


    // this is previous game implementation:
//    public void startGameCLI() {
//        for (Player_choice ignored : Player_choice.values()) {
//            placeShipsOnBoard();
//            switchPlayer();
//        }
//        getConsoleInputAndDoTurn();
//    }
}
