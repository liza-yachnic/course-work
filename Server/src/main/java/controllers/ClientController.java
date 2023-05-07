package controllers;

import models.Order;
import db.IHandler;
import db.orders.OrdersHandler;
import db.tickets.TicketsHandler;
import db.tours.ToursHandler;
import db.users.UsersHandler;
import helpers.IController;
import models.Tour;
import models.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Connector;

import java.io.IOException;
import java.util.ArrayList;

public class ClientController implements IController {
    
    Logger logger = LogManager.getLogger(ClientController.class);
    
    public Connector connect = CoreController.connector;
    private final IHandler usersHandler = new UsersHandler();
    private final IHandler toursHandler = new ToursHandler();
    private final IHandler ordersHandler = new OrdersHandler();
    private final IHandler ticketsHandler = new TicketsHandler();
    
    @Override
    public void saveDate(String msg) throws IOException, ClassNotFoundException {
        if ("orderTour".equals(msg)) {
            String tourCode = connect.readLine();
            User user = (User) connect.readObj();
            System.out.println("order user -> " + user);
            if (makeOrderTour(tourCode, user, toursHandler.getList())) {
                connect.writeLine("true");
            } else {
                connect.writeLine("false");
            }
        } else {
            logger.log(Level.ERROR, "клиент сервер заказ тура case default ");
        }
    }

    @Override
    public void editDate(String msg) throws IOException, ClassNotFoundException {

    }

    @Override
    public void deleteData(String msg) throws IOException, ClassNotFoundException {
        if ("deleteOrder".equals(msg)) {
            boolean result = false;
            String id = connect.readLine();
            int orderId = Integer.parseInt(id);

            ArrayList<Order> orders = (ArrayList<Order>) ordersHandler.getList().clone();
            for (Order o : orders) {
                if (orderId == o.getId()) {
                    result = ordersHandler.deleteObj(o);
                    break;
                }
            }

            connect.writeLine(Boolean.toString(result));
        } else {
            logger.log(Level.ERROR,"user server do not response deleteOrder case default");
        }
    }

    @Override
    public void getDate(String msg) throws IOException, ClassNotFoundException {
        switch (msg) {
            case "viewUser" -> {
                connect.writeObjList(usersHandler.getList());
            }
            case "viewTicket" -> {
                connect.writeObjList(ticketsHandler.getList());
            }
            case "viewTour" -> {
                connect.writeObjList(toursHandler.getList());
            }
            case "viewOrder" -> {
                final String userCode = connect.readLine();
                boolean flagOrder = checkOrderClient(userCode, ordersHandler.getList());

                if (flagOrder) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }

                if (flagOrder) {
                    ArrayList<Object> objects = getClientOrder(userCode, ordersHandler.getList());
                    connect.writeObjList(objects);
                }
            }
            default -> {
                logger.log(Level.ERROR,"user server view default case ");
            }
        }
    }

    @Override
    public void start() {
        try {
            while (true) {
                String msg = connect.readLine();
                switch (msg) {
                    case "view" -> {
                        this.getDate(connect.readLine());
                    }
                    case "add" -> {
                        this.saveDate(connect.readLine());
                    }
                    case "delete" -> {
                        this.deleteData(connect.readLine());
                    }
                    case "edit" -> {
                        this.editDate(connect.readLine());
                    }
                    case "close" -> {
                        connect.close();
                        return;
                    }
                    default -> {
                        logger.log(Level.ERROR,"не найден router в ClientController");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    private boolean makeOrderTour(String tourCode, User user, ArrayList<Object> objects) {
        ArrayList<Tour> tours = (ArrayList<Tour>) toursHandler.getList().clone();
        if (checkTour(tourCode, tours)) {
            for (Tour t : tours) {
                if (tourCode.equals(t.getTourCode())) {
                    Order order = new Order();
                    order.setClientCode(user.getClientCode());
                    order.setTourCode(t.getTourCode());
                    return ordersHandler.addObj(order);
                }
            }
        }
        return false;
    }

    private ArrayList<Object> getClientOrder(String userCode, ArrayList<Object> objects) {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Order> orderArrayList = (ArrayList<Order>) objects.clone();
        for (Order o : orderArrayList) {
            if (userCode.equals(o.getClientCode())) {
                orders.add(o);
            }
        }
        return (ArrayList<Object>) orders.clone();
    }

    private boolean checkTour(String tourCode, ArrayList<Tour> tours) {
        for (Tour t : tours) {
            if (tourCode.equals(t.getTourCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOrderClient(String userCode, ArrayList<Object> objects) {
        ArrayList<Order> orders = (ArrayList<Order>) objects.clone();
        for (Order o : orders) {
            if (userCode.equals(o.getClientCode())) {
                return true;
            }
        }
        return false;
    }
}
