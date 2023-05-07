package controllers;

import db.IHandler;
import db.users.UsersHandler;
import helpers.ControllerFactory;
import helpers.IController;
import models.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Connector;

import java.net.ServerSocket;
import java.util.ArrayList;

public class CoreController {
    Logger logger = LogManager.getLogger(CoreController.class);
    private static int countClient = 0;
    public static Connector connector;

    public CoreController(ServerSocket serverSocket) {
        connector = new Connector(serverSocket);
        countClient++;
    }

    public void work() {
        try {
            IHandler usersHandler = new UsersHandler();
            System.out.println("User connector --> " + countClient);

            while (true) {
                String req = connector.readLine();
                switch (req) {
                    case "signIn" -> {
                        String login = connector.readLine();
                        String passwordHash = connector.readLine();

                        ArrayList<User> clients = (ArrayList<User>) usersHandler.getList().clone();
                        for (User client : clients) {
                            if (passwordHash.equals(client.getPasswordHash()) && login.equals(client.getLogin())) {
                                System.out.println(client);
                                connector.writeLine("true");

                                if (client.getIsAdmin()) {
                                    connector.writeLine("admin");
                                    IController iController = ControllerFactory.getType("admin");
                                    iController.start();

                                } else {
                                    System.out.println(client);
                                    connector.writeLine("client");
                                    connector.writeObj(client);

                                    IController iController = ControllerFactory.getType("client");
                                    iController.start();
                                }

                                countClient--;
                                return;
                            }
                        }
                        connector.writeLine("false");
                        connector.writeLine("false");
                    }
                    case "signUp" -> {
                        if (usersHandler.addObj(connector.readObj())) {
                            connector.writeLine("true");
                        } else {
                            connector.writeLine("false");
                        }
                    }
                    default -> logger.log(Level.ERROR, "class ServerController switch(connector.readLine()) error");
                }
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

}
