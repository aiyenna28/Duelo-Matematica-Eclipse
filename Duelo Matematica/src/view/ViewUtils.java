package view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.io.InputStream;

public final class ViewUtils {

    // Default safely to "System" until loadCustomFont() overwrites it with the true family name
    private static String customFontFamily = "System";

    // ------------------------------------------------------------------
    // 9-slice tuning values. All insets are in SOURCE image pixels.
    // Adjust these by looking at your PNGs.
    // ------------------------------------------------------------------
    // btn.png (3 frames: normal / hover / pressed)
    private static final double BTN_CAP_X = 8, BTN_CAP_Y = 3, BTN_SCALE = 3;

    // ui_scroll.png (title scroll)
    private static final double SCROLL_CAP_X = 20, SCROLL_CAP_TOP = 8, SCROLL_CAP_BOTTOM = 14, SCROLL_SCALE = 3;

    // ui_popup_scroll.png (12 animation frames)
    private static final double POPUP_CAP_X = 8, POPUP_CAP_TOP = 8, POPUP_CAP_BOTTOM = 14, POPUP_SCALE = 3;

    private ViewUtils() { }

    // ==========================================
    // FONT
    // ==========================================
    public static void loadCustomFont() {
        try {
            InputStream stream = ViewUtils.class.getResourceAsStream("/_assets/fonts/Duelo.ttf");
            if (stream != null) {
                Font font = Font.loadFont(stream, 12);
                if (font != null) {
                    customFontFamily = font.getFamily();
                    System.out.println(">>> SUCCESS: JavaFX successfully parsed font family name: " + customFontFamily);
                } else {
                    System.err.println(">>> CRITICAL ERROR: Stream found, but Font.loadFont() returned NULL! JavaFX rejected the TTF file format.");
                }
            } else {
                System.err.println(">>> CRITICAL ERROR: ClassLoader returned NULL for /_assets/fonts/Duelo.ttf! The file path or folder structure is mismatched.");
            }
        } catch (Exception e) {
            System.err.println(">>> EXCEPTION DURING FONT LOADING:");
            e.printStackTrace();
        }
    }

    public static Font getCustomFont(double size) {
        return Font.font(customFontFamily, size);
    }

    // ==========================================
    // LABELS
    // ==========================================
    public static Label createMainTitleLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        headerLabel.setFont(Font.font(customFontFamily, 26));

        headerLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
        headerLabel.setMinHeight(Region.USE_COMPUTED_SIZE);
        headerLabel.setMaxHeight(Region.USE_COMPUTED_SIZE);

        headerLabel.setStyle(
            "-fx-font-family: '" + customFontFamily + "'; " +
            "-fx-font-weight: bold;" +
            "-fx-padding: 20px 40px;" +
            "-fx-background-color: transparent;" +
            "-fx-alignment: center;" +
            "-fx-text-alignment: center;" +
            "-fx-line-spacing: 5px;"
        );

