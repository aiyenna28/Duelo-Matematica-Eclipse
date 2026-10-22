package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public final class ViewUtils {

    public enum ButtonSize {
        SMALL(12),
        MEDIUM(15),
        LARGE(18);

        private final int fontSize;
        ButtonSize(int fontSize) { this.fontSize = fontSize; }
        public int getFontSize() { return fontSize; }
    }

    private static String CUSTOM_FONT_FAMILY = "System";

    static {
        try {
            InputStream fontStream = ViewUtils.class.getResourceAsStream("/_assets/fonts/your_font.ttf");
            if (fontStream != null) {
                Font customFont = Font.loadFont(fontStream, 12);
                if (customFont != null) {
                    CUSTOM_FONT_FAMILY = customFont.getFamily();
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR loading custom font: " + e.getMessage());
        }
    }

    private ViewUtils() { }

    public static StackPane createHeaderLabel(String text) {
        return createSpriteHeader("/panels/ui_scroll.png", text);
    }

    /**
     * Creates a clean, properly proportioned single background panel for popups, 
     * replacing the default white container without vertical stretching bugs.
     */
    public static StackPane createFullScrollBackground(VBox contentContainer, String imagePath) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Scroll Image NOT FOUND at path: /_assets" + imagePath);
            StackPane fallback = new StackPane(contentContainer);
            fallback.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #777777; -fx-border-width: 1px;");
            return fallback;
        }

        Image scrollSheet = new Image(stream);
        double sheetWidth = scrollSheet.getWidth();
        double sheetHeight = scrollSheet.getHeight();

        // Use the final frame if it's an animation strip
        double frameWidth = sheetWidth;
        double startX = 0;
        if (imagePath.contains("ui_popup_scroll")) {
            int frameCount = 12;
            frameWidth = sheetWidth / frameCount;
            startX = frameWidth * (frameCount - 1);
        }

        ImageView bgView = new ImageView(scrollSheet);
        bgView.setViewport(new Rectangle2D(startX, 0, frameWidth, sheetHeight));
        bgView.setFitWidth(320); // Clean fixed popup window width
        bgView.setFitHeight(420); // Clean fixed popup window height
        bgView.setPreserveRatio(false);

        StackPane fullScrollPopup = new StackPane(bgView, contentContainer);
        fullScrollPopup.setAlignment(Pos.CENTER);
        fullScrollPopup.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);

        return fullScrollPopup;
    }

    /**
     * Popup Header with an Unrolling Animation Strip (Left to Right frames)
     * and a synchronized clipping mask that reveals the popup contents progressively.
     */
    public static StackPane createPopupHeader(String text, VBox contentContainer) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets/panels/ui_popup_scroll.png");
        if (stream == null) {
            System.err.println("ERROR: Popup Scroll Animation Strip NOT FOUND at path: /_assets/panels/ui_popup_scroll.png");
            return new StackPane(new Label(text));
        }

        Image sheet = new Image(stream);
        double sheetTotalWidth = sheet.getWidth();
        double sheetHeight = sheet.getHeight();

        int frameCount = 12; 
        double frameWidth = sheetTotalWidth / frameCount;

        ImageView imageView = new ImageView(sheet);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(260); 
        imageView.setFitHeight(70); 

        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, sheetHeight));

        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setStyle(
            "-fx-font-family: '" + CUSTOM_FONT_FAMILY + "'; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #3b200c; " + 
            "-fx-alignment: center; " +
            "-fx-text-alignment: center;"
        );

        StackPane graphicStack = new StackPane(imageView, headerLabel);
        graphicStack.setAlignment(Pos.CENTER);
        graphicStack.setPadding(new Insets(5, 10, 5, 10));

        javafx.scene.shape.Rectangle contentClip = new javafx.scene.shape.Rectangle(0, 0, 0, 0);
        if (contentContainer != null) {
            contentContainer.setClip(contentClip);
            contentContainer.setOpacity(0);
        }

        Timeline unrollAnimation = new Timeline();
        double frameDurationMillis = 35; 

        for (int i = 0; i < frameCount; i++) {
            final double currentX = i * frameWidth;
            final double progressRatio = (double) (i + 1) / frameCount;
            
            unrollAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(i * frameDurationMillis), event -> {
                    imageView.setViewport(new Rectangle2D(currentX, 0, frameWidth, sheetHeight));
                    
                    if (contentContainer != null) {
                        contentContainer.setOpacity(1);
                        contentClip.setWidth(contentContainer.getWidth() * progressRatio);
                        contentClip.setHeight(contentContainer.getHeight() > 0 ? contentContainer.getHeight() : 400);
                    }
                })
            );
        }
        unrollAnimation.setCycleCount(1);

        unrollAnimation.setOnFinished(e -> {
            if (contentContainer != null) {
                contentContainer.setClip(null); 
            }
        });

        graphicStack.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                unrollAnimation.playFromStart();
            }
        });

        return graphicStack;
    }

    /**
     * True Horizontal 3-Slice Scroll Header for Main Menus:
     * Keeps left & right wooden rollers at native pixel size, and loops the middle parchment section horizontally.
     */
    public static StackPane createSpriteHeader(String imagePath, String text) {
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Scroll Image NOT FOUND at path: /_assets" + imagePath);
            return new StackPane(new Label(text));
        }

        Image scrollSheet = new Image(stream);
        double sheetWidth = scrollSheet.getWidth();
        double sheetHeight = scrollSheet.getHeight();

        double capWidth = 14.0; 
        double middleSliceWidth = sheetWidth - (capWidth * 2); 

        Label headerLabel = new Label(text);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setStyle(
            "-fx-font-family: '" + CUSTOM_FONT_FAMILY + "'; " +
            "-fx-font-size: 18px; " + 
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #3b200c; " + 
            "-fx-alignment: center; " + 
            "-fx-text-alignment: center;"
        );

        HBox scrollBox = new HBox();
        scrollBox.setAlignment(Pos.CENTER);
        scrollBox.setFillHeight(true);

        int maxLineLength = 0;
        for (String line : text.split("\n")) {
            if (line.length() > maxLineLength) maxLineLength = line.length();
        }
        int middleTilesCount = Math.max(4, (int) Math.ceil(maxLineLength * 1.2)); 

        ImageView leftCap = new ImageView(scrollSheet);
        leftCap.setViewport(new Rectangle2D(0, 0, capWidth, sheetHeight));
        leftCap.setFitWidth(capWidth);
        leftCap.setFitHeight(sheetHeight);

        HBox middleContainer = new HBox();
        middleContainer.setAlignment(Pos.CENTER);
        for (int i = 0; i < middleTilesCount; i++) {
            ImageView midTile = new ImageView(scrollSheet);
            midTile.setViewport(new Rectangle2D(capWidth, 0, middleSliceWidth, sheetHeight));
            midTile.setFitWidth(middleSliceWidth);
            midTile.setFitHeight(sheetHeight);
            middleContainer.getChildren().add(midTile);
        }

        ImageView rightCap = new ImageView(scrollSheet);
        rightCap.setViewport(new Rectangle2D(sheetWidth - capWidth, 0, capWidth, sheetHeight));
        rightCap.setFitWidth(capWidth);
        rightCap.setFitHeight(sheetHeight);

        scrollBox.getChildren().addAll(leftCap, middleContainer, rightCap);

        StackPane graphicStack = new StackPane(scrollBox, headerLabel);
        graphicStack.setAlignment(Pos.CENTER);
        graphicStack.setPadding(new Insets(15, 25, 15, 25));

        return graphicStack;
    }
    
    public static Button createCloseButton(String text) {
        Button closeButton = new Button(text);
        closeButton.setStyle(
            "-fx-font-family: '" + CUSTOM_FONT_FAMILY + "'; " +
            "-fx-background-color: transparent; -fx-font-weight: bold;" +
            "-fx-font-size: 14px; -fx-cursor: hand;"
        );
        return closeButton;
    }
   
    public static Button createSpriteButton(String imagePath, Runnable onClickAction) {
        return createSpriteButton(imagePath, ButtonSize.LARGE, onClickAction);
    }

    public static Button createSpriteButton(String imagePath, ButtonSize size, Runnable onClickAction) {
       
        InputStream stream = ViewUtils.class.getResourceAsStream("/_assets" + imagePath);
        if (stream == null) {
            System.err.println("ERROR: Image NOT FOUND at path: /_assets" + imagePath);
            return new Button("Missing Image");
        }
      
        Image spriteSheet = new Image(stream);
       
        double totalWidth = spriteSheet.getWidth(); 
        double sheetHeight = spriteSheet.getHeight(); 
        double stateWidth = totalWidth / 3.0; 

        double targetHeight = 55.0;
        double scale = targetHeight / sheetHeight;

        double capWidth = stateWidth * 0.25; 
        double middleWidth = stateWidth - (capWidth * 2); 

        double scaledCapWidth = capWidth * scale;
        double scaledMiddleWidth = middleWidth * scale;

        Label textLabel = new Label();
        DropShadow textShadow = new DropShadow(2, 0, 1, Color.rgb(0, 0, 0, 0.7));
        String textStyle = "-fx-font-family: '" + CUSTOM_FONT_FAMILY + "'; " +
                           "-fx-font-size: " + size.getFontSize() + "px; " +
                           "-fx-font-weight: bold; -fx-text-fill: #ffffff; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 2, 0, 0, 1);";
        textLabel.setStyle(textStyle);

        HBox normalBox = createMultiSliceStateBox(spriteSheet, 0, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth);
        HBox hoverBox = createMultiSliceStateBox(spriteSheet, stateWidth, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth);
        HBox pressedBox = createMultiSliceStateBox(spriteSheet, stateWidth * 2, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth);

        StackPane graphicStack = new StackPane(normalBox, textLabel);
        graphicStack.setAlignment(Pos.CENTER);
        graphicStack.setPadding(new Insets(0, 15, 0, 15));

        Button button = new Button();
        button.setGraphic(graphicStack);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;");
        button.setMinHeight(targetHeight);
        button.setMaxHeight(targetHeight);

        button.textProperty().addListener((obs, oldV, newV) -> {
            textLabel.setText(newV);
            double approxCharWidth = size.getFontSize() * 0.65;
            double requiredTextSpace = newV.length() * approxCharWidth + 30;
            double minContentWidth = Math.max(140, requiredTextSpace); 
            
            int middleTilesCount = (int) Math.ceil((minContentWidth - (scaledCapWidth * 2)) / scaledMiddleWidth);
            middleTilesCount = Math.max(1, middleTilesCount);

            updateStateBox(normalBox, spriteSheet, 0, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth, middleTilesCount);
            updateStateBox(hoverBox, spriteSheet, stateWidth, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth, middleTilesCount);
            updateStateBox(pressedBox, spriteSheet, stateWidth * 2, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth, middleTilesCount);
        });

        button.setText("BUTTON");

        button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (button.isArmed()) return;
            graphicStack.getChildren().set(0, isNowHovered ? hoverBox : normalBox);
        });

        button.armedProperty().addListener((obs, wasArmed, isNowArmed) -> {
            if (isNowArmed) {
                graphicStack.getChildren().set(0, pressedBox);
                textLabel.setTranslateX(2);
                textLabel.setTranslateY(2);
                textLabel.setEffect(null);
            } else {
                graphicStack.getChildren().set(0, button.isHover() ? hoverBox : normalBox);
                textLabel.setTranslateX(0);
                textLabel.setTranslateY(0);
                textLabel.setEffect(textShadow);
            }
        });

        if (onClickAction != null) {
            button.setOnAction(e -> onClickAction.run());
        }

        return button;
    }

    private static HBox createMultiSliceStateBox(Image sheet, double startX, double stateWidth, double sheetHeight, 
                                                 double scaledCapWidth, double scaledMiddleWidth, double targetHeight, 
                                                 double capWidth, double middleWidth) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setFillHeight(true);
        updateStateBox(box, sheet, startX, stateWidth, sheetHeight, scaledCapWidth, scaledMiddleWidth, targetHeight, capWidth, middleWidth, 2);
        return box;
    }

    private static void updateStateBox(HBox box, Image sheet, double startX, double stateWidth, double sheetHeight, 
                                       double scaledCapWidth, double scaledMiddleWidth, double targetHeight, 
                                       double capWidth, double middleWidth, int middleTilesCount) {
        box.getChildren().clear();

        ImageView leftCap = new ImageView(sheet);
        leftCap.setViewport(new Rectangle2D(startX, 0, capWidth, sheetHeight));
        leftCap.setFitWidth(scaledCapWidth);
        leftCap.setFitHeight(targetHeight);

        HBox middleContainer = new HBox();
        for (int i = 0; i < middleTilesCount; i++) {
            ImageView midTile = new ImageView(sheet);
            midTile.setViewport(new Rectangle2D(startX + capWidth, 0, middleWidth, sheetHeight));
            midTile.setFitWidth(scaledMiddleWidth);
            midTile.setFitHeight(targetHeight);
            middleContainer.getChildren().add(midTile);
        }

        ImageView rightCap = new ImageView(sheet);
        rightCap.setViewport(new Rectangle2D(startX + stateWidth - capWidth, 0, capWidth, sheetHeight));
        rightCap.setFitWidth(scaledCapWidth);
        rightCap.setFitHeight(targetHeight);

        box.getChildren().addAll(leftCap, middleContainer, rightCap);
    }
}