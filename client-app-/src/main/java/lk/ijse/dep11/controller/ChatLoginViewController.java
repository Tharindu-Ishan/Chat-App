package lk.ijse.dep11.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
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
        readServerResponse();
        Platform.runLater(()-> closeSocketOnStageCloseRequest() );
        lstUsers.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(String text, boolean empty) {
                        super.updateItem(text, empty);
                        if(!empty){
                            setGraphic(new Circle(5, Color.LIMEGREEN));
                            setGraphicTextGap(7.5);
                            setText(text);
                        }else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

    }
    private void connect(){
        try {
            socket = new Socket("192.168.8.105", 5050);
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
    private void closeSocketOnStageCloseRequest(){
        txtMsg.getScene().getWindow().setOnCloseRequest(windowEvent -> {
            try {
                oos.writeObject(new Dep11Message(Dep11Headers.EXIT,null));
                oos.flush();
                if(!socket.isClosed()) socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
    @FXML
    void imgSendOnMouseClicked(MouseEvent event) {
        txtMsg.fireEvent(new ActionEvent());

    }

    @FXML
    void txtMsgOnAction(ActionEvent event) {
        try {
            Dep11Message msg = new Dep11Message(Dep11Headers.MSG, txtMsg.getText());
            oos.writeObject(msg);
            oos.flush();
            txtMsg.clear();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to connect to the server,try again").show();
            e.printStackTrace();
        }

    }

}
