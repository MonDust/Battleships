package pg.edu.pl.entities;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.utils.Constants.DEFAULT_BOARD_SIZE;

public class Board {
    private final int width, height;
    private final Field[][] fields;
    private final List<Ship> ships = new ArrayList<>();

    /**
     * Initialize board and create empty fields.
     */
    public Board() {
        width = DEFAULT_BOARD_SIZE;
        height = DEFAULT_BOARD_SIZE;
        fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y] = new Field(x, y);
            }
        }
    }

    /**
     * Initialize board and create empty fields.
     * @param customWidth width
     * @param customHeight height
     */
    public Board(int customWidth, int customHeight) {
        width = customWidth;
        height = customHeight;
        fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y] = new Field(x, y);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Returns a field at specific coordinates.
     * @param x x coordinate of the field
     * @param y y coordinate of the field
     * @return field at x and y
     */
    public Field getField(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Invalid field coordinates");
        }
        return fields[x][y];
    }

    /**
     * Returns all fields on this board.
     * @return Array of the size of the board (8x8 by default) containing all fields.
     */
    public Field[][] getFields() {
        return fields;
    }

    /**
     * Returns all ships on this board.
     * @return List of all ships
     */
    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Possible shot results.
     */
    public enum ShotResult {
        HIT,
        MISS,
        ALREADY_REVEALED
    }

    /**
     * Places a ship on the board on specific coordinates.
     * @param ship the ship to be placed
     * @param x x coordinate of the first field of the ship
     * @param y y coordinate of the first field of the ship
     * @param horizontal placement of the ship true if horizontal, false if vertical
     * @return true if placement successful, false otherwise
     */
    public boolean placeShip(Ship ship, int x, int y, boolean horizontal) {
        List<Field> shipFields = new ArrayList<>();
        for (int i = 0; i < ship.getSize(); i++) {
            int newX = horizontal ? x + i : x;
            int newY = horizontal ? y : y + i;
            if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                return false;
            }
            Field field = getField(newX, newY);
            if (field.getShip() != null) {
                return false;
            }
            shipFields.add(field);
        }
        for (Field f : shipFields) {
            f.setShip(ship);
        }
        ship.setFields(shipFields);
        ship.setPlaced(true);
        ships.add(ship);
        return true;
    }

    /**
     * Shoots at certain coordinates of the board and checks for hit or sunk ship.
     * @param x x coordinate of the field to shoot
     * @param y y coordinate of the field to shoot
     * @return ALREADY_REVEALED - if the field has been revealed before and cannot be shot at again
     * HIT - if the shoot hit a ship
     * MISS - if the field shot at is empty
     * @throws IndexOutOfBoundsException if coords are out of bounds
     */
    public ShotResult shoot(int x, int y) {
        Field field = getField(x, y);

        if (field.isRevealed()) {
            return ShotResult.ALREADY_REVEALED;
        }

        field.setRevealed(true);

        if (field.getShip() != null) {
            Ship ship = field.getShip();
            field.setHit(true);


            boolean allHit = true;
            for (Field shipField : ship.getFields()) {
                if (!shipField.isRevealed() || !shipField.isHit()) {
                    allHit = false;
                    break;
                }
            }

            if (allHit) {
                ship.setSunk(true);
            }

            return ShotResult.HIT;
        }

        return ShotResult.MISS;
    }

    /**
     * Checks if all ships on the board have sunk.
     * @return true if all ships have sunk
     * false if at least one ship on the board is not sunk
     */
    public boolean areAllShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }
}
