package pg.edu.pl.gui;

import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.utils.Ship_type;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final Game game;
    private final GamePanel gamePanel;


    public MainWindow() {
        setTitle("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        setLayout(new BorderLayout());

        //Buttons
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




        add(topPanel, BorderLayout.NORTH);

        game = new Game();
        gamePanel = new GamePanel(
                game.getPlayer_1().getBoard(),
                game.getPlayer_2().getBoard(),
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
}
