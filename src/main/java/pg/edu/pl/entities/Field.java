package pg.edu.pl.entities;

public class Field {
    private int x;
    private int y;
    private boolean isHit = false;
    private boolean isRevealed = false;
    private Ship ship = null;

    public Field(int x, int y) {
        this.x =x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public Ship getShip() {
        return ship;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
