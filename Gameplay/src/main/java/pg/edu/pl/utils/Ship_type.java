package pg.edu.pl.utils;

import lombok.Getter;

/**
 * Enum to represent ship types in the Battleships game with their sizes.
 */
@Getter
public enum Ship_type {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    DESTROYER(3),
    SUBMARINE(2);

    /**
     * -- GETTER --
     *  Getter method to get the size of the ship
     */
    private final int size;

    /**
     * Constructor for enum.
     * @param size Initialize with the size of the ship.
     */
    Ship_type(int size) {
        this.size = size;
    }

    /**
     * to String.
     * @return String
     */
    @Override
    public String toString() {
        return name() + " (" + size + " cells)";
    }
}