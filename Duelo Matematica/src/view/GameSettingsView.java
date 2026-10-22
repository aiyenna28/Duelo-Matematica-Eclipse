package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;

public class GameSettingsView {

    private final VBox root;
    private final Button backButton;
    private final Slider musicSlider;
    private final Slider sfxSlider;

    public GameSettingsView() {
        // 1. Initialize root container first
        root = new VBox(20);
        root.setPadding(new Insets(15, 30, 30, 30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #777777; -fx-border-width: 1px;");

        // 2. Pass root into createPopupHeader to sync content reveal with the unroll animation
        StackPane titleScroll = ViewUtils.createPopupHeader("SETTINGS", root);
        backButton = ViewUtils.createCloseButton("X");

        BorderPane headerBox = new BorderPane();
        headerBox.setCenter(titleScroll);
        headerBox.setRight(backButton);
        BorderPane.setAlignment(titleScroll, Pos.CENTER);

        musicSlider = new Slider(0, 100, 50);
        musicSlider.setPrefWidth(150);

        sfxSlider = new Slider(0, 100, 50);
        sfxSlider.setPrefWidth(150);

        Label musicLabel = new Label("MUSIC");
        musicLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
        Label sfxLabel = new Label("SFX");
        sfxLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(25); 
        formGrid.setVgap(25); 
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(musicLabel, 0, 0);
        formGrid.add(musicSlider, 1, 0);
        formGrid.add(sfxLabel, 0, 1);
        formGrid.add(sfxSlider, 1, 1);

        // 3. Add components to root
        root.getChildren().addAll(headerBox, formGrid);
    }

    public Node getRoot() { return root; }
    public Button getBackButton() { return backButton; }
    public Slider getMusicSlider() { return musicSlider; }
    public Slider getSfxSlider() { return sfxSlider; }
}