package pg.edu.pl.entities.interfaces;

import pg.edu.pl.entities.Field;

import java.util.List;

/**
 * Interface for the ship.
 */
public interface IShip {
    /**
     * Get size of the ship.
     * @return size of the ship
     */
    int getSize();
    /**
     * returns whether the ship was sunk.
     * @return is ship sunk (boolean) - true if was, false if wasn't
     */
    boolean isSunk();

    /**
     *
     * @return is ship placed (boolean) - true if was, false if wasn't
     */
    boolean isPlaced();

    /**
     * Set if the ship was sunk
     * @param sunk true if was sunk, false if wasn't
     */
    void setSunk(boolean sunk);

    /**
     * Set if the ship was placed
     * @param placed true if was placed, false if wasn't
     */
    void setPlaced(boolean placed);

    /**
     * Get all the fields of the ship.
     * @return - all the fields on which the ship was placed on.
     */
    List<IField> getFields();

    /**
     * Set all the fields of the ship.
     * @param fields - all the fields on which the ship is to be placed on.
     */
    void setFields(List<IField> fields);
}