        return headerLabel;
    }

    public static Label createPopupHeaderLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setFont(Font.font(customFontFamily, 20));

        headerLabel.setStyle(
            "-fx-font-family: '" + customFontFamily + "'; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12px 12px; " +
            "-fx-alignment: center; " +
            "-fx-text-alignment: center;" +
            "-fx-background-color: transparent;"
        );

        return headerLabel;
    }

    public static Label createSubHeaderLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setFont(Font.font(customFontFamily, 14));
        headerLabel.setStyle("-fx-font-family: '" + customFontFamily + "'; -fx-border-color: transparent; -fx-background-color: transparent;");
        return headerLabel;
    }

    public static Label createBodyTextLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setFont(Font.font(customFontFamily, 10));
        headerLabel.setStyle("-fx-font-family: '" + customFontFamily + "'; -fx-border-color: transparent; -fx-background-color: transparent;");
        return headerLabel;
    }

    public static Button createCloseButton(String text) {
        Button closeButton = new Button(text);
        closeButton.setStyle(
            "-fx-font-family: '" + customFontFamily + "'; " +
            "-fx-background-color: transparent; -fx-font-weight: bold;" +
            "-fx-font-size: 14px; -fx-cursor: hand;"
        );
        return closeButton;
    }

    // ==========================================
    // SCROLL HEADER (9-slice)
    // ==========================================
    public static StackPane createSpriteHeader(String imagePath, String text) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Scroll Image NOT FOUND at path: /_assets" + imagePath);
            return new StackPane(new Label(text));
        }
        Image img = new Image(stream);

        Label label = new Label(text);
        label.setFont(getCustomFont(18));
        label.setTextFill(Color.web("#3b200c"));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setLineSpacing(8);

        // Measure the REAL text block (width AND height), then size the scroll around it
        Text m = new Text(text);
        m.setFont(getCustomFont(18));
        m.setLineSpacing(8);
        double textW = m.getLayoutBounds().getWidth();
        double textH = m.getLayoutBounds().getHeight();

        NineSlice scroll = new NineSlice(img, 1, SCROLL_CAP_X, SCROLL_CAP_X,
                SCROLL_CAP_TOP, SCROLL_CAP_BOTTOM, SCROLL_SCALE, true);
        double w = textW + 2 * SCROLL_CAP_X * SCROLL_SCALE + 40;
        double h = textH + (SCROLL_CAP_TOP + SCROLL_CAP_BOTTOM) * SCROLL_SCALE + 100;
        scroll.setPrefSize(w, h);
        scroll.setMinSize(w, h);
        scroll.setMaxSize(w, h);

        StackPane stack = new StackPane(scroll, label);
        stack.setAlignment(Pos.CENTER);
        return stack;
    }

    // ==========================================
    // SPRITE BUTTONS
    // ==========================================

    /** Icon-style button with no text (fixed-size, uses the 3-frame sheet as-is). */
    public static Button createSpriteButton(String imagePath, Runnable onClickAction) {
        return createSpriteButton(imagePath, -1, -1, onClickAction);
    }

    /** Text button. Width grows with the text using 3-slice, so the ends never warp. */
    public static Button createSpriteButton(String imagePath, String text, Runnable onClickAction) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Image NOT FOUND at path: /_assets" + imagePath);
            return new Button("Missing Image");
        }
        Image sheet = new Image(stream);

        Text m = new Text(text);
        m.setFont(getCustomFont(18));
        double w = m.getLayoutBounds().getWidth() + 2 * BTN_CAP_X * BTN_SCALE + 30;
        double h = sheet.getHeight() * BTN_SCALE; // natural height, just scaled by an integer

        NineSlice bg = new NineSlice(sheet, 3, BTN_CAP_X, BTN_CAP_X, BTN_CAP_Y, BTN_CAP_Y, BTN_SCALE, false);
        bg.setPrefSize(w, h);

        Label label = new Label(text);
        label.setFont(getCustomFont(18));
        label.setTextFill(Color.web("#6b3e26"));
        label.setTranslateY(-4);

        StackPane graphic = new StackPane(bg, label);

        Button button = new Button();
        button.setGraphic(graphic);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0; -fx-border-width: 0; -fx-cursor: hand;");
        button.setPrefSize(w, h);
        button.setMinSize(w, h);
        button.setMaxSize(w, h);

        button.hoverProperty().addListener((o, was, hov) -> {
            if (!button.isArmed()) bg.setFrame(hov ? 1 : 0);
        });
        button.armedProperty().addListener((o, was, armed) -> {
            bg.setFrame(armed ? 2 : (button.isHover() ? 1 : 0));
            label.setTranslateY(armed ? -2 : -4);
        });

        if (onClickAction != null) {
            button.setOnAction(e -> onClickAction.run());
        }
        return button;
    }

    /** Fixed-size sprite button (no 9-slice). Used by the no-text overload above. */
    public static Button createSpriteButton(String imagePath, double width, double height, Runnable onClickAction) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Image NOT FOUND at path: /_assets" + imagePath);
            return new Button("Missing Image");
        }

        Image spriteSheet = new Image(stream);
        double frameWidth = spriteSheet.getWidth() / 3.0;
        double frameHeight = spriteSheet.getHeight();

        ImageView imageView = new ImageView(spriteSheet);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        Label textLabel = new Label();
        textLabel.setFont(getCustomFont(14));
        textLabel.setTextFill(Color.web("#6b3e26"));
        textLabel.setPadding(Insets.EMPTY);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setTranslateY(-6);

        StackPane graphicStack = new StackPane(imageView, textLabel);
        StackPane.setAlignment(imageView, Pos.CENTER);
        StackPane.setAlignment(textLabel, Pos.CENTER);

        Button button = new Button();
        button.setGraphic(graphicStack);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0; -fx-background-insets: 0; -fx-border-width: 0;");

        button.textProperty().addListener((obs, oldV, newV) -> textLabel.setText(newV));

        if (width > 0 && height > 0) {
            button.setPrefSize(width, height);
            button.setMinSize(width, height);
            button.setMaxSize(width, height);

            graphicStack.prefWidthProperty().bind(button.widthProperty());
            graphicStack.prefHeightProperty().bind(button.heightProperty());
            graphicStack.minWidthProperty().bind(button.widthProperty());
            graphicStack.minHeightProperty().bind(button.heightProperty());
            graphicStack.maxWidthProperty().bind(button.widthProperty());
            graphicStack.maxHeightProperty().bind(button.heightProperty());

            imageView.fitWidthProperty().bind(graphicStack.widthProperty());
            imageView.fitHeightProperty().bind(graphicStack.heightProperty());

        } else {
            // No size given: show one frame at its natural size
            imageView.setFitWidth(frameWidth);
            imageView.setFitHeight(frameHeight);
        }

        button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (button.isArmed()) return;
            imageView.setViewport(new Rectangle2D(isNowHovered ? frameWidth : 0, 0, frameWidth, frameHeight));
        });

        button.armedProperty().addListener((obs, wasArmed, isNowArmed) -> {
            if (isNowArmed) {
                imageView.setViewport(new Rectangle2D(frameWidth * 2, 0, frameWidth, frameHeight));
                textLabel.setTranslateY(-5);
            } else {
                imageView.setViewport(new Rectangle2D(button.isHover() ? frameWidth : 0, 0, frameWidth, frameHeight));
                textLabel.setTranslateY(-6);
            }
        });

        if (onClickAction != null) {
            button.setOnAction(e -> onClickAction.run());
        }

        return button;
    }

    public static Button createTwoFrameSpriteButton(String imagePath, double size, Runnable onClickAction) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Image NOT FOUND at path: /_assets" + imagePath);
            return new Button("Missing Image");
        }

        Image spriteSheet = new Image(stream);
        double frameWidth = spriteSheet.getWidth() / 2.0;
        double frameHeight = spriteSheet.getHeight();

        ImageView imageView = new ImageView(spriteSheet);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        Button button = new Button();
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0; -fx-background-insets: 0; -fx-border-width: 0;");

        button.setPrefSize(size, size);
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        imageView.fitWidthProperty().bind(button.widthProperty());
        imageView.fitHeightProperty().bind(button.heightProperty());

        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(255, 235, 59, 0.9));
        glow.setRadius(20);
        glow.setSpread(0.3);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (button.isArmed()) return;
            imageView.setEffect(isNowHovered ? glow : null);
        });

        button.armedProperty().addListener((obs, wasArmed, isNowArmed) -> {
            if (isNowArmed) {
                imageView.setViewport(new Rectangle2D(frameWidth, 0, frameWidth, frameHeight));
            } else {
                imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
                if (button.isHover()) {
                    imageView.setEffect(glow);
                }
            }
        });

        if (onClickAction != null) {
            button.setOnAction(e -> onClickAction.run());
        }

        return button;
    }

    // ==========================================
    // DROPDOWN POPUP (9-slice, 12-frame animation)
    // ==========================================
    public static void animateDropdown(Node view, StackPane rootContainer, Node... backgroundNodes) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets/panels/ui_popup_scroll.png");
        if (stream == null) {
            System.err.println("ERROR: Popup image NOT FOUND at path: /_assets/panels/ui_popup_scroll.png");
            rootContainer.getChildren().setAll(backgroundNodes);
            rootContainer.getChildren().add(view);
            return;
        }
        Image woodImg = new Image(stream);
        final int frameCount = 12;

        // The panel is drawn around the view's size instead of being stretched to it
        NineSlice panel = new NineSlice(woodImg, frameCount,
                POPUP_CAP_X, POPUP_CAP_X, POPUP_CAP_TOP, POPUP_CAP_BOTTOM, POPUP_SCALE, true);
        panel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // fill the container

        StackPane dropdownContainer = new StackPane(panel, view);
        dropdownContainer.setAlignment(Pos.CENTER);
        dropdownContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        rootContainer.getChildren().setAll(backgroundNodes);
        rootContainer.getChildren().add(dropdownContainer);

        rootContainer.applyCss();
        rootContainer.layout();

        double targetHeight = dropdownContainer.getHeight();
        if (targetHeight <= 0) targetHeight = 500;

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(dropdownContainer.widthProperty());
        clip.setHeight(0);
        dropdownContainer.setClip(clip);

        Timeline frameTimeline = new Timeline();
        for (int i = 0; i < frameCount; i++) {
            final int frame = i;
            frameTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 40), e -> panel.setFrame(frame)));
        }

        Timeline clipTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(clip.heightProperty(), 0, Interpolator.EASE_IN)),
            new KeyFrame(Duration.millis(480), new KeyValue(clip.heightProperty(), targetHeight, Interpolator.EASE_OUT))
        );

        frameTimeline.play();
        clipTimeline.play();
    }
}