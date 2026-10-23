package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * GAME START MENU VIEW — The visual layout for the "Select Mode" screen.
 * 
 * Uses custom 9-slice scroll headers and sprite buttons consistent with MainMenuView.
 */
public class GameStartMenuView {

    private final StackPane root;
    private final Button btnStory;
    private final Button btn1vs1;
    private final Button btnProfile;
    private final Button btnLeaderboard;
    private final Button btnBack;
    private final Button gearButton;

    public GameStartMenuView(String titleText) {
        // 1. Build Header using the 9-slice sprite header utility
        StackPane headerLabel = ViewUtils.createSpriteHeader("/panels/ui_scroll.png", titleText);

        // 2. Build Main Buttons using sprite buttons
        btnStory       = ViewUtils.createSpriteButton("/buttons/btn.png", "STORY", null);
        btn1vs1        = ViewUtils.createSpriteButton("/buttons/btn.png", "1VS1", null);
        btnLeaderboard = ViewUtils.createSpriteButton("/buttons/btn.png", "LEADERBOARD", null);
        btnBack        = ViewUtils.createSpriteButton("/buttons/btn.png", "BACK", null);
        
        // Profile can be a two-frame icon button or a sprite button depending on preference; 
        // retaining your two-frame sprite choice here:
        btnProfile     = ViewUtils.createTwoFrameSpriteButton("/buttons/btn_profile.png", 50, null);

        // 3. Side-by-side row for Profile & Leaderboard
        // If btnLeaderboard is a full sprite text button, you can place an icon/button side-by-side cleanly.
        HBox profileLeaderboardRow = new HBox(15, btnProfile, btnLeaderboard);
        profileLeaderboardRow.setAlignment(Pos.CENTER);
        profileLeaderboardRow.setFillHeight(true);
        
        // Make the leaderboard expand to fill available row space if needed
        HBox.setHgrow(btnLeaderboard, Priority.ALWAYS);

        // 4. Group all buttons vertically
        VBox buttonBox = new VBox(15, btnStory, btn1vs1, profileLeaderboardRow, btnBack);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(true);

        // 5. Stack the header and buttons in the center
        VBox centerContent = new VBox(30, headerLabel, buttonBox);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setFillWidth(false);

        // 6. Build the Gear Button (bottom-left)
        gearButton = ViewUtils.createTwoFrameSpriteButton("/buttons/btn_settings.png", 48, null);

        // 7. Assemble Root Layout
        root = new StackPane();
        root.setStyle("-fx-background-color: #ffffff;");
        root.getChildren().addAll(centerContent, gearButton);
        
        // Pin the gear icon to the bottom-left
        StackPane.setAlignment(gearButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(gearButton, new Insets(20));
    }

    // --- GETTERS for the Controller ---
    public Node getRoot()             { return root; }
    public Button getStoryButton()      { return btnStory; }
    public Button get1vs1Button()       { return btn1vs1; }
    public Button getProfileButton()    { return btnProfile; }
    public Button getLeaderboardButton(){ return btnLeaderboard; }
    public Button getBackButton()       { return btnBack; }
    public Button getGearButton()       { return gearButton; }
}