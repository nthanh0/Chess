package ddt.chess.ui;

import ddt.chess.core.Game;
import ddt.chess.util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class ChessUI {
    private final Game game;
    private final JFrame frame;
    private final BoardPanel boardPanel;
    private final GameControlPanel controlPanel;
    private final ImageLoader imageLoader;
    private final MenuBuilder menuBuilder;

    public ChessUI() {
        game = new Game();

        // Initialize ImageLoader with default theme
        String[] themeNames = {"wood", "blue", "green"};
        imageLoader = new ImageLoader("green", themeNames);
        imageLoader.loadAllImages();

        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardPanel = new BoardPanel(game, imageLoader);
        controlPanel = new GameControlPanel(game, boardPanel, imageLoader);

        // Create menu builder and set menu bar
        menuBuilder = new MenuBuilder(game, boardPanel, controlPanel, imageLoader);
        frame.setJMenuBar(menuBuilder.buildMenuBar());

        frame.add(boardPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}