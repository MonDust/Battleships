package pg.edu.pl.entities;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.interfaces.IField;
import pg.edu.pl.entities.interfaces.IShip;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the ship.
 */
@Getter
@Setter
public class Ship implements IShip {
    private final int size;
    /**
     * Is the ship sunk
     */
    private boolean isSunk = false;
    /**
     * Is the ship placed on the board
     */
    private boolean isPlaced = false;
    private List<IField> fields = new ArrayList<>();

    /**
     * Constructor for the class.
     * @param size - size of the ship.
     */
    public Ship(int size) {
        this.size = size;
    }


    @Override
    public boolean isSunk() {
        return isSunk;
    }

    @Override
    public boolean isPlaced() {
        return isPlaced;
    }

    @Override
    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    @Override
    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }


}
