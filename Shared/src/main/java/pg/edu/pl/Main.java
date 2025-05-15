package pg.edu.pl;

import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.gui.MainWindow;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        MainWindow window = new MainWindow();
        game.startGameCLI();
    }
}
