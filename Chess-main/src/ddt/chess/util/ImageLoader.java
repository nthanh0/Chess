package ddt.chess.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private String currentTheme;
    private String[] themeNames;
    private final Map<String, Color[]> themeColors = new HashMap<>();
    private Map<String, Map<String, ImageIcon>> themePieceImages = new HashMap<>();
    private ImageIcon lightSquareImage;
    private ImageIcon darkSquareImage;
    private ImageIcon selectedSquareImage;
    private ImageIcon checkSquareImage;

    private final int PIECE_SIZE = 40;
    private final int SQUARE_SIZE = 60;

    public ImageLoader(String currentTheme, String[] themeNames) {
        this.currentTheme = currentTheme;
        this.themeNames = themeNames;
        initializeThemeColors();
    }

    // Update this method in ImageLoader class
    public void setCurrentTheme(String theme) {
        if (!currentTheme.equals(theme)) {
            currentTheme = theme;
            loadSquareImages();
        }
    }

    private void initializeThemeColors() {
        themeColors.put("classic", new Color[] {
                new Color(240, 240, 240),  // Light square - white
                new Color(119, 149, 86)    // Dark square - green
        });

        themeColors.put("wood", new Color[] {
                new Color(240, 217, 181),  // Light wood
                new Color(181, 136, 99)    // Dark wood
        });

        themeColors.put("blue", new Color[] {
                new Color(220, 230, 240),  // Light blue
                new Color(93, 131, 171)    // Dark blue
        });

        themeColors.put("green", new Color[] {
                new Color(234, 240, 206),  // Light green
                new Color(119, 149, 86)    // Dark green
        });
    }

    public boolean loadAllImages() {
        try {
            boolean success = loadAllThemePieceImages();
            if (success) {
                loadSquareImages();
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ImageIcon getPieceImage(String pieceCode) {
        Map<String, ImageIcon> currentThemeImages = themePieceImages.get(currentTheme);
        return currentThemeImages.get(pieceCode);
    }

    public ImageIcon getLightSquareImage() {
        return lightSquareImage;
    }

    public ImageIcon getDarkSquareImage() {
        return darkSquareImage;
    }

    public ImageIcon getSelectedSquareImage() {
        return selectedSquareImage;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    private boolean loadAllThemePieceImages() {
        try {
            String[] pieceTypes = {"pawn", "rook", "knight", "bishop", "queen", "king"};
            String[] colors = {"white", "black"};
            String[] pieceLetters = {"P", "R", "N", "B", "Q", "K"};

            for (String theme : themeNames) {
                Map<String, ImageIcon> themeImages = new HashMap<>();

                for (int i = 0; i < pieceTypes.length; i++) {
                    // White pieces
                    BufferedImage whiteImage = loadImageFromFilesOrResources(
                            theme + "_" + colors[0] + "_" + pieceTypes[i] + ".png");

                    if (whiteImage == null) {
                        whiteImage = loadImageFromFilesOrResources(colors[0] + "_" + pieceTypes[i] + ".png");
                    }

                    if (whiteImage != null) {
                        themeImages.put(pieceLetters[i], resizeIcon(new ImageIcon(whiteImage)));
                    } else {
                        themeImages.put(pieceLetters[i], createFallbackPieceIcon(pieceLetters[i], true));
                    }

                    // Black pieces
                    BufferedImage blackImage = loadImageFromFilesOrResources(
                            theme + "_" + colors[1] + "_" + pieceTypes[i] + ".png");

                    if (blackImage == null) {
                        blackImage = loadImageFromFilesOrResources(colors[1] + "_" + pieceTypes[i] + ".png");
                    }

                    if (blackImage != null) {
                        themeImages.put(pieceLetters[i].toLowerCase(), resizeIcon(new ImageIcon(blackImage)));
                    } else {
                        themeImages.put(pieceLetters[i].toLowerCase(), createFallbackPieceIcon(pieceLetters[i], false));
                    }
                }

                themePieceImages.put(theme, themeImages);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private ImageIcon createCheckSquareIcon() {
        // Tạo biểu tượng ô khi vua bị chiếu (đỏ để cảnh báo)
        BufferedImage img = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // Get theme colors
        Color[] colors = themeColors.get(currentTheme);
        if (colors == null) {
            colors = themeColors.get("classic"); // Default to classic if theme not found
        }

        // Sử dụng màu ô sáng với viền đỏ
        g2d.setColor(colors[0]);
        g2d.fillRect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

        // Thêm viền đỏ
        g2d.setColor(new Color(255, 0, 0, 220)); // Màu đỏ với độ trong suốt
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(2, 2, SQUARE_SIZE - 4, SQUARE_SIZE - 4);

        g2d.dispose();
        return new ImageIcon(img);
    }

    public ImageIcon getCheckSquareImage() {
        return checkSquareImage;
    }

    private void loadSquareImages() {
        try {
            BufferedImage lightImg = null;
            BufferedImage darkImg = null;
            BufferedImage selectedImg = null;

            lightImg = loadImageFromFilesOrResources(currentTheme + "_light_square.png");
            darkImg = loadImageFromFilesOrResources(currentTheme + "_dark_square.png");
            selectedImg = loadImageFromFilesOrResources(currentTheme + "_selected_square.png");

            if (lightImg != null && darkImg != null) {
                lightSquareImage = new ImageIcon(lightImg);
                darkSquareImage = new ImageIcon(darkImg);

                if (selectedImg != null) {
                    selectedSquareImage = new ImageIcon(selectedImg);
                } else {
                    selectedSquareImage = createSelectedSquareIcon();
                }
            } else {
                lightSquareImage = createSquareIcon(true);
                darkSquareImage = createSquareIcon(false);
                selectedSquareImage = createSelectedSquareIcon();
            }
        } catch (Exception e) {
            e.printStackTrace();
            lightSquareImage = createSquareIcon(true);
            darkSquareImage = createSquareIcon(false);
            selectedSquareImage = createSelectedSquareIcon();
        }
    }

    // Load image from file or resources
    private BufferedImage loadImageFromFilesOrResources(String fileName) {
        BufferedImage image = null;

        // Try loading from file paths
        String[] paths = {".", "images", "src/main/resources", "src/ddt/resources", "resources", "assets", "sprites", "tiles"};
        for (String path : paths) {
            try {
                File file = new File(path, fileName);
                if (file.exists()) {
                    image = ImageIO.read(file);
                    System.out.println("Image loaded from: " + file.getPath());
                    return image;
                }
            } catch (Exception e) {
                // Continue trying other paths
            }
        }

        // Try loading from classpath resources
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            if (is != null) {
                image = ImageIO.read(is);
                System.out.println("Image loaded from classpath: " + fileName);
                return image;
            }
        } catch (Exception e) {
            // Not found in classpath
        }

        // Try loading from other classpath locations
        String[] resourcePaths = {"images/", "resources/", "assets/", "sprites/"};
        for (String path : resourcePaths) {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream(path + fileName);
                if (is != null) {
                    image = ImageIO.read(is);
                    System.out.println("Image loaded from classpath: " + path + fileName);
                    return image;
                }
            } catch (Exception e) {
                // Continue trying other paths
            }
        }

        System.err.println("Could not load image: " + fileName);
        return null;
    }


    private ImageIcon createFallbackPieceIcon(String pieceCode, boolean isWhite) {
        // Create a simple colored shape icon as fallback for missing images
        BufferedImage img = new BufferedImage(PIECE_SIZE, PIECE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Set colors based on white/black piece
        g2d.setColor(isWhite ? Color.WHITE : Color.BLACK);
        g2d.fillOval(5, 5, PIECE_SIZE - 10, PIECE_SIZE - 10);

        g2d.setColor(isWhite ? Color.BLACK : Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(5, 5, PIECE_SIZE - 10, PIECE_SIZE - 10);

        // Add letter inside
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String text = pieceCode.toUpperCase();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        g2d.drawString(text,
                (PIECE_SIZE - textWidth) / 2,
                (PIECE_SIZE - textHeight) / 2 + fm.getAscent());

        g2d.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon resizeIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(PIECE_SIZE, PIECE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private ImageIcon createSquareIcon(boolean isLight) {
        // Create a simple colored square as fallback for missing images
        BufferedImage img = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // Get theme colors
        Color[] colors = themeColors.get(currentTheme);
        if (colors == null) {
            colors = themeColors.get("classic"); // Default to classic if theme not found
        }

        g2d.setColor(isLight ? colors[0] : colors[1]);
        g2d.fillRect(0, 0, SQUARE_SIZE, SQUARE_SIZE);
        g2d.dispose();

        return new ImageIcon(img);
    }

    private ImageIcon createSelectedSquareIcon() {
        // Create a selected square icon (highlighted with border)
        BufferedImage img = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // Get theme colors
        Color[] colors = themeColors.get(currentTheme);
        if (colors == null) {
            colors = themeColors.get("classic"); // Default to classic if theme not found
        }

        // Use light square color with a highlight
        g2d.setColor(colors[0]);
        g2d.fillRect(0, 0, SQUARE_SIZE, SQUARE_SIZE);

        // Add highlight border
        g2d.setColor(new Color(255, 215, 0, 200)); // Gold color with some transparency
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(2, 2, SQUARE_SIZE - 4, SQUARE_SIZE - 4);

        g2d.dispose();
        return new ImageIcon(img);
    }
}