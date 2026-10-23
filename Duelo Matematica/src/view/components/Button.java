package view.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import java.util.Objects;

public class Button extends Canvas {

    private final Image spritesheet;

    private boolean hovered;
    private boolean pressed;
    private Runnable onAction;

    private String text = "";
    private double fontSize = 18;

    private static final int STATE_WIDTH = 88;
    private static final int STATE_HEIGHT = 34;
    private static final int LEFT_CAP = 15;
    private static final int RIGHT_CAP = 15;
    private static final int SCALE = 4; // to set the scale of the button, if it feels too small/large

    public Button(int middleTiles) {
        this(new Image(Objects.requireNonNull(Button.class.getResourceAsStream("/_assets/buttons/btn.png"))), middleTiles);
    }

    public Button(Image spritesheet, int middleTiles) {
        super(
                (LEFT_CAP + RIGHT_CAP
                        + (STATE_WIDTH - LEFT_CAP - RIGHT_CAP) * middleTiles)
                        * SCALE,
                STATE_HEIGHT * SCALE
        );

        this.spritesheet = spritesheet;

        setOnMouseEntered(e -> {
            hovered = true;
            drawButton();
        });

        setOnMouseExited(e -> {
            hovered = false;
            drawButton();
        });

        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                pressed = true;
                drawButton();
            }
        });

        setOnMouseReleased(e -> {
            boolean inside = contains(e.getX(), e.getY());

            if (pressed && inside && onAction != null) {
                onAction.run();
            }

            pressed = false;
            drawButton();
        });

        drawButton();
    }

    public void setText(String text) {
        this.text = text;
        drawButton();
    }

    public void setFontSize(double fontSize) {
        if (fontSize <= 0) {
            throw new IllegalArgumentException(
                    "Font size must be greater than zero."
            );
        }

        this.fontSize = fontSize;
        drawButton();
    }

    public void setOnAction(Runnable action) {
        onAction = action;
    }

    private void drawButton() {
        GraphicsContext gc = getGraphicsContext2D();

        gc.setImageSmoothing(false); // this makes the pixel art not blurry when enlarged
        gc.clearRect(0, 0, getWidth(), getHeight());

        int stateIndex = pressed ? 2 : hovered ? 1 : 0;
        int sourceX = stateIndex * STATE_WIDTH;

        int middleSourceWidth =
                STATE_WIDTH - LEFT_CAP - RIGHT_CAP;

        int leftWidth = LEFT_CAP * SCALE;
        int rightWidth = RIGHT_CAP * SCALE;
        int middleTileWidth = middleSourceWidth * SCALE;
        int buttonHeight = STATE_HEIGHT * SCALE;

        // Left cap
        gc.drawImage(
                spritesheet,
                sourceX, 0,
                LEFT_CAP, STATE_HEIGHT,
                0, 0,
                leftWidth, buttonHeight
        );

        // Repeating middle texture
        int x = leftWidth;
        int middleEnd = (int) getWidth() - rightWidth;

        while (x + middleTileWidth <= middleEnd) {
            gc.drawImage(
                    spritesheet,
                    sourceX + LEFT_CAP, 0,
                    middleSourceWidth, STATE_HEIGHT,
                    x, 0,
                    middleTileWidth, buttonHeight
            );

            x += middleTileWidth;
        }

        // Right cap
        gc.drawImage(
                spritesheet,
                sourceX + STATE_WIDTH - RIGHT_CAP, 0,
                RIGHT_CAP, STATE_HEIGHT,
                (int) getWidth() - rightWidth, 0,
                rightWidth, buttonHeight
        );

        // Button text
        if (text != null && !text.isEmpty()) {
            gc.setFont(Fonts.pixel(fontSize));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);

            // Change text color based on button state
            if (pressed) {
                gc.setFill(Color.web("#7B4949"));
            } else {
                gc.setFill(Color.web("#FFF9E3"));
            }

            // Move text down when pressed
            double textY = getHeight() / 2;

            if (pressed) {
                textY += 6;
            }

            gc.fillText(
                    text,
                    getWidth() / 2,
                    textY
            );
        }
    }
}