package com.example.client.controllers;

import com.example.client.utils.animations.Shake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Ticket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Check;
import utils.Connector;

import java.io.IOException;


public class CheckAndCreateTicketController {
    Logger logger = LogManager.getLogger(AddTourController.class);
    private final Connector connector = CoreController.connector;
    @FXML
    private Button createTicketBtn;
    @FXML
    private TextField idOrderField;
    @FXML
    private TextField departurePointField;
    @FXML
    private Label errorIdOrderLabel;
    @FXML
    private ComboBox<String> boxTransportType;
    ObservableList<String> list = FXCollections.observableArrayList("Самолёт", "Автобус", "Поезд", "Корабль");

    @FXML
    void initialize() {
        boxTransportType.setItems(list);
        createTicketBtn.setOnAction(ActionEvent -> {
            String idOrder = idOrderField.getText().trim();
            String transportType = boxTransportType.getValue();
            String departurePoint = departurePointField.getText().trim();

            if (!Check.isNumber(idOrder)) {
                Shake shake = new Shake(idOrderField);
                shake.shake();
            } else if (transportType.isEmpty()) {
                Shake shake = new Shake(boxTransportType);
                shake.shake();
            } else if (departurePoint.isEmpty()) {
                Shake shake = new Shake(departurePointField);
                shake.shake();
            } else {
                errorIdOrderLabel.setText("");

                try {

                    Ticket ticket = new Ticket();
                    ticket.setTransportType(transportType);
                    ticket.setDeparturePoint(departurePoint);

                    int a = 10;
                    int b = 10000;
                    int randomCode = a + (int) (Math.random() * ((b - a) + 1));
                    ticket.setTicketCode("Ticket#" + randomCode);

                    connector.writeLine("add");
                    connector.writeLine("addTicket");
                    connector.writeLine(idOrder);
                    connector.writeObj(ticket);
                    String flagAddTicketAndCheckOrder = connector.readLine();

                    System.out.println(flagAddTicketAndCheckOrder);
                    if (flagAddTicketAndCheckOrder.equals("true")) {
                        errorIdOrderLabel.setText("Билет создан!");
                    } else {
                        errorIdOrderLabel.setText("Не удалось создать билет!");
                    }

                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        });
    }

    @FXML
    void closeCreateTicketPane(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}