package com.example.client.controllers;

import com.example.client.utils.animations.Shake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Order;
import models.Ticket;
import models.Tour;
import models.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Check;
import utils.Connector;

import java.io.IOException;
import java.util.ArrayList;

public class ClientController {

    Logger logger = LogManager.getLogger(ClientController.class);
    private Connector connector = CoreController.connector;
    private User profile = new User();

    public TabPane mainTabPane;
    public Tab toursTab;
    public Button toursViewBtn;
    public Button reserveTourBtn;
    public TextField inputTourCodeField;
    public Label errorReserveTourLabel;
    public TableView<Tour> viewToursTab;
    public Tab userProfileTab;
    public TextField userLoginField;
    public TextField userFioField;
    public TextField userMailField;
    public TextField userCodeField;
    public TextField userPasswordField;
    public TextField userPassportIdField;
    public TextField userPhoneNumberField;
    public Tab ticketsTab;
    public TableView<Ticket> ticketTableView;
    public Button ticketsViewBtn;
    public Tab ordersTab;
    public TableView<Order> ordersTableView;
    public Button ordersViewBtn;
    public Button cancelOrderBtn;
    public TextField cancelOrderIdField;
    public Label errorOrderLabel;
    public AnchorPane myOrdersBtn;
    public Button toursBtn;
    public Button myTicketBtn;
    public Button profileBtn;
    public Button myOrderBtn;
    public Button closeBtn;
    public Label activeMenuLabel;

