package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameStartMenuView {

    private final StackPane root;
    private final Button btnStory;
    private final Button btn1vs1;
    private final Button btnProfile;
    private final Button btnLeaderboard;
    private final Button btnBack;
    private final Button gearButton;

    public GameStartMenuView(String titleText) {
        StackPane headerScroll = ViewUtils.createHeaderLabel(titleText);

        btnStory       = createBorderedButton("STORY", ViewUtils.ButtonSize.MEDIUM);
        btn1vs1        = createBorderedButton("1VS1", ViewUtils.ButtonSize.MEDIUM);
        btnProfile     = createBorderedButton("PROFILE", ViewUtils.ButtonSize.MEDIUM);
        btnLeaderboard = createBorderedButton("LEADERBOARD", ViewUtils.ButtonSize.SMALL);
        btnBack        = createBorderedButton("BACK", ViewUtils.ButtonSize.MEDIUM);

        HBox profileLeaderboardRow = new HBox(15, btnProfile, btnLeaderboard);
        profileLeaderboardRow.setAlignment(Pos.CENTER);
        profileLeaderboardRow.setFillHeight(true);
        
        HBox.setHgrow(btnProfile, Priority.ALWAYS);
        HBox.setHgrow(btnLeaderboard, Priority.ALWAYS);

        VBox buttonBox = new VBox(15, btnStory, btn1vs1, profileLeaderboardRow, btnBack);
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

    public Node getRoot()                 { return root; }
    public Button getStoryButton()        { return btnStory; }
    public Button get1vs1Button()         { return btn1vs1; }
    public Button getProfileButton()      { return btnProfile; }
    public Button getLeaderboardButton(){ return btnLeaderboard; }
    public Button getBackButton()         { return btnBack; }
    public Button getGearButton()         { return gearButton; }
}