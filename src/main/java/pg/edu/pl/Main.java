package pg.edu.pl;

import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.game_mechanics.interfaces.IGame;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.startGameCLI();
    }
}
