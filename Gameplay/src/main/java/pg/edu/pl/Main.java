package pg.edu.pl;

import pg.edu.pl.game_mechanics.GUIs.CLIPrintingGame;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.gui.MainWindow;

public class Main {
    public static void main(String[] args) {
        IGame game = new Game();
        CLIPrintingGame CLIGame = new CLIPrintingGame(game);
        MainWindow window = new MainWindow();
        CLIGame.startGameCLI();
    }
}
