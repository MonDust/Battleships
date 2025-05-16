package pg.edu.pl.gui;

import pg.edu.pl.game_mechanics.GUIs.GUIGame;
import pg.edu.pl.game_mechanics.Game;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final GamePanel gamePanel;


    public MainWindow() {
        setTitle("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        setLayout(new BorderLayout());

        //Buttons
        JPanel topPanel = getTopPanel();

        add(topPanel, BorderLayout.NORTH);

        GUIGame game = new GUIGame(new Game());
        gamePanel = new GamePanel(
                game.getGame().getPlayer_1().getBoard(),
                game.getGame().getPlayer_2().getBoard(),
                game
        );
        add(gamePanel, BorderLayout.CENTER);
        game.setGamePanel(gamePanel);
        pack();


        JPanel controls = new JPanel();
        JButton rotateButton = new JButton("Orientation: V");
        rotateButton.addActionListener(e -> {
            BoardPanel pb = gamePanel.getPlayerBoard();
            pb.toggleOrientation();
            rotateButton.setText("Orientation: " + (pb.isHorizontal() ? "V" : "H"));
        });
        controls.add(rotateButton);

        add(controls, BorderLayout.SOUTH);

        setVisible(true);


        game.startPlacingSequence();


        setVisible(true);
    }

    private static JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton connectButton = new JButton("Connect");
        JButton optionsButton = new JButton("Options");
        JButton exitButton = new JButton("Exit");


        Dimension buttonSize = new Dimension(150, 40);
        connectButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        //Exit
        exitButton.addActionListener(e -> System.exit(0));


        topPanel.add(connectButton);
        topPanel.add(optionsButton);
        topPanel.add(exitButton);
        return topPanel;
    }
}
