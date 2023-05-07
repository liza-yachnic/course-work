package com.example.client.controllers;

import com.example.client.utils.animations.Shake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Tour;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Check;
import utils.Connector;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTourController {
    Logger logger = LogManager.getLogger(AddTourController.class);

    private final Connector connector = CoreController.connector;

    @FXML
    private Button addTourPaneBtn;

    @FXML
    private Label flagTourAddLabel;

    @FXML
    private TextField countryNameField;

    @FXML
    private TextField cityNameField;

    @FXML
    private TextField priceTourField;

    @FXML
    private TextField durationTourField;

    @FXML
    private TextField tourNameField;

    @FXML
    private DatePicker dateTourField;

    @FXML
    private ComboBox<String> boxTourType;

    ObservableList<String> boxTourTypeObservableList = FXCollections.observableArrayList(
            "Отдых на море", "Развлечение и отдых", "Экскурсионный", "Спорт, походы, экстрим",
            "Дайвинг", "Сафари, охота, рыбалка", "Экзотические", "Круизный",
            "Свадебный", "Бизнес-тур", "Детские");

    @FXML
    void initialize() {
        boxTourType.setItems(boxTourTypeObservableList);

        addTourPaneBtn.setOnAction(ActionEvent -> {
            String price = priceTourField.getText().trim();
            String duration = durationTourField.getText().trim();
            LocalDate dateTour = dateTourField.getValue();

            if (!Check.isFloat(price)) {
                Shake shakePrice = new Shake(priceTourField);
                shakePrice.shake();
            } else if (!Check.isNumber(duration)) {
                Shake shakeDuration = new Shake(durationTourField);
                shakeDuration.shake();
            } else if (dateTour.toString().isEmpty()) {
                Shake shake = new Shake(dateTourField);
                shake.shake();
            } else {
                flagTourAddLabel.setText("");
                Tour tour = new Tour();

                tour.setPrice(Float.parseFloat(price));
                tour.setDuration(duration);
                tour.setTourDate(dateTour.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
                tour.setTourName(tourNameField.getText().trim());
                tour.setTourType(boxTourType.getValue());
                tour.setCountryName(countryNameField.getText().trim());
                tour.setCityName(cityNameField.getText().trim());

                int a = 10;
                int b = 10000;
                int randomCode = a + (int) (Math.random() * ((b - a) + 1));

                tour.setTourCode("T#" + randomCode);

                try {
                    connector.writeLine("add");
                    connector.writeLine("addTour");
                    connector.writeObj(tour);
                    String flagAddTout = connector.readLine();
                    if (flagAddTout.equals("true")) {
                        flagTourAddLabel.setText("Тур успешно добавлен!");
                    } else {
                        flagTourAddLabel.setText("Не удалось добавить тур!");
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        });
    }

    @FXML
    void closeTourPane(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}