package pg.edu.pl.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
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

        //Game Panel
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.LIGHT_GRAY);
        gamePanel.setPreferredSize(new Dimension(800, 600));


        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
