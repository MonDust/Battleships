package pg.edu.pl.gui;

import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.game_mechanics.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{
    private BoardPanel playerBoard;
    private BoardPanel opponentBoard;
    public GamePanel(IBoard playerModel, IBoard opponentModel, Game game) {
         setLayout(new GridLayout(1, 2, 50, 0));

         playerBoard = new BoardPanel("Player", playerModel, game);
         opponentBoard = new BoardPanel("Opponent", opponentModel, game);

         add(playerBoard);
         add(opponentBoard);
    }
    public BoardPanel getPlayerBoard() {
        return playerBoard;
    }

    public BoardPanel getOpponentBoard() {
        return opponentBoard;
    }
}
