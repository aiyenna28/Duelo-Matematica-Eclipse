package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * GAME MAIN MENU VIEW — The visual layout for the secondary game menu.
 * 
 * Updated to utilize standardized multi-slice sprites and full-screen layout design.
 */
public class GameMainMenuView {

    private final StackPane root;
    
    // The header rendered using the multi-slice scroll asset
    private final StackPane headerLabel;
    
    // The main buttons powered by sprite sheets
    private final Button btnStart;
    private final Button btnCredits;
    private final Button btnBack;
    
    // The gear icon in the corner
    private final Button gearButton;

    public GameMainMenuView(String titleText) {
        
        // ==========================================
        // 1. THE HEADER (Using Multi-Slice Sprite Header)
        // ==========================================
        headerLabel = ViewUtils.createSpriteHeader("/panels/ui_scroll.png", titleText);

        // ==========================================
        // 2. THE MAIN BUTTONS (Using Sprite Buttons)
        // ==========================================
        btnStart   = ViewUtils.createSpriteButton("/buttons/btn.png", "START", null);
        btnCredits = ViewUtils.createSpriteButton("/buttons/btn.png", "CREDITS", null);
        btnBack    = ViewUtils.createSpriteButton("/buttons/btn.png", "BACK", null);
        // Stack the three buttons vertically in a VBox with comfortable spacing.
        VBox buttonBox = new VBox(15, btnStart, btnCredits, btnBack);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(false);

        // ==========================================
        // 3. GROUPING THE CENTER CONTENT
        // ==========================================
        VBox centerContent = new VBox(25, headerLabel, buttonBox);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setFillWidth(false);

        // ==========================================
        // 4. THE GEAR BUTTON (SETTINGS)
        // ==========================================
        gearButton = createGearButton();

        // ==========================================
        // 5. ASSEMBLING THE ROOT LAYOUT
        // ==========================================
        root = new StackPane();
        root.setStyle("-fx-background-color: #ffffff;"); // Clean full-screen background canvas
        root.getChildren().addAll(centerContent, gearButton);
        
        // Center the main content block
        StackPane.setAlignment(centerContent, Pos.CENTER);

        // Push the gear button to the bottom-left corner with proper margins.
        StackPane.setAlignment(gearButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(gearButton, new Insets(20));
    }

    // ==========================================
    // UI HELPER METHODS
    // ==========================================

    /**
     * HELPER — Builds the circular/icon Gear button using a 2-frame sprite.
     */
    private Button createGearButton() {
        Button gearBtn = ViewUtils.createTwoFrameSpriteButton("/buttons/btn_settings.png", 48, null);
        return gearBtn;
    }

    // ============================================================
    // GETTERS — The "Control Panel" for the Controller
    // ============================================================

    public Node getRoot()             { return root; }
    
    public Button getStartButton()    { return btnStart; }
    
    public Button getCreditsButton()  { return btnCredits; }
    
    public Button getBackButton()     { return btnBack; }
    
    public Button getGearButton()     { return gearButton; }
}