package pg.edu.pl.entities;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.interfaces.IField;
import pg.edu.pl.entities.interfaces.IShip;

/**
 * Class representing field on the board.
 * With status hit - if the ship was hit,
 * and status revealed - if the field was revealed, but there was no ship on the field.
 * The ship that was placed on the field is also accessible from it.
 */
@Getter
@Setter
public class Field implements IField {
    private final int x;
    private final int y;
    private boolean isHit = false;
    private boolean isRevealed = false;
    private IShip ship = null;

    /**
     * Constructor for the class.
     * @param x - x coordinate on the board
     * @param y - y coordinate on the board
     */
    public Field(int x, int y) {
        this.x =x;
        this.y=y;
    }
}
