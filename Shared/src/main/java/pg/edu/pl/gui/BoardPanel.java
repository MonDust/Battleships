package pg.edu.pl.gui;

import lombok.Getter;
import pg.edu.pl.entities.*;
import pg.edu.pl.entities.interfaces.*;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.utils.*;


import javax.swing.*;
import java.awt.*;

import static pg.edu.pl.utils.Constants.DEFAULT_BOARD_SIZE;

@Getter
public class BoardPanel extends JPanel{
    private final IBoard board;
    private final Game game;
    private FieldButton[][] buttonGrid = new FieldButton[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
    private boolean isPlacingShip = false;
    private Ship_type shipType;
    private boolean isHorizontal = true;

    public BoardPanel(String title, IBoard board, Game game) {
        this.board=board;
        this.game=game;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(DEFAULT_BOARD_SIZE, DEFAULT_BOARD_SIZE));
        for (int row = 0; row < DEFAULT_BOARD_SIZE; row++) {
            for (int col = 0; col < DEFAULT_BOARD_SIZE; col++) {
                FieldButton field = new FieldButton(row, col, this);
                buttonGrid[row][col] = field;
                grid.add(field);
            }
        }
        add(grid, BorderLayout.CENTER);
    }
    public void startPlacingShip(Ship_type type, boolean horizontal) {
        this.isPlacingShip = true;
        this.shipType = type;
        this.isHorizontal = horizontal;
    }

    public void refreshView() {
        for (int r = 0; r < board.getWidth(); r++) {
            for (int c = 0; c < board.getHeight(); c++) {
                FieldButton btn = buttonGrid[r][c];
                var f = board.getField(r, c);
                btn.updateVisual(f);
            }
        }

    }
    public void stopPlacingShip() {
        this.isPlacingShip = false;
    }

     public void toggleOrientation() {
        isHorizontal = !isHorizontal;
    }

}

