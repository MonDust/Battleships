package pg.edu.pl.entities.interfaces;

import pg.edu.pl.utils.ShotResult;
import pg.edu.pl.utils.Player_choice;

/**
 * Interface for the player.
 */
public interface IPlayer {
    /**
     * GetId of the player.
     * @return Player choice - either one or two.
     */
    Player_choice getId();

    /**
     * Get the board of the player (the one at which they placed ships)
     * @return board of the player
     */
    IBoard getBoard();

    /**
     * Shoot the board
     * @param x - x coordinate
     * @param y - y coordinate
     * @return - the result of the shot
     */
    ShotResult shoot(int x, int y);

    boolean isPlayerReady();

    void setPlayerReady(boolean player_ready);
}
