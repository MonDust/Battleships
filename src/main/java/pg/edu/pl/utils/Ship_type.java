package pg.edu.pl.utils;

/**
 * Enum to represent ship types in the Battleships game with their sizes.
 */
public enum Ship_type {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    DESTROYER(3),
    SUBMARINE(2);

    private final int size;

    /**
     * Constructor for enum.
     * @param size Initialize with the size of the ship.
     */
    Ship_type(int size) {
        this.size = size;
    }

    /**
     * Getter method to get the size of the ship
     * @return int - size
     */
    public int getSize() {
        return size;
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