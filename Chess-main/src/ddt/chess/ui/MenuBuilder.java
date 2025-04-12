package ddt.chess.ui;

import ddt.chess.core.Game;
import ddt.chess.util.ImageLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBuilder {
    private final Game game;
    private final BoardPanel boardPanel;
    private final GameControlPanel controlPanel;
    private final ImageLoader imageLoader;

    public MenuBuilder(Game game, BoardPanel boardPanel, GameControlPanel controlPanel, ImageLoader imageLoader) {
        this.game = game;
        this.boardPanel = boardPanel;
        this.controlPanel = controlPanel;
        this.imageLoader = imageLoader;
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);

        JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameItem.addActionListener(e -> resetGame());

        JMenuItem undoMoveItem = new JMenuItem("Undo Move", KeyEvent.VK_U);
        undoMoveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoMoveItem.addActionListener(e -> undoMove());

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameItem);
        gameMenu.add(undoMoveItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        // Theme Menu
        JMenu themeMenu = new JMenu("Theme");
        themeMenu.setMnemonic(KeyEvent.VK_T);

        ButtonGroup themeGroup = new ButtonGroup();
        String[] themes = {"wood", "blue", "green"};

        for (String theme : themes) {
            JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem(theme);
            themeItem.setSelected(imageLoader.getCurrentTheme().equals(theme));
            themeItem.addActionListener(e -> changeTheme(theme));
            themeGroup.add(themeItem);
            themeMenu.add(themeItem);
        }

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem rulesItem = new JMenuItem("Chess Rules", KeyEvent.VK_R);
        rulesItem.addActionListener(e -> showRules());

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAbout());

        helpMenu.add(rulesItem);
        helpMenu.add(aboutItem);

        // Add all menus to menubar
        menuBar.add(gameMenu);
        menuBar.add(themeMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void resetGame() {
        game.getBoard().setUpEmpty();
        game.getBoard().setupPieces();
        game.getHistory().clearHistory();
        controlPanel.updateMoveHistory();
        boardPanel.resetBoard();
    }

    private void undoMove() {
        game.undoLastMove();
        controlPanel.updateMoveHistory();
        boardPanel.resetBoard();
    }

    private void changeTheme(String theme) {
        imageLoader.setCurrentTheme(theme);
        boardPanel.repaint();

        // Update control panel colors to match the theme
        controlPanel.updateThemeColors(theme);
    }

    private void showRules() {
        JOptionPane.showMessageDialog(null,
                "Chess Rules:\n\n" +
                        "• The game is played on an 8x8 board\n" +
                        "• White moves first, then players alternate turns\n" +
                        "• A player must move when it's their turn\n" +
                        "• Pieces capture by moving to a square occupied by an opponent's piece\n" +
                        "• The goal is to checkmate your opponent's king\n" +
                        "• For detailed rules on piece movement and special moves,\n" +
                        "  please refer to the official chess rules.",
                "Chess Rules",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(null,
                "Chess Game\n" +
                        "Version 1.0\n\n" +
                        "A simple chess implementation in Java\n" +
                        "Developed as a learning project",
                "About Chess Game",
                JOptionPane.INFORMATION_MESSAGE);
    }



}