package connection;


import Protocol.Message;
import Protocol.MessageManager;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
import exceptions.ClientConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientConnectionSingleton {
    private static ClientConnectionSingleton instance;
    private static Socket socket;
    private static final int PORT = 8888;
    private static InputStream inputStream;

    private ClientConnectionSingleton() {
    }

    // static block initialization for exception handling
    static {
        try {
            instance = new ClientConnectionSingleton();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static ClientConnectionSingleton getInstance() throws ClientConnectionException {
        init();
        return instance;
    }

    private static void init() throws ClientConnectionException {
        try {
            socket =  new Socket("localhost", 8888);
            inputStream = socket.getInputStream();
        } catch (IOException ex) {
            throw new ClientConnectionException("Couldn't connect due to:" + ex.getMessage());
        }
    }

    /**
     * необходимо отправить ответ
     **/
    public Message listenForMessages() throws IOException, MismatchedClassException, ProtocolVersionException {
        return MessageManager.readMessage(socket.getInputStream());
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Socket getSocket() {
        return socket;
    }
}
