package pg.edu.pl.entities.interfaces;

/**
 * Interface for the field.
 */
public interface IField {
    /**
     * Function for assigning ships to the field.
     * @param ship - ship to be assigned.
     */
    void setShip(IShip ship);

    /**
     * Function for getting the assigned ship.
     * @return - assigned ship to the field.
     */
    IShip getShip();

    /**
     * Function to set if the field was revealed.
     * @param revealed - true if was revealed, false if wasn't
     */
    void setRevealed(boolean revealed);

    /**
     * Function to see if the field was revealed.
     * @return - true if was revealed, false if wasn't
     */
    boolean isRevealed();

    /**
     * Function to set if the field was hit.
     * @param hit - true if was hit, false if wasn't
     */
    void setHit(boolean hit);

    /**
     * Function to see if the field was hit.
     * @return - true if was hit, false if wasn't
     */
    boolean isHit();
}
