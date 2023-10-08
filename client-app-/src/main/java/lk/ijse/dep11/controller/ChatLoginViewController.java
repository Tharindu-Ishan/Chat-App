package lk.ijse.dep11.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lk.ijse.dep11.Dep11Headers;
import lk.ijse.dep11.Dep11Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatLoginViewController {

    @FXML
    private ImageView imgSend;

    @FXML
    private ListView<String> lstUsers;

    @FXML
    private TextArea txtChatHistory;

    @FXML
    private TextField txtMsg;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public void initialize(){
        connect();

    }
    private void connect(){
        try {
            socket = new Socket("172.20.10.10", 5050);
            OutputStream os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to Connect to the server!").show();
            Platform.exit();
        }
    }
    private void readServerResponse(){
        new Thread(()->{
            try {
                InputStream is = socket.getInputStream();
                ois = new ObjectInputStream(is);

                while (true){
                    Dep11Message msg = (Dep11Message)ois.readObject();
                    if(msg.getHeader()== Dep11Headers.USERS){
                        ArrayList<String> ipAddressList=(ArrayList<String>)msg.getBody();
                        Platform.runLater(()->{
                            lstUsers.getItems().clear();
                            lstUsers.getItems().addAll(ipAddressList);
                        });
                    } else if (msg.getHeader()==Dep11Headers.MSG) {
                        Platform.runLater(()->{
                            txtChatHistory.setText(msg.getBody().toString());
                            txtChatHistory.setScrollTop(Double.MAX_VALUE);
                        });
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if(e instanceof EOFException){
                    Platform.runLater(()->{
                        new Alert(Alert.AlertType.ERROR,"Connection lost, try Again!").show();
                        Platform.exit();
                    });
                } else if (!socket.isClosed()) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @FXML
    void imgSendOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void txtMsgOnAction(ActionEvent event) {

    }

}
