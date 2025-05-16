package pg.edu.pl.game_mechanics;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.Board;
import pg.edu.pl.entities.Player;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IPlayer;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.gui.GamePanel;
import pg.edu.pl.utils.Player_choice;
import pg.edu.pl.utils.Ship_type;
import pg.edu.pl.utils.ShotResult;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pg.edu.pl.utils.Constants.*;

/**
 * Class implementing the game.
 */
@Getter
@Setter
public class Game implements IGame {
    private IPlayer player_1;
    private IPlayer player_2;
    private Player_choice currentPlayer;

    private int widthOfTheBoard;
    private int heightOfTheBoard;

    private boolean game_over;

    private GamePanel gamePanel;
    private Deque<Ship_type> shipsToPlace = new ArrayDeque<>();

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    /**
     * Default constructor for the game.
     */
    public Game(){
        this.widthOfTheBoard = DEFAULT_BOARD_SIZE;
        this.heightOfTheBoard = DEFAULT_BOARD_SIZE;
        initializeGame();
    }

    /**
     * Constructor for the game.
     * @param widthOfTheBoard - width of the board
     * @param heightOfTheBoard - height of the board
     */
    public Game(int widthOfTheBoard, int heightOfTheBoard){
        if (widthOfTheBoard <= MIN_BOARD_SIZE || heightOfTheBoard <= MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("Board width and height must be greater than " + MIN_BOARD_SIZE);
        }

        this.widthOfTheBoard = widthOfTheBoard;
        this.heightOfTheBoard = heightOfTheBoard;

        initializeGame();
    }

    /**
     * Function for initializing the game.
     */
    private void initializeGame() {
        game_over = false;

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

    public IPlayer getPlayer(Player_choice playerChoice) {
        if (playerChoice == Player_choice.PLAYER_ONE) {
            return player_1;
        }
        return player_2;
    }

    @Override
    public void switchPlayer() {
        if(currentPlayer == Player_choice.PLAYER_ONE) {
            currentPlayer = Player_choice.PLAYER_TWO;
        } else {
            currentPlayer = Player_choice.PLAYER_ONE;
        }
    }

    @Override
    public IBoard getCurrentOpponentBoard() {
        if (currentPlayer == Player_choice.PLAYER_ONE) {
            return player_2.getBoard();
        }
        return player_1.getBoard();
    }

    @Override
    public IBoard getCurrentPlayerBoard() {
        if (currentPlayer == Player_choice.PLAYER_ONE) {
            return player_1.getBoard();
        }
        return player_2.getBoard();
    }

    private void moveResult(ShotResult result) {
        logger.log(Level.INFO,"Result: " + result);
    }

    @Override
    public boolean isGameOver() {
        return game_over;
    }

    @Override
    public void exit() {
        logger.log(Level.INFO,"Exiting game...");
        System.exit(0);
    }

    @Override
    public void win(Player_choice player) {
        logger.log(Level.INFO, GAME_OVER_MESSAGE + "%n", player.getPlayerName());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exit();
    }

    @Override
    public boolean doTurn(int x, int y) {
        IBoard targetBoard = getCurrentOpponentBoard();
        ShotResult result = targetBoard.shoot(x,y);

        moveResult(result);

        if(targetBoard.areAllShipsSunk()) {
            win(currentPlayer);
            game_over = true;
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
}
