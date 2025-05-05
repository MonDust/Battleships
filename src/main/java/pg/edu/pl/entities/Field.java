package pg.edu.pl.entities;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.entities.interfaces.IShip;

@Getter
@Setter
public class Field {
    private final int x;
    private final int y;
    private boolean isHit = false;
    private boolean isRevealed = false;
    private IShip ship = null;

    public Field(int x, int y) {
        this.x =x;
        this.y=y;
    }
}
