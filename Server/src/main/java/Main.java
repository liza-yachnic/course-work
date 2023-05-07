import java.io.IOException;
import java.net.ServerSocket;

import controllers.CoreController;


public class Main {
    public static int port = 8888;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                CoreController CoreController = new CoreController(serverSocket);
                new Thread(CoreController::work).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
