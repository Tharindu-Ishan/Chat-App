package lk.ijse.dep11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scene/LoginScene.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene chatScene = new Scene(root);
        primaryStage.setScene(chatScene);
        primaryStage.setTitle("Login to Chat App");
        primaryStage.show();
        primaryStage.centerOnScreen();

    }
}
