package pg.edu.pl.entities;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final int size;
    /**
     * Is the ship sunk
     */
    private boolean isSunk = false;
    /**
     * Is the ship placed on the board
     */
    private boolean isPlaced = false;
    private List<Field> fields = new ArrayList<>();

    public Ship(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }


}
