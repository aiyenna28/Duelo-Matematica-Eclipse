package app;

import controller.MainMenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.AuthService;
import view.MainMenuView;
import view.ViewUtils; // 1. Make sure ViewUtils is imported

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // 2. LOAD THE CUSTOM FONT FIRST (Toolkit is now active and ready)
        ViewUtils.loadCustomFont();

        // MODEL: one shared instance of the account manager.
        AuthService authService = new AuthService();

        // VIEW: build the window shell.
        MainMenuView view = new MainMenuView(stage);

        // CONTROLLER: attach actions to the buttons.
        new MainMenuController(view, authService);

        // Show the OS window.
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}