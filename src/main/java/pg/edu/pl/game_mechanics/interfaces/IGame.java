package pg.edu.pl.game_mechanics.interfaces;

import pg.edu.pl.utils.Player_choice;

public interface IGame {

    void restart();

    boolean doTurn(int x, int y);

    void win(Player_choice player);
    void exit();
}
