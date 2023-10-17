package lk.ijse.dep11;

import lk.ijse.dep11.controller.LoginSceneController;
import lk.ijse.dep11.users.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerAppInitializer {
    private  static  volatile ArrayList<User> userList=new ArrayList<>();
    private static volatile String chatHistory="";
    private static UserName username;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(5050);
        System.out.println("Server is listening");

        while (true){
            System.out.println("Waiting for an incoming connection");
            Socket localSocket = serverSocket.accept();
            InputStream is = localSocket.getInputStream();
            ObjectInputStream ois2 = new ObjectInputStream(is);
            username = (UserName)ois2.readObject();


            User user = new User(localSocket,username);
            userList.add(user);
            System.out.println("New connection "+user.getRemoteIpAddress() );
            new Thread(()->{
                try{
                  sendChatHistory(user);
                  broadcastLoggedUsers();
                    ObjectInputStream ois = user.getObjectInputStream();
                    while (true){
                        Dep11Message msg = (Dep11Message) ois.readObject();
                        if(msg.getHeader()==Dep11Headers.MSG){
                            chatHistory+=String.format("%s: %s\n",user.getUserName().username,msg.getBody());
                            broadcastChatHistory();
                        } else if (msg.getHeader()==Dep11Headers.EXIT) {
                            removeUser(user);
                            return;

                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    removeUser(user);
                    if(e instanceof EOFException) return;
                    e.printStackTrace();
                }

            }).start();


        }
    }
    private static void sendChatHistory(User user) throws IOException {

        ObjectOutputStream oos = user.getObjectOutputStream();
        Dep11Message msg = new Dep11Message(Dep11Headers.MSG, chatHistory);
        oos.writeObject(msg);
        oos.flush();
    }
    private static void broadcastLoggedUsers(){

        ArrayList<String> ipAddressList = new ArrayList<>();
        ArrayList<String> userNameList = new ArrayList<>();
        for (User user : userList) {
            ipAddressList.add(user.getRemoteIpAddress()); //getRemote
            userNameList.add(user.getUserName().username);
        }
        for (User user : userList) {
            new Thread(()->{
                try {
                    ObjectOutputStream oos = user.getObjectOutputStream();
                    Dep11Message msg = new Dep11Message(Dep11Headers.USERS, userNameList);
                    oos.writeObject(msg);
                    oos.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
    private static void broadcastChatHistory(){


        for (User user : userList) {
            new Thread(()->{
                try {
                    ObjectOutputStream oos = user.getObjectOutputStream();
                    oos.writeObject(new Dep11Message(Dep11Headers.MSG,chatHistory));
                    oos.flush();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    private static void removeUser(User user){


        if(userList.contains(user)){
            userList.remove(user);
            broadcastLoggedUsers();
            if(!user.getLocalSocket().isClosed()){
                try {
                    user.getLocalSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
