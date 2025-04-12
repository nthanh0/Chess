package ddt.chess.ui;

import ddt.chess.core.Board;
import ddt.chess.core.Game;
import ddt.chess.util.ImageLoader;
import ddt.chess.util.MoveHistory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControlPanel extends JPanel {
    private final Game game;
    private final BoardPanel boardPanel;
    private final JTextArea moveHistoryArea;
    private final JButton resetButton;
    private final ImageLoader imageLoader;

    // Define theme colors for the control panel
    private final Color PANEL_BACKGROUND = new Color(232, 220, 202);
    private final Color BUTTON_BACKGROUND = new Color(181, 136, 99);
    private final Color BUTTON_TEXT = Color.WHITE;
    private final Color HISTORY_BACKGROUND = new Color(253, 245, 230);

    public GameControlPanel(Game game, BoardPanel boardPanel, ImageLoader imageLoader) {
        this.game = game;
        this.boardPanel = boardPanel;
        this.imageLoader = imageLoader;

        // Set panel properties
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(new Dimension(200, 480));
        setBackground(PANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(150, 120, 90)),
                new EmptyBorder(10, 10, 10, 10)));

        // Create title label with styled appearance
        JLabel titleLabel = new JLabel("Game Controls", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(new Color(90, 60, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Create move history display with styled appearance
        JPanel historyPanel = new JPanel(new BorderLayout());

        historyPanel.setBackground(PANEL_BACKGROUND);

        moveHistoryArea = new JTextArea(20, 15);
        moveHistoryArea.setEditable(false);
        moveHistoryArea.setBackground(HISTORY_BACKGROUND);
        moveHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        moveHistoryArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(150, 120, 90), 1));

        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Create control buttons with styled appearance
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 5, 8));
        buttonPanel.setBackground(PANEL_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create styled buttons
        resetButton = createStyledButton("Reset Game");

        // Create theme selector with styled appearance
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        themePanel.setBackground(PANEL_BACKGROUND);


        resetButton.addActionListener(e -> {
            game.getBoard().setUpEmpty();
            // Create a completely new game state
            game.getBoard().setupPieces();
            // Clear the history properly
            game.getHistory().clearHistory();
            updateMoveHistory();
            boardPanel.resetBoard();
        });


        buttonPanel.add(themePanel);
        buttonPanel.add(resetButton);

        add(titleLabel, BorderLayout.NORTH);
        add(historyPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial update
        updateMoveHistory();
    }

    /**
     * Creates a styled button with consistent appearance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 90, 60), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 160, 120));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
            }
        });

        return button;
    }

    public void updateMoveHistory() {
        MoveHistory history = game.getHistory();
        moveHistoryArea.setText(history.getHistoryString(game.getBoard()));
    }

    /**
     * Updates the control panel theme colors to match the current game theme
     */
    public void updateThemeColors(String theme) {
        Color panelBg, buttonBg, buttonText, historyBg;

        // Set color scheme based on theme
        switch (theme) {
            case "blue":
                panelBg = new Color(220, 230, 240);
                buttonBg = new Color(93, 131, 171);
                buttonText = Color.WHITE;
                historyBg = new Color(235, 240, 245);
                break;
            case "green":
                panelBg = new Color(234, 240, 206);
                buttonBg = new Color(119, 149, 86);
                buttonText = Color.WHITE;
                historyBg = new Color(245, 250, 230);
                break;
            case "wood":
            default:
                panelBg = new Color(232, 220, 202);
                buttonBg = new Color(181, 136, 99);
                buttonText = Color.WHITE;
                historyBg = new Color(253, 245, 230);
                break;
        }

        // Apply the new colors
        setBackground(panelBg);
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(panelBg);
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JPanel) {
                        subComp.setBackground(panelBg);
                    }
                }
            }
        }

        // Update button colors
        resetButton.setBackground(buttonBg);
        resetButton.setForeground(buttonText);

        // Update move history area
        moveHistoryArea.setBackground(historyBg);

        repaint();
    }
}