package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("views/core-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("MyTravel App | Главное окно");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(ClientApplication.class.getResourceAsStream("images/map-icon.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}