    @FXML
    void initialize() {
        this.profile = CoreController.client;

        closeBtn.setOnAction(ActionEvent -> {
            try {
                connector.writeLine("close");
                viewToursTab = null;
                ticketTableView = null;
                connector.close();
                connector = null;
                profile = null;

                System.exit(2);
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        toursBtn.setOnAction(ActionEvent -> {
            errorReserveTourLabel.setText("");
            activeMenuLabel.setText("Туры");
            mainTabPane.getSelectionModel().select(toursTab);
        });

        myTicketBtn.setOnAction(ActionEvent -> {
            activeMenuLabel.setText("Билеты");
            mainTabPane.getSelectionModel().select(ticketsTab);
        });

        myOrderBtn.setOnAction(ActionEvent -> {
            errorOrderLabel.setText("");
            cancelOrderIdField.setText("");
            activeMenuLabel.setText("Заказы");
            mainTabPane.getSelectionModel().select(ordersTab);
        });

        profileBtn.setOnAction(ActionEvent -> {
            errorReserveTourLabel.setText("");
            activeMenuLabel.setText("Профиль");
            mainTabPane.getSelectionModel().select(userProfileTab);
            userFioField.setText(profile.getFIO());
            userCodeField.setText(profile.getClientCode());
            userPassportIdField.setText(profile.getPassportId());
            userMailField.setText(profile.getMail());
            userPhoneNumberField.setText(profile.getMobileNumber());
            userLoginField.setText(profile.getLogin());
            userPasswordField.setText("***********");
        });

        reserveTourBtn.setOnAction(ActionEvent -> {
            String inputTourCode = inputTourCodeField.getText().trim();

            if (inputTourCode.isEmpty()) {
                Shake shakeOrderTour = new Shake(inputTourCodeField);
                shakeOrderTour.shake();
                errorReserveTourLabel.setText("Введите код тура!");
            } else {
                try {
                    User client = new User();
                    client.setClientCode(profile.getClientCode());
                    client.setId(profile.getId());
                    client.setIsAdmin(profile.getIsAdmin());
                    client.setPasswordHash(profile.getPasswordHash());
                    client.setLogin(profile.getLogin());
                    client.setMail(profile.getMail());
                    client.setFIO(profile.getFIO());
                    client.setPassportId(profile.getPassportId());

                    connector.writeLine("add");
                    connector.writeLine("orderTour");
                    connector.writeLine(inputTourCode);
                    connector.writeObj(client);
                    String flagOrderAddOrNot = connector.readLine();

                    if (flagOrderAddOrNot.equals("true")) {
                        errorReserveTourLabel.setText("Тур забронирован");
                    } else if (flagOrderAddOrNot.equals("false")) {
                        errorReserveTourLabel.setText("Тур не забронирован");
                    } else {
                        errorReserveTourLabel.setText("Ошибка резервации");
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    inputTourCodeField.setText("");
                }
            }
        });

        ticketsViewBtn.setOnAction(ActionEvent -> {
            try {
                connector.writeLine("view");
                connector.writeLine("viewTicket");
                ArrayList<Ticket> ticketArrayList = (ArrayList<Ticket>) connector.readObjList().clone();
                ArrayList<Ticket> tickets = new ArrayList<>();

                String clientCode = profile.getClientCode();

                for (Ticket t : ticketArrayList) {
                    if (clientCode.equals(t.getUserCode())) {
                        tickets.add(t);
                    }
                }

                if (tickets.isEmpty()) {
                    ticketTableView.getItems().clear();
                } else {
                    ObservableList<Ticket> observableList = FXCollections.observableArrayList(tickets);
                    ticketTableView.setItems(observableList);
                    ticketTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                    ticketTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("ticketCode"));
                    ticketTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("userCode"));
                    ticketTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("transportType"));
                    ticketTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("departurePoint"));
                    ticketTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("arrivalPoint"));
                    ticketTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory("departureDate"));
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        toursViewBtn.setOnAction(ActionEvent -> {
            try {
                connector.writeLine("view");
                connector.writeLine("viewTour");
                ArrayList<Tour> tourArrayList = (ArrayList<Tour>) connector.readObjList().clone();
                ObservableList<Tour> observableList = FXCollections.observableArrayList(tourArrayList);
                viewToursTab.setItems(observableList);
                viewToursTab.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                viewToursTab.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("tourName"));
                viewToursTab.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("tourType"));
                viewToursTab.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("tourCode"));
                viewToursTab.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("countryName"));
                viewToursTab.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("cityName"));
                viewToursTab.getColumns().get(6).setCellValueFactory(new PropertyValueFactory("price"));
                viewToursTab.getColumns().get(7).setCellValueFactory(new PropertyValueFactory("duration"));
                viewToursTab.getColumns().get(8).setCellValueFactory(new PropertyValueFactory("tourDate"));
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        ordersViewBtn.setOnAction(ActionEvent -> {
            try {
                connector.writeLine("view");
                connector.writeLine("viewOrder");
                connector.writeLine(profile.getClientCode());
                final String flagOrder = connector.readLine();
                if (flagOrder.equals("true")) {
                    ArrayList<Order> orders = (ArrayList<Order>) connector.readObjList().clone();
                    ObservableList<Order> orderObservableList = FXCollections.observableArrayList(orders);
                    ordersTableView.setItems(orderObservableList);
                    ordersTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                    ordersTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("clientCode"));
                    ordersTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("tourCode"));
                } else if (flagOrder.equals("false")) {
                    ordersTableView.getItems().clear();
                    errorOrderLabel.setText("У вас нет заказов");
                } else {
                    ordersTableView.getItems().clear();
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        cancelOrderBtn.setOnAction(ActionEvent -> {
            String cancelOrderId = cancelOrderIdField.getText().trim();

            if (!Check.isNumber(cancelOrderId)) {
                Shake shakeOrderId = new Shake(cancelOrderIdField);
                shakeOrderId.shake();
                cancelOrderIdField.setText("");
                errorOrderLabel.setText("Введите id заказа ");
            } else {
                try {
                    connector.writeLine("view");
                    connector.writeLine("viewOrder");
                    connector.writeLine(profile.getClientCode());
                    final String flagOrder = connector.readLine();
                    if (flagOrder.equals("true")) {

                        connector.readObjList();
                        connector.writeLine("delete");
                        connector.writeLine("deleteOrder");
                        connector.writeLine(cancelOrderId);
                        String msg = connector.readLine();

                        if (msg.equals("true")) {
                            errorOrderLabel.setText("Заказ успешно удалён!");
                        } else {
                            cancelOrderIdField.setText("");
                            errorOrderLabel.setText("Не удалось удалить заказ!");
                        }
                    } else {
                        cancelOrderIdField.setText("");
                        errorOrderLabel.setText("У Вас отсутствует данный заказ.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    cancelOrderIdField.setText("");
                }
            }
        });
    }

}