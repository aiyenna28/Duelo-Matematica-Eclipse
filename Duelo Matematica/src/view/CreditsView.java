package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class CreditsView {

    private final VBox root;
    private final Button backButton;

    public CreditsView() {
        // 1. Initialize root container first
        root = new VBox(20);
        root.setPadding(new Insets(15, 40, 40, 40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #777777; -fx-border-width: 1px;");

        // 2. Pass root into createPopupHeader to sync content reveal with the unroll animation
        StackPane titleScroll = ViewUtils.createPopupHeader("CREDITS", root);
        backButton = ViewUtils.createCloseButton("X");

        BorderPane headerBox = new BorderPane();
        headerBox.setCenter(titleScroll);
        headerBox.setRight(backButton);
        BorderPane.setAlignment(titleScroll, Pos.CENTER);

        VBox leftCol = new VBox(20);
        leftCol.getChildren().addAll(
            createSection("PROGRAMMERS", "CIRE", "AIYENNA"),
            createSection("UI / UX DESIGN", "HILARIE"),
            createSection("PLAYTESTER", "AIYENNA")
        );

        VBox rightCol = new VBox(20);
        rightCol.getChildren().addAll(
            createSection("AUDIO DESIGN", "HILARIE"),
            createSection("ART & ANIMATION", "HILARIE"),
            createSection("PM & ARCHITECT", "CIRE")
        );

        GridPane grid = new GridPane();
        grid.setHgap(50); 
        grid.setAlignment(Pos.CENTER);
        grid.add(leftCol, 0, 0);
        grid.add(rightCol, 1, 0);

        // 3. Add components to root
        root.getChildren().addAll(headerBox, grid);
    }

    private VBox createSection(String role, String... names) {
        Label roleLabel = new Label(role);
        roleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #222222;");

        VBox section = new VBox(2); 
        section.getChildren().add(roleLabel);

        for (String name : names) {
            Label nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
            section.getChildren().add(nameLabel);
        }

        return section;
    }

    public Node getRoot() { return root; }
    public Button getBackButton() { return backButton; }
}