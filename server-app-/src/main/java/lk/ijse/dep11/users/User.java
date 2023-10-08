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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Socket localSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public User(Socket localSocket) throws IOException {
        this.localSocket = localSocket;
        OutputStream os = localSocket.getOutputStream();
        objectOutputStream=new ObjectOutputStream(os);
        objectOutputStream.flush();
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        return objectInputStream!=null?objectInputStream:new ObjectInputStream(localSocket.getInputStream());
    }
    public String getRemoteIpAddress(){
        return ((InetSocketAddress)(localSocket.getRemoteSocketAddress())).getHostString();
    }
}
