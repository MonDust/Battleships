package pg.edu.pl.game_mechanics.GUIs;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.gui.BoardPanel;
import pg.edu.pl.gui.FieldButton;
import pg.edu.pl.gui.GamePanel;
import pg.edu.pl.utils.Ship_type;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Class implementing GUI for the game.
 */
@Getter
@Setter
public class GUIGame {
    private IGame game;
    private GamePanel gamePanel;
    private Deque<Ship_type> shipsToPlace = new ArrayDeque<>();

    public GUIGame(IGame game) {
        this.game = game;
    }

    public void startPlacingSequence() {
        shipsToPlace.clear();
        shipsToPlace.addAll(Arrays.asList(Ship_type.values()));
        placeNextShip();
    }


    private void placeNextShip() {
        BoardPanel pb = gamePanel.getPlayerBoard();
        if (shipsToPlace.isEmpty()) {
            registerShootListeners();
        } else {
            Ship_type next = shipsToPlace.poll();
            pb.startPlacingShip(next, pb.isHorizontal());
        }
    }

    public void onShipPlaced() {
        gamePanel.getPlayerBoard().refreshView();
        placeNextShip();
    }

    private void registerShootListeners() {
        BoardPanel op = gamePanel.getOpponentBoard();
        FieldButton[][] grid = op.getButtonGrid();
        for (int r = 0; r < game.getWidthOfTheBoard(); r++) {
            for (int c = 0; c < game.getHeightOfTheBoard(); c++) {
                final int row = r;
                final int col = c;
                FieldButton btn = grid[row][col];
                btn.addActionListener(e -> {
                    boolean valid = game.doTurn(row, col);
                    gamePanel.getPlayerBoard().refreshView();
                    gamePanel.getOpponentBoard().refreshView();
                    if (!valid) {
                        JOptionPane.showMessageDialog(null, "Field already revealed");
                    }
                    if (game.isGameOver()) {
                        JOptionPane.showMessageDialog(
                                null, game.getCurrentPlayer().getPlayerName() + " wins!");
                    }
                });
            }
        }
    }
}
