package com.example.client.utils;

import com.example.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class InputDialog {

    public InputDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/sign-up-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Новое окно");
        stage.setScene(new Scene(root, 300, 300));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    public InputDialog(ActionEvent actionEvent, String path) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Новое окно");
        stage.setScene(new Scene(root, 300, 300));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    public InputDialog(ActionEvent actionEvent, String path, int h, int w) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ClientApplication.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("MyTravel App | Новое окно");
        stage.setScene(new Scene(root, h, w));
        stage.getIcons().add(new Image(Objects.requireNonNull(ClientApplication.class.getResourceAsStream("images/map-icon.png"))));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }
}