package pg.edu.pl.game_mechanics;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.Board;
import pg.edu.pl.entities.Player;
import pg.edu.pl.utils.Player_choice;

import static pg.edu.pl.utils.Constants.DEFAULT_BOARD_SIZE;
import static pg.edu.pl.utils.Constants.MIN_BOARD_SIZE;

@Getter
@Setter
public class Game {
    private Board playerBoard;
    private Board opponentBoard;

    private Player player;
    private Player opponent;

    private int widthOfTheBoard;
    private int heightOfTheBoard;

    private Player_choice currentPlayer;

    public Game() {
        this.widthOfTheBoard = DEFAULT_BOARD_SIZE;
        this.heightOfTheBoard = DEFAULT_BOARD_SIZE;
        initializeGame();
    }

    public Game(int widthOfTheBoard, int heightOfTheBoard) {
        this.widthOfTheBoard = widthOfTheBoard;
        this.heightOfTheBoard = heightOfTheBoard;

        if(widthOfTheBoard <= MIN_BOARD_SIZE || heightOfTheBoard <= MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("Board width and height must be greater than " + MIN_BOARD_SIZE);
        }
        initializeGame();
    }

    private void initializeGame() {
        playerBoard = new Board(widthOfTheBoard, heightOfTheBoard);
        opponentBoard = new Board(widthOfTheBoard, heightOfTheBoard);
        player = new Player(Player_choice.PLAYER_ONE, playerBoard);
        opponent = new Player(Player_choice.PLAYER_TWO, opponentBoard);

        currentPlayer = Player_choice.PLAYER_ONE;
    }

    public void play() {

    }

    public void restart() {
        initializeGame();
    }

    public void win() {
        //
    }

    public void exit() {
        //
    }
}
