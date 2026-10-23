package view.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import java.util.Objects;

public class ScrollHeader extends Canvas {

    private final Image scrollSheet;
    private String text = "";
    private double fontSize = 18;

    // Asset dimensions based on your vertical scroll sprite (ui_scroll.png)
    private static final int ASSET_WIDTH = 48;  // Adjust to your actual sprite width
    private static final int ASSET_HEIGHT = 96; // Adjust to your actual sprite height
    private static final int TOP_CAP = 16;      // Height of top wooden roller
    private static final int BOTTOM_CAP = 16;   // Height of bottom wooden roller
    private static final int SCALE = 3;         // Scale factor for crisp pixel art

    public ScrollHeader(String text, int verticalTiles) {
        super(
            ASSET_WIDTH * SCALE,
            (TOP_CAP + BOTTOM_CAP + ((ASSET_HEIGHT - TOP_CAP - BOTTOM_CAP) * verticalTiles)) * SCALE
        );

        this.scrollSheet = new Image(Objects.requireNonNull(ScrollHeader.class.getResourceAsStream("/_assets/panels/ui_scroll.png")));
        this.text = text;
        drawScroll();
    }

    public void setText(String text) {
        this.text = text;
        drawScroll();
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
        drawScroll();
    }

    private void drawScroll() {
        GraphicsContext gc = getGraphicsContext2D();

        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, getWidth(), getHeight());

        int middleSourceHeight = ASSET_HEIGHT - TOP_CAP - BOTTOM_CAP;

        int topHeight = TOP_CAP * SCALE;
        int bottomHeight = BOTTOM_CAP * SCALE;
        int middleTileHeight = middleSourceHeight * SCALE;
        int scrollWidth = ASSET_WIDTH * SCALE;

        // 1. Top Cap (Roller)
        gc.drawImage(
            scrollSheet,
            0, 0,
            ASSET_WIDTH, TOP_CAP,
            0, 0,
            scrollWidth, topHeight
        );

        // 2. Repeating Middle Parchment Slices Vertically
        double y = topHeight;
        double middleBottomLimit = getHeight() - bottomHeight;

        while (y + middleTileHeight <= middleBottomLimit) {
            gc.drawImage(
                scrollSheet,
                0, TOP_CAP,
                ASSET_WIDTH, middleSourceHeight,
                0, y,
                scrollWidth, middleTileHeight
            );
            y += middleTileHeight;
        }

        // 3. Bottom Cap (Roller)
        gc.drawImage(
            scrollSheet,
            0, ASSET_HEIGHT - BOTTOM_CAP,
            ASSET_WIDTH, BOTTOM_CAP,
            0, getHeight() - bottomHeight,
            scrollWidth, bottomHeight
        );

        // 4. Render Multi-Line Text Centered Inside the Scroll
        if (text != null && !text.isEmpty()) {
            gc.setFont(Fonts.pixel(fontSize));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.web("#3b200c")); // Dark brown vintage ink color

            String[] lines = text.split("\n");
            double lineHeight = fontSize * 1.2; // Line spacing adjustment
            double startY = (getHeight() / 2) - (((lines.length - 1) * lineHeight) / 2);

            for (int i = 0; i < lines.length; i++) {
                gc.fillText(lines[i], getWidth() / 2, startY + (i * lineHeight));
            }
        }
    }
}