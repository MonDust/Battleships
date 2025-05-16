package pg.edu.pl.game_mechanics.interfaces;

import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IPlayer;
import pg.edu.pl.utils.Player_choice;

/**
 * Interface for the Game.
 */
public interface IGame {

    /**
     * Restart the game.
     */
    void restart();

    /**
     * Player makes a turn.
     * @param x x coordinate
     * @param y y coordinate
     * @return true if turn succeeded, false if not.
     */
    boolean doTurn(int x, int y);

    /**
     * Functions used when player wins
     * @param player - player which won.
     */
    void win(Player_choice player);

    /**
     * Exit the game.
     */
    void exit();

    /**
     * Get width of the board that players are playing.
     * @return width of the board.
     */
    int getWidthOfTheBoard();

    /**
     * Get height of the board that players are playing.
     * @return height of the board.
     */
    int getHeightOfTheBoard();

    /**
     * Check if sb already won.
     * @return true if sb won, false if game is still playing.
     */
    boolean isGameOver();

    /**
     * Get current player that has a turn.
     * @return either Player One or Player Two, depending on whose turn it is.
     */
    Player_choice getCurrentPlayer();

    /**
     * Opponent of the current player board.
     * @return the board of the opponent.
     */
    IBoard getCurrentOpponentBoard();

    /**
     * Current player board.
     * @return the board of the player.
     */
    IBoard getCurrentPlayerBoard();

    /**
     * Get player One
     * @return the player
     */
    IPlayer getPlayer_1();

    /**
     * Get player Two
     * @return the player
     */
    IPlayer getPlayer_2();

    /**
     * Get player
     * @param player Player one or player two
     * @return player (IPlayer)
     */
    IPlayer getPlayer(Player_choice player);

    /**
     * Switch the current player.
     */
    void switchPlayer();

    /**
     * Set the current player
     * @param player Player One or Player Two
     */
    void setCurrentPlayer(Player_choice player);

}
