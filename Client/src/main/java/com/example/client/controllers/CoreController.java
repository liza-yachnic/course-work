package com.example.client.controllers;

import com.example.client.ClientApplication;
import com.example.client.utils.InputDialog;
import com.example.client.utils.animations.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Connector;

import java.io.IOException;
import java.util.Objects;

public class CoreController {

    Logger logger = LogManager.getLogger(CoreController.class);
    public static Connector connector;

    public static User client;

    public TextField loginField;
    public Tooltip loginToolTip;
    public PasswordField passwordField;
    public Tooltip passToolTip;
    public Button authSignInButton;
    public Tooltip addTourToolTip;
    public Button loginSignUpButton;

    static {
        connector = new Connector("localhost", 8888);
    }

    public void openNewScene(String window, String title) {
        try {
            loginSignUpButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApplication.class.getResource(window));
            loader.load();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("MyTravel App | " + title);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(Objects.requireNonNull(ClientApplication.class.getResourceAsStream("images/map-icon.png"))));
            stage.showAndWait();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public void getOpenSignUp(ActionEvent actionEvent) {
        new InputDialog(actionEvent, "views/sign-up-view.fxml", 678, 400);
    }

    @FXML
    void initialize() {
        authSignInButton.setTooltip(addTourToolTip);
        passwordField.setTooltip(passToolTip);
        loginField.setTooltip(loginToolTip);

        authSignInButton.setOnAction(actionEvent -> {
            String login = loginField.getText().trim();
            String pass = passwordField.getText().trim();

            if (login.isEmpty()) {
                Shake loginShake = new Shake(loginField);
                loginShake.shake();
            } else if (pass.isEmpty()) {
                Shake passShake = new Shake(passwordField);
                passShake.shake();
            } else {
                String passHash = DigestUtils.sha256Hex(pass);
                try {
                    connector.writeLine("signIn");
                    connector.writeLine(login);
                    connector.writeLine(passHash);

                    String isStableConnectedStr = connector.readLine();
                    String isAdminOrClientStr = connector.readLine();

                    if (isStableConnectedStr.equals("true")) {
                        if (isAdminOrClientStr.equals("admin")) {
                            logger.log(Level.INFO, "Админ вошёл в учётную запись");
                            openNewScene("views/admin-view.fxml", "Панель администратора");
                        } else if (isAdminOrClientStr.equals("client")) {
                            client = (User) connector.readObj();

                            logger.log(Level.INFO, String.format("Пользователь \"%s\" прошёл авторизацию", client.getLogin()));
                            openNewScene("views/client-view.fxml", "Панель пользователя");
                        } else {
                            loginField.setText("");
                            passwordField.setText("");

                            logger.log(Level.WARN, String.format("Пользователь \"%s\" не найден", login));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.ERROR, e.getMessage());

                    loginField.setText("");
                    passwordField.setText("");
                }
            }
        });
    }
}
