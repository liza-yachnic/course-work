package utils;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Connector implements Closeable {
    Logger logger = LogManager.getLogger(Connector.class);
    
    private Socket socket;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    public Connector(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.bufferedReader = createReader();
            this.bufferedWriter = createWriter();
            this.objectOutputStream = createOutputStreamer();
            this.objectInputStream = createInputStreamer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connector(ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
            this.bufferedReader = createReader();
            this.bufferedWriter = createWriter();
            this.objectOutputStream = createOutputStreamer();
            this.objectInputStream = createInputStreamer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private BufferedWriter createWriter() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private ObjectOutputStream createOutputStreamer() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    private ObjectInputStream createInputStreamer() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    public void writeLine(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public Object readObj() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    public void writeObj(Object obj) throws IOException {
        objectOutputStream.writeObject(obj);
    }

    public ArrayList<Object> readObjList() throws IOException, ClassNotFoundException {
        return (ArrayList<Object>) objectInputStream.readObject();
    }

    public void writeObjList(ArrayList<Object> objList) throws IOException {
        objectOutputStream.writeObject(objList);
        objectOutputStream.flush();
    }

    public void clearConnector() throws IOException {
        objectOutputStream.flush();
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @Override
    public void close() {
        try {
            bufferedReader.close();
            bufferedWriter.close();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

}
