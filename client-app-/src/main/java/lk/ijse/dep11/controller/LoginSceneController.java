package lk.ijse.dep11.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep11.UserName;

import java.io.IOException;

public class LoginSceneController {

    public AnchorPane root;
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnUpload;

    @FXML
    private TextField txtUserName;

    public static String username;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scene/ChatScene.fxml"));
        AnchorPane root1 = fxmlLoader.load();
        Scene chatScene = new Scene(root1);
        Stage primaryStage=(Stage) root.getScene().getWindow();
        primaryStage.setScene(chatScene);
        primaryStage.setTitle("Chat-App");
        primaryStage.show();
        primaryStage.centerOnScreen();

    }

    @FXML
    void btnUploadOnAction(ActionEvent event) {
        if(txtUserName.getText().strip().isBlank()){
            txtUserName.requestFocus();
            return;
        }
        else {
            UserName.name=new UserName(txtUserName.getText().strip());
        }


    }

}
