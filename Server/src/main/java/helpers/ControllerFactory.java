package helpers;

import controllers.AdminController;
import controllers.ClientController;

public class ControllerFactory {
    public static IController getType(String type) {
        return switch (type) {
            case "admin" -> new AdminController();
            case "client" -> new ClientController();
            default -> throw new RuntimeException();
        };
    }
}
