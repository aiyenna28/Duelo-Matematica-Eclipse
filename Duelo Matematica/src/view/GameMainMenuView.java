package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameMainMenuView {

    private final StackPane root;
    private final StackPane headerScroll;
    private final Button btnStart;
    private final Button btnCredits;
    private final Button btnBack;
    private final Button gearButton;

    public GameMainMenuView(String titleText) {
        headerScroll = ViewUtils.createHeaderLabel(titleText);

        btnStart   = createBorderedButton("START", ViewUtils.ButtonSize.MEDIUM);
        btnCredits = createBorderedButton("CREDITS", ViewUtils.ButtonSize.MEDIUM);
        btnBack    = createBorderedButton("BACK", ViewUtils.ButtonSize.MEDIUM);

        VBox buttonBox = new VBox(15, btnStart, btnCredits, btnBack);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(true);

        VBox centerContent = new VBox(20, headerScroll, buttonBox);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setFillWidth(false);

        gearButton = createGearButton();

        root = new StackPane();
        root.setStyle("-fx-background-color: #ffffff;");
        root.getChildren().addAll(centerContent, gearButton);
        
        StackPane.setAlignment(gearButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(gearButton, new Insets(20));
    }

    private Button createBorderedButton(String text, ViewUtils.ButtonSize size) {
        Button button = ViewUtils.createSpriteButton("/buttons/btn.png", size, null);
        button.setText(text);
        return button;
    }

    private Button createGearButton() {
        Button gearBtn = new Button("\u2699"); 
        gearBtn.setPrefSize(48, 48);
        gearBtn.setStyle(
            "-fx-font-size: 24px; -fx-font-weight: bold;" +
            "-fx-border-color: #000000; -fx-border-width: 2px;" +
            "-fx-border-radius: 50%; -fx-background-radius: 50%;" +
            "-fx-background-color: #f9f9f9; -fx-cursor: hand;"
        );
        return gearBtn;
    }

    public Node getRoot()             { return root; }
    public Button getStartButton()    { return btnStart; }
    public Button getCreditsButton()  { return btnCredits; }
    public Button getBackButton()     { return btnBack; }
    public Button getGearButton()     { return gearButton; }
}