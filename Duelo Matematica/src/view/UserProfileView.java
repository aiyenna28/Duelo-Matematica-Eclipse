package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import model.UserProfileModel;

public class UserProfileView {

    private final VBox root;
    private final Button backButton;

    public UserProfileView(UserProfileModel profile) {
        // 1. Initialize root container first
        root = new VBox(20);
        root.setPadding(new Insets(15, 30, 30, 30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setStyle(
                "-fx-background-color: #f4f4f4; -fx-border-color: #777777;" +
                "-fx-border-width: 1px;"
            );

        // 2. Pass root into createPopupHeader to sync content reveal with the unroll animation
        StackPane titleScroll = ViewUtils.createPopupHeader("PROFILE", root);
        backButton = ViewUtils.createCloseButton("X");

        BorderPane headerBox = new BorderPane();
        headerBox.setCenter(titleScroll);
        headerBox.setRight(backButton);
        BorderPane.setAlignment(titleScroll, Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(80); 
        grid.setVgap(25); 
        grid.setAlignment(Pos.CENTER_LEFT);

        String labelStyle = "-fx-font-size: 20px; -fx-text-fill: #222222;";
        String valueStyle = "-fx-font-size: 20px; -fx-text-fill: #000000;";

        Label userLabel = new Label("USER:");
        userLabel.setStyle(labelStyle);
        Label userValue = new Label(profile.getUsername().toUpperCase());
        userValue.setStyle(valueStyle);
        grid.add(userLabel, 0, 0);
        grid.add(userValue, 1, 0);

        Label tierLabel = new Label("TIER:");
        tierLabel.setStyle(labelStyle);
        Label tierValue = new Label(profile.getTier().toUpperCase());
        tierValue.setStyle(valueStyle);
        grid.add(tierLabel, 0, 1);
        grid.add(tierValue, 1, 1);

        Label eloLabel = new Label("ELO:");
        eloLabel.setStyle(labelStyle);
        Label eloValue = new Label(String.valueOf(profile.getElo()));
        eloValue.setStyle(valueStyle);
        grid.add(eloLabel, 0, 2);
        grid.add(eloValue, 1, 2);

        // 3. Add components to root
        root.getChildren().addAll(headerBox, grid);
    }

    public Node getRoot()          { return root; }
    public Button getBackButton()  { return backButton; }
}