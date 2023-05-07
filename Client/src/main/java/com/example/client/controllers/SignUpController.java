package com.example.client.controllers;

import com.example.client.utils.animations.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Check;
import utils.Connector;

import java.io.IOException;

public class SignUpController {
    Logger logger = LogManager.getLogger(SignUpController.class);

    private static final Connector connector = CoreController.connector;
    public TextField fioField;
    public TextField phoneNumberField;
    public PasswordField passwordField;
    public Button signUpButton;
    public TextField mailField;
    public TextField loginField;
    public TextField passportIdField;
    public Label errorLabel;
    public Button closeSignUpButton;

    public void clickOnSingUpBtn(ActionEvent actionEvent) {
        String fio = fioField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();
        String mail = mailField.getText().trim();
        String login = loginField.getText().trim();
        String passportId = passportIdField.getText().trim();

        if (phoneNumber.isEmpty() || !Check.isPhoneNumber(phoneNumber)) {
            Shake phoneNumberShake = new Shake(phoneNumberField);
            phoneNumberShake.shake();
        } else if (fio.isEmpty()) {
            Shake fioShake = new Shake(fioField);
            fioShake.shake();
        } else if (password.isEmpty()) {
            Shake passwordShake = new Shake(passwordField);
            passwordShake.shake();
        } else if (mail.isEmpty() || !Check.isEmail(mail)) {
            Shake mailShake = new Shake(mailField);
            mailShake.shake();
        } else if (login.isEmpty()) {
            Shake loginShake = new Shake(loginField);
            loginShake.shake();
        } else if (passportId.isEmpty()) {
            Shake passportIdShake = new Shake(passportIdField);
            passportIdShake.shake();
        } else {
            System.out.println("Try to create user");
            try {
                User user = new User();
                int randomCode = (100 + (int) (Math.random() * ((1000 - 100) + 1)));
                String clientCode = "#" + randomCode;
                String passwordHash = DigestUtils.sha256Hex(password);

                user.setFIO(fio);
                user.setClientCode(clientCode);
                user.setMobileNumber(phoneNumber);
                user.setPasswordHash(passwordHash);
                user.setMail(mail);
                user.setLogin(login);
                user.setPassportId(passportId);
                user.setIsAdmin(false);

                logger.log(Level.INFO, user);

                connector.writeLine("signUp");
                connector.writeObj(user);
                String isSuccessStr = connector.readLine();

                if (isSuccessStr.equals("false")) {
                    errorLabel.setText("Ошибка при регистрации. Обратитесь к администратору");
                } else {
                    errorLabel.setText("Вы успешно зарегистрировались!");
                }
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public void clickOnCloseSignUpBtn(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
    }
}
