package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginView {

    private StackPane rootView;
    
    private TextField emailField;
    private PasswordField passwordField;
    private Button confirmButton;
    private Button backButton; // "X" close button
    private Hyperlink forgotPasswordLink;
    private Label messageLabel;

    public LoginView() {
        // 1. Popup title header scroll
        VBox dummyContainer = new VBox();
        StackPane titleScroll = ViewUtils.createPopupHeader("LOGIN", dummyContainer);
        backButton = ViewUtils.createCloseButton("X");

        BorderPane headerBox = new BorderPane();
        headerBox.setCenter(titleScroll);
        headerBox.setRight(backButton);
        BorderPane.setAlignment(titleScroll, Pos.CENTER);

        // 2. Form fields and inputs
        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b200c; -fx-font-size: 12px;");
        
        emailField = new TextField();
        emailField.setPromptText("demo@duelo.com");
        emailField.setPrefWidth(220);

        Label passLabel = new Label("PASSWORD");
        passLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b200c; -fx-font-size: 12px;");
        
        passwordField = new PasswordField();
        passwordField.setPrefWidth(220);

        forgotPasswordLink = new Hyperlink("FORGOT PASSWORD?");
        forgotPasswordLink.setStyle("-fx-text-fill: #3b200c; -fx-font-size: 11px;");

        confirmButton = ViewUtils.createSpriteButton("/buttons/btn.png", ViewUtils.ButtonSize.LARGE, null);
        confirmButton.setText("CONFIRM");

        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        messageLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        // 3. Pack everything into the main inner content container
        VBox contentBox = new VBox(12, headerBox, emailLabel, emailField, passLabel, passwordField, forgotPasswordLink, messageLabel, confirmButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20, 40, 40, 40));

        // 4. Wrap the entire layout in the single cohesive background scroll panel (replacing the white box)
        rootView = ViewUtils.createFullScrollBackground(contentBox, "/panels/ui_popup_scroll.png");
    }

    public StackPane getView() {
        return rootView;
    }

    public StackPane getRoot() {
        return rootView;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Hyperlink getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}