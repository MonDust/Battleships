package pg.edu.pl.entities.interfaces;

import pg.edu.pl.entities.Field;
import pg.edu.pl.utils.ShotResult;

import java.util.List;

public interface IBoard {
    /**
     * Places a ship on the board on specific coordinates.
     * @param ship the ship to be placed
     * @param x x coordinate of the first field of the ship
     * @param y y coordinate of the first field of the ship
     * @param horizontal placement of the ship true if horizontal, false if vertical
     * @return true if placement successful, false otherwise
     */
    boolean placeShip(IShip ship, int x, int y, boolean horizontal);
    /**
     * -- GETTER --
     *  Returns all ships on this board.
     *
     */
    List<IShip> getShips();
    /**
     * Checks if all ships on the board have sunk.
     * @return true if all ships have sunk
     * false if at least one ship on the board is not sunk
     */
    boolean areAllShipsSunk();


    /**
     * Returns a field at specific coordinates.
     * @param x x coordinate of the field
     * @param y y coordinate of the field
     * @return field at x and y
     */
    Field getField(int x, int y);
    /**
     * Shoots at certain coordinates of the board and checks for hit or sunk ship.
     * @param x x coordinate of the field to shoot
     * @param y y coordinate of the field to shoot
     * @return ALREADY_REVEALED - if the field has been revealed before and cannot be shot at again
     * HIT - if the shoot hit a ship
     * MISS - if the field shot at is empty
     * @throws IndexOutOfBoundsException if coords are out of bounds
     */
    ShotResult shoot(int x, int y);

    int getWidth();
    int getHeight();
}
