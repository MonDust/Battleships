package pg.edu.pl.gui;

import lombok.Getter;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.game_mechanics.GUIs.GUIGame;

import javax.swing.*;
import java.awt.*;

@Getter
public class GamePanel extends JPanel{
    private final BoardPanel playerBoard;
    private final BoardPanel opponentBoard;

    public GamePanel(IBoard playerModel, IBoard opponentModel, GUIGame game) {
         setLayout(new GridLayout(1, 2, 50, 0));

         playerBoard = new BoardPanel("Player", playerModel, game);
         opponentBoard = new BoardPanel("Opponent", opponentModel, game);

         add(playerBoard);
         add(opponentBoard);
    }

}
