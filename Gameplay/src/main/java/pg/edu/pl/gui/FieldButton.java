package pg.edu.pl.gui;
import pg.edu.pl.gui.BoardPanel;
import pg.edu.pl.entities.*;
import pg.edu.pl.entities.interfaces.*;
import pg.edu.pl.utils.*;

import javax.swing.*;
import java.awt.*;
public class FieldButton extends JButton {
    private final int row;
    private final int col;
    private final BoardPanel panel;

    public FieldButton(int row, int col, BoardPanel boardPanel) {
        this.row = row;
        this.col = col;
        this.panel = boardPanel;
        setFont(new Font("Arial", Font.BOLD, 18));
        setBackground(Color.WHITE);
        addActionListener(e -> handleClick());
    }

    private void handleClick() {
        IBoard board = panel.getBoard();
        IField f = board.getField(row, col);

        if (panel.isPlacingShip()) {
            boolean ok = board.placeShip(
                new Ship(panel.getShipType().getSize()),
                row, col, panel.isHorizontal()
            );
            if (ok) {
                panel.stopPlacingShip();
                panel.refreshView();
                panel.getGame().onShipPlaced();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot place a ship here");
            }
        } else {

            if (!f.isRevealed()) {
                board.shoot(row, col);
                panel.refreshView();
            } else {
                JOptionPane.showMessageDialog(this, "Field already revealed");
            }
        }
    }

    public void updateVisual(IField f) {
        if (f.isRevealed()) {
            if (f.getShip() != null) {
                setBackground(Color.RED);
                setText("X");
            } else {
                setBackground(Color.BLUE);
                setText("O");
            }
        } else if (f.getShip() != null) {
            setBackground(Color.GRAY);
            setText("");
        } else {
            setBackground(Color.WHITE);
            setText("");
        }
    }
}

