package pg.edu.pl.entities.interfaces;

import pg.edu.pl.entities.Field;

import java.util.List;

public interface IShip {
    int getSize();
    boolean isSunk();
    boolean isPlaced();
    void setSunk(boolean sunk);
    void setPlaced(boolean placed);
    List<Field> getFields();
    void setFields(List<Field> fields);
}
