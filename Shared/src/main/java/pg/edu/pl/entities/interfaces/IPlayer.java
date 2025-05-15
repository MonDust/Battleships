package pg.edu.pl.entities.interfaces;

import pg.edu.pl.utils.ShotResult;
import pg.edu.pl.utils.Player_choice;

public interface IPlayer {
    Player_choice getId();
    IBoard getBoard();
    ShotResult shoot(int x, int y);
}
