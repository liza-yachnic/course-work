package controllers;

import db.IHandler;
import db.orders.OrdersHandler;
import db.tickets.TicketsHandler;
import db.tours.ToursHandler;
import db.users.UsersHandler;
import helpers.IController;
import models.Order;
import models.Ticket;
import models.Tour;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Connector;

import java.io.IOException;
import java.util.ArrayList;

public class AdminController implements IController {
    Logger logger = LogManager.getLogger(ClientController.class);

    public Connector connect = CoreController.connector;
    private final IHandler usersHandler = new UsersHandler();
    private final IHandler toursHandler = new ToursHandler();
    private final IHandler ordersHandler = new OrdersHandler();
    private final IHandler ticketsHandler = new TicketsHandler();

    @Override
    public void saveDate(String msg) throws IOException, ClassNotFoundException {
        switch (msg) {
            case "addUser" -> {
                boolean flagAddClient = usersHandler.addObj(connect.readObj());
                if (flagAddClient) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }
            }
            case "addTour" -> {
                boolean flagAddTour = toursHandler.addObj(connect.readObj());
                if (flagAddTour) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }
            }
            case "addTicket" -> {
                String idOrder = connect.readLine();
                Ticket ticket = (Ticket) connect.readObj();
                boolean flag = makeOrder(Integer.parseInt(idOrder), ticket);
                if (flag) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }
            }
        }
    }

    @Override
    public void editDate(String msg) throws IOException, ClassNotFoundException {
        switch (msg) {
            case "editUser" -> {
                if (usersHandler.editObj(connect.readObj())) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }
            }
            case "editTour" -> {
                Object ob = connect.readObj();
                if (toursHandler.editObj(ob)) {
                    connect.writeLine("true");
                } else {
                    connect.writeLine("false");
                }
            }
        }
    }

    @Override
    public void deleteData(String msg) throws IOException, ClassNotFoundException {
        switch (msg) {
            case "deleteUser" -> {
                boolean result = false;
                String login = connect.readLine();
                String userCode = connect.readLine();

                ArrayList<User> users = (ArrayList<User>) usersHandler.getList().clone();

                for (User user : users) {
                    if (login.equals(user.getLogin()) && userCode.equals(user.getClientCode())) {
                        result = usersHandler.deleteObj(user);
                        break;
                    }
                }

                connect.writeLine(Boolean.toString(result));
            }
            case "deleteTour" -> {
                boolean result = false;
                String id = connect.readLine();
                int idTour = Integer.parseInt(id);

                ArrayList<Tour> tours = (ArrayList<Tour>) toursHandler.getList().clone();
                for (Tour tour : tours) {
                    if (idTour == tour.getId()) {
                        result = toursHandler.deleteObj(tour);
                        break;
                    }
                }

                connect.writeLine(Boolean.toString(result));
            }
            case "ticketDelete" -> {
                boolean result = false;
                String id = connect.readLine();
                int idTicket = Integer.parseInt(id);

                ArrayList<Ticket> tickets = (ArrayList<Ticket>) ticketsHandler.getList().clone();
                for (Ticket ticket : tickets) {
                    if (idTicket == ticket.getId()) {
                        result = ticketsHandler.deleteObj(ticket);
                        break;
                    }
                }
                
                connect.writeLine(Boolean.toString(result));
            }
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
                connect.writeObjList(ordersHandler.getList());
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
                    case "search" -> {
                        String req = connect.readLine();
                        this.search(req);
                    }
                    case "edit" -> {
                        this.editDate(connect.readLine());
                    }
                    case "close" -> {
                        connect.close();
                        return;
                    }
                    default -> {
                        logger.log(Level.ERROR, "не найден router в AdminController");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }


    private boolean makeOrder(int idOrder, Ticket ticket) {
        boolean flagAddTicket;
        boolean flagClient;
        boolean flagTour;

        ArrayList<Tour> tourArrayList = (ArrayList<Tour>) toursHandler.getList().clone();
        ArrayList<Order> orderArrayList = (ArrayList<Order>) ordersHandler.getList().clone();
        ArrayList<User> userArrayList = (ArrayList<User>) usersHandler.getList().clone();

        for (Order o : orderArrayList) {
            if (idOrder == o.getId()) {
                flagClient = checkClient(o.getClientCode(), userArrayList);
                flagTour = checkTour(o.getTourCode(), tourArrayList);
                if (flagClient && flagTour) {
                    ticket.setUserCode(o.getClientCode());
                    for (Tour t : tourArrayList) {
                        if (o.getTourCode().equals(t.getTourCode())) {
                            ticket.setDepartureDate(t.getTourDate());
                            ticket.setArrivalPoint(t.getCountryName() + "-" + t.getCityName());
                            flagAddTicket = ticketsHandler.addObj(ticket);
                            ordersHandler.deleteObj(o);
                            return flagAddTicket;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void search(String msg) throws IOException {
        switch (msg) {
            case "searchUser" -> {
                String fio = connect.readLine();
                String login = connect.readLine();

                int counter = 0;
                ArrayList<User> users = (ArrayList<User>) usersHandler.getList().clone();
                for (User user : users) {
                    if (fio.equals(user.getFIO()) && login.equals(user.getLogin())) {
                        ++counter;
                        connect.writeLine("true");
                        connect.writeObj(user);
                    }
                }
                if (counter == 0) {
                    connect.writeLine("false");
                }
            }
            case "searchClient" -> {
                final String userCode = connect.readLine();
                if (checkOrderClientCode(userCode)) {
                    ArrayList<Object> objects = getSearchOrders(userCode);
                    connect.writeLine("true");
                    connect.writeObjList(objects);
                } else {
                    connect.writeLine("false");
                }
            }
            case "searchTour" -> {
                final String tourCode = connect.readLine();
                if (checkOrderTourCode(tourCode)) {
                    ArrayList<Object> objects = getSearchOrdersTour(tourCode);
                    connect.writeLine("true");
                    connect.writeObjList(objects);
                } else {
                    connect.writeLine("false");
                }
            }
        }
    }

    private boolean checkOrderTourCode(String tourCode) {
        ArrayList<Order> orders = (ArrayList<Order>) ordersHandler.getList().clone();
        for (Order o : orders) {
            if (tourCode.equals(o.getTourCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOrderClientCode(String userCode) {
        ArrayList<Order> orders = (ArrayList<Order>) ordersHandler.getList().clone();
        for (Order o : orders) {
            if (userCode.equals(o.getClientCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkClient(String userCode, ArrayList<User> users) {
        for (User user : users) {
            if (userCode.equals(user.getClientCode())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTour(String tourCode, ArrayList<Tour> tours) {
        for (Tour t : tours) {
            if (tourCode.equals(t.getTourCode())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Object> getSearchOrders(String userCode) {
        ArrayList<Order> orders = (ArrayList<Order>) ordersHandler.getList().clone();
        ArrayList<Object> objects = new ArrayList<>();
        for (Order o : orders) {
            if (userCode.equals(o.getClientCode())) {
                objects.add(o);
            }
        }
        return objects;
    }

    private ArrayList<Object> getSearchOrdersTour(String tourCode) {
        ArrayList<Order> orders = (ArrayList<Order>) ordersHandler.getList().clone();
        ArrayList<Object> objects = new ArrayList<>();
        for (Order o : orders) {
            if (tourCode.equals(o.getTourCode())) {
                objects.add(o);
            }
        }
        return objects;
    }
}
