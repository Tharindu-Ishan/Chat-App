package lk.ijse.dep11;

import lk.ijse.dep11.users.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerAppInitializer {
    private  static  volatile ArrayList<User> userList=new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5050);
        System.out.println("Server is listening");

        while (true){
            System.out.println("Wainting for an incoming connection");
            Socket localSocket = serverSocket.accept();
            User user = new User(localSocket);


        }
    }

}
