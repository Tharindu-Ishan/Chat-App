package lk.ijse.dep11.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class User {
//    private Socket localSocket;
//    private ObjectOutputStream objectOutputStream;
//    private ObjectInputStream objectInputStream;
//
//    public User(Socket localSocket) throws IOException {
//        this.localSocket = localSocket;
//        OutputStream os = localSocket.getOutputStream();
//        objectOutputStream=new ObjectOutputStream(os);
//        objectOutputStream.flush();
//    }
//
//    public ObjectInputStream getObjectInputStream() throws IOException {
//        return objectInputStream!=null?objectInputStream:new ObjectInputStream(localSocket.getInputStream());
//    }
//    public String getRemoteIpAddress(){
//        return ((InetSocketAddress)(localSocket.getRemoteSocketAddress())).getHostString();
//    }
//}
//package lk.ijse.dep11.users;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class User {
    private Socket localSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public User(Socket localSocket) throws IOException {
        this.localSocket = localSocket;

        /* Before we start to read from the ObjectInputStream from the client side
         * we need to setup the ObjectOutputStream first from the server side.
         * Otherwise, client side will block when it tries to construct the ObjectInputStream */
        objectOutputStream = new ObjectOutputStream(localSocket.getOutputStream());
        objectOutputStream.flush();
    }

    public Socket getLocalSocket() {
        return localSocket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() throws IOException {

        /* Let's return a single instance of objectInputStream every time */
        return objectInputStream != null ? objectInputStream :
                (objectInputStream = new ObjectInputStream(localSocket.getInputStream()));
    }

    public String getRemoteIpAddress(){
        return ((InetSocketAddress)(localSocket.getRemoteSocketAddress())).getHostString();
    }
}
