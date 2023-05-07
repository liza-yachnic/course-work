package com.example.client.controllers;

import com.example.client.utils.InputDialog;
import com.example.client.utils.animations.Shake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Order;
import models.Ticket;
import models.Tour;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Check;
import utils.Connector;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminController {
    Logger logger = LogManager.getLogger(AdminController.class);

    private final Connector connector = CoreController.connector;

    private User userSearch;

    private final String adminRole = "Админ";
    private final String userRole = "Пользователь";
    private final ObservableList<String> userRoles = FXCollections.observableArrayList(adminRole, userRole);

    private String flagSearchUser = "false";
    public TabPane mainTabPane;
    public Tab toursTab;
    public TableView<Tour> viewToursTab;
    public Button addTourBtn;
    public Button toursViewBtn;
    public Button deleteTourBtn;

    public Label errorTourDeleteId;
    public TextField deleteIdTourField;
    public Button editTourBtn;
    public TextField countryTourEditField;
    public TextField searchIdTourEditFiled;
    public TextField cityTourEditField;
    public TextField durationTourEditField;
    public TextField priceTourEditField;
    public TextField nameTourEditField;
    public DatePicker dateTourEditField;
    public Button searchTourIdBtn;
    public TextField idTourEditFiled;
    public TextField codeTourEditField;
    public TextField typeTourEditField;
    public Label errorMsgEditTourLabel;
    public Tab usersTab;
    public TableView<User> usersTableView;
    public Button viewUsersBtn;
    public Button deleteUsersBtn;
    public TextField loginDeleteField;
    public TextField clientCodeDeleteField;
    public TextField signUpSearchFIOField;
    public TextField signUpSearchLoginField;
    public Button searchBtn;
    public TextField signUpEditPasswordField;
    public ChoiceBox<String> signUpEditIsAdminField;
    public TextField signUpEditMailField;
    public TextField signUpEditFIOField;
    public TextField signUpEditClientCodeField;
    public TextField signUpEditPassportIdField;
    public TextField signUpEditMobileNumberField;
    public TextField signUpEditLoginField;
    public Button editBtn;
    public Label errorSearchLabel;
    public Label errorEditLabel;
    public Button addUserBtn;
    public ChoiceBox<String> signUpIsAdminField;
    public TextField signUpPasswordField;
    public TextField signUpMailField;
    public TextField signUpFIOField;
    public TextField signUpClientCodeField;
    public TextField signUpPassportIdField;
    public TextField signUpMobileNumberField;
    public TextField signUpLoginField;
    public Label outPutErrorAddUserLabel;
    public Label errorDeleteUserLabel;
    public Tab ticketsTab;
    public TableView<Ticket> ticketTableView;
    public Button ticketsViewBtn;
    public Button ticketDeleteBtn;
    public TextField ticketIdDeleteField;
    public Label errorTicketDeleteLabel;
    public Tab ordersTab;
    public TableView<Order> ordersTableView;
    public Button checkAndCreateTicketBtn;
    public Button searchOrderClientCodeBtn;
    public TextField searchTourCodeField;
    public TextField searchClientCodeField;
    public Button searchOrderTourCodeBtn;
    public TableView<Order> tabViewOrdersSearch;
    public Label errorOrderLabel;
    public Button ordersViewBtn;
    public Button toursBtn;
    public Button ticketBtn;
    public Button usersBtn;
    public Button orderBtn;
    public Button closeBtn;
    public Label activeMenuLabel;

    @FXML
    void initialize() {
        signUpEditIsAdminField.setItems(userRoles);
        signUpIsAdminField.setItems(userRoles);

        closeBtn.setOnAction(ActionEvent -> {
            try {
                connector.writeLine("close");
                connector.close();
                System.exit(1);
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        usersBtn.setOnAction(actionEvent -> {
            activeMenuLabel.setText("Пользователи");
            mainTabPane.getSelectionModel().select(usersTab);
        });

        toursBtn.setOnAction(actionEvent -> {
            activeMenuLabel.setText("Туры");
            mainTabPane.getSelectionModel().select(toursTab);
        });

        ticketBtn.setOnAction(actionEvent -> {
            activeMenuLabel.setText("Билеты");
            mainTabPane.getSelectionModel().select(ticketsTab);
        });

        orderBtn.setOnAction(ActionEvent -> {
            errorOrderLabel.setText("");
            searchTourCodeField.setText("");
            searchClientCodeField.setText("");
            activeMenuLabel.setText("Заказы");
            mainTabPane.getSelectionModel().select(ordersTab);
        });

        /**Просмотр пользователя*/
        viewUsersBtn.setOnAction(actionEvent -> {
            try {
                connector.writeLine("view");
                connector.writeLine("viewUser");
                ArrayList<User> clientArrayList = (ArrayList<User>) connector.readObjList().clone();
                ObservableList<User> observableList = FXCollections.observableArrayList(clientArrayList);

                usersTableView.setItems(observableList);
                usersTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("login"));
                usersTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("FIO"));
                usersTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("mail"));
                usersTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("clientCode"));
                usersTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("passportId"));
                usersTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("mobileNumber"));
                usersTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory("isAdmin"));
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        /**Добавить пользователя*/
        addUserBtn.setOnAction(actionEvent -> {
            String fio = signUpFIOField.getText().trim();
            String clientCode = signUpClientCodeField.getText().trim();
            String passportId = signUpPassportIdField.getText().trim();
            String mail = signUpMailField.getText().trim();
            String mobileNumber = signUpMobileNumberField.getText().trim();
            String login = signUpLoginField.getText().trim();
            String password = signUpPasswordField.getText().trim();
            String isAdmin = signUpIsAdminField.getValue();

            if (fio.isEmpty()) {
                Shake shakeFIO = new Shake(signUpEditFIOField);
                shakeFIO.shake();
            } else if (clientCode.isEmpty()) {
                Shake shakeClientCode = new Shake(signUpEditClientCodeField);
                shakeClientCode.shake();
            } else if (mobileNumber.isEmpty() || !Check.isPhoneNumber(mobileNumber)) {
                Shake shakeMobileNumber = new Shake(signUpEditMobileNumberField);
                shakeMobileNumber.shake();
            } else if (password.isEmpty()) {
                Shake shakePassword = new Shake(signUpEditPasswordField);
                shakePassword.shake();
            } else if (mail.isEmpty()) {
                Shake shakeMail = new Shake(signUpEditMailField);
                shakeMail.shake();
            } else if (login.isEmpty()) {
                Shake shakeLogin = new Shake(signUpEditLoginField);
                shakeLogin.shake();
            } else if (passportId.isEmpty()) {
                Shake shakePassportId = new Shake(signUpEditPassportIdField);
                shakePassportId.shake();
            } else {
                User client = new User();
                client.setFIO(fio);
                client.setClientCode(clientCode);
                client.setPassportId(passportId);
                client.setMail(mail);
                client.setMobileNumber(mobileNumber);
                client.setLogin(login);
                client.setPasswordHash(DigestUtils.sha256Hex(password));
                client.setIsAdmin(isAdmin.equals(adminRole));

                try {
                    outPutErrorAddUserLabel.setText("");

                    connector.writeLine("add");
                    connector.writeLine("addUser");
                    connector.writeObj(client);
                    String flagAddClient = connector.readLine();

                    if (flagAddClient.equals("true")) {
                        outPutErrorAddUserLabel.setText("Пользователь добавлен!");
                    } else if (flagAddClient.equals("false")) {
                        outPutErrorAddUserLabel.setText("Не удалось добавить пользователя. Возможно он уже существует");
                    } else {
                        outPutErrorAddUserLabel.setText("Ошибка добавления пользователя");
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }

                signUpIsAdminField.setValue(userRole);
                signUpFIOField.setText("");
                signUpMobileNumberField.setText("");
                signUpClientCodeField.setText("");
                signUpPassportIdField.setText("");
                signUpMailField.setText("");
                signUpLoginField.setText("");
                signUpPasswordField.setText("");
            }
        });

        /**Удалить пользователя*/
        deleteUsersBtn.setOnAction(actionEvent -> {
            String loginDelete = loginDeleteField.getText().trim();
            String clientCodeDelete = clientCodeDeleteField.getText().trim();

            if (loginDelete.isEmpty()) {
                Shake shakeLogin = new Shake(loginDeleteField);
                shakeLogin.shake();
            } else if (clientCodeDelete.isEmpty()) {
                Shake shakeClientCode = new Shake(clientCodeDeleteField);
                shakeClientCode.shake();
            } else {
                errorDeleteUserLabel.setText("");

                try {
                    connector.writeLine("delete");
                    connector.writeLine("deleteUser");
                    connector.writeLine(loginDelete);
                    connector.writeLine(clientCodeDelete);
                    String flagIsDeleted = connector.readLine();
                    System.out.println(flagIsDeleted);

                    if (flagIsDeleted.equals("true")) {
                        loginDeleteField.setText("");
                        clientCodeDeleteField.setText("");
                        errorDeleteUserLabel.setText("Пользователь успешно удалён!");
                    } else {
                        errorDeleteUserLabel.setText("Не удалось удалить пользователя!");
                    }
                } catch (Exception e) {
                    logger.log(Level.ERROR, e.getMessage());
                }


            }
        });

        /**Поиск пользователя*/
        searchBtn.setOnAction(actionEvent -> {
            String searchFio = signUpSearchFIOField.getText().trim();
            String searchLogin = signUpSearchLoginField.getText().trim();

            if (searchFio.isEmpty()) {
                Shake shakeSearchFio = new Shake(signUpSearchFIOField);
                shakeSearchFio.shake();

                errorSearchLabel.setText("Поле 'ФИО' пустое!");
            } else if (searchLogin.isEmpty()) {
                Shake shakeSearchLogin = new Shake(signUpSearchLoginField);
                shakeSearchLogin.shake();

                errorSearchLabel.setText("Поле 'Логин' пустое!");
            } else {
                errorSearchLabel.setText("");

                try {
                    connector.writeLine("search");
                    connector.writeLine("searchUser");
                    connector.writeLine(searchFio);
                    connector.writeLine(searchLogin);
                    String flagSearchUser = connector.readLine();
                    this.flagSearchUser = flagSearchUser;
                    User client;

                    if (flagSearchUser.equals("true")) {
                        client = (User) connector.readObj();
                        this.userSearch = client;
                        if (client != null) {
                            signUpEditFIOField.setText(client.getFIO());
                            signUpEditClientCodeField.setText(client.getClientCode());
                            signUpEditPassportIdField.setText(client.getPassportId());
                            signUpEditPasswordField.setText("");
                            signUpEditMailField.setText(client.getMail());
                            signUpEditLoginField.setText(client.getLogin());
                            signUpEditMobileNumberField.setText(client.getMobileNumber());
                            signUpEditIsAdminField.setValue(client.getIsAdmin() ? adminRole : userRole);
                        }
                    } else if (flagSearchUser.equals("false")) {
                        signUpEditFIOField.setText("");
                        signUpEditClientCodeField.setText("");
                        signUpEditPassportIdField.setText("");
                        signUpEditMailField.setText("");
                        signUpEditPasswordField.setText("");
                        signUpEditLoginField.setText("");
                        signUpEditMobileNumberField.setText("");
                        signUpEditIsAdminField.setValue(userRole);
                        errorSearchLabel.setText("Пользователь не найден!");
                    } else {
                        errorSearchLabel.setText("Ошибка сервера!");
                    }
                } catch (Exception e) {
                    logger.log(Level.ERROR, e.getMessage());
                }

                signUpSearchFIOField.setText("");
                signUpSearchLoginField.setText("");
            }
        });

        /**Изменение пользователя*/
        editBtn.setOnAction(actionEvent -> {
            String fio = signUpEditFIOField.getText().trim();
            String clientCode = signUpEditClientCodeField.getText().trim();
            String passportId = signUpEditPassportIdField.getText().trim();
            String mail = signUpEditMailField.getText().trim();
            String pass = signUpEditPasswordField.getText().trim();
            String login = signUpEditLoginField.getText().trim();
            String mobileNumber = signUpEditMobileNumberField.getText().trim();
            String isAdmin = signUpEditIsAdminField.getValue();

            if (fio.isEmpty()) {
                Shake shakeFIO = new Shake(signUpEditFIOField);
                shakeFIO.shake();
            } else if (clientCode.isEmpty()) {
                Shake shakeClientCode = new Shake(signUpEditClientCodeField);
                shakeClientCode.shake();
            } else if (mobileNumber.isEmpty() || !Check.isPhoneNumber(mobileNumber)) {
                Shake shakeMobileNumber = new Shake(signUpEditMobileNumberField);
                shakeMobileNumber.shake();
            } else if (pass.isEmpty()) {
                Shake shakePassword = new Shake(signUpEditPasswordField);
                shakePassword.shake();
            } else if (mail.isEmpty()) {
                Shake shakeMail = new Shake(signUpEditMailField);
                shakeMail.shake();
            } else if (login.isEmpty()) {
                Shake shakeLogin = new Shake(signUpEditLoginField);
                shakeLogin.shake();
            } else if (passportId.isEmpty()) {
                Shake shakePassportId = new Shake(signUpEditPassportIdField);
                shakePassportId.shake();
            } else if (flagSearchUser.equals("false") || userSearch == null) {
                errorEditLabel.setText("Пользователь для изменения не найден!");
            } else {
                errorEditLabel.setText("");

                User client = new User();

                client.setId(userSearch.getId());
                client.setFIO(fio);
                client.setClientCode(clientCode);
                client.setPassportId(passportId);
                client.setMail(mail);
                client.setPasswordHash(DigestUtils.sha256Hex(pass));
                client.setLogin(login);
                client.setMobileNumber(mobileNumber);
                client.setIsAdmin(isAdmin.equals(adminRole));

                try {
                    connector.writeLine("edit");
                    connector.writeLine("editUser");
                    connector.writeObj(client);

                    String flagIsEdited = connector.readLine();

                    if (flagIsEdited.equals("true")) {
                        errorEditLabel.setText("Пользователь успешно изменён");
                    } else {
                        errorEditLabel.setText("Не удалось изменить пользователя");
                    }

                    signUpEditFIOField.setText("");
                    signUpEditClientCodeField.setText("");
                    signUpEditPassportIdField.setText("");
                    signUpEditMailField.setText("");
                    signUpEditPasswordField.setText("");
                    signUpEditLoginField.setText("");
                    signUpEditMobileNumberField.setText("");
                    signUpEditIsAdminField.setValue("");
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        });

        /**Просмотр билетов*/
        ticketsViewBtn.setOnAction(actionEvent -> {
            try {
                connector.writeLine("view");
                connector.writeLine("viewTicket");
                ArrayList<Ticket> ticketAdminArrayList = (ArrayList<Ticket>) connector.readObjList().clone();
                ObservableList<Ticket> observableList = FXCollections.observableArrayList(ticketAdminArrayList);

                ticketTableView.setItems(observableList);
                ticketTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                ticketTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("ticketCode"));
                ticketTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("userCode"));
                ticketTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("transportType"));
                ticketTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("departurePoint"));
                ticketTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("arrivalPoint"));
                ticketTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory("departureDate"));
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        });

        /**Удалить билет*/
        ticketDeleteBtn.setOnAction(ActionEvent -> {
            String idTicket = ticketIdDeleteField.getText().trim();

            if (!Check.isNumber(idTicket)) {
                Shake shake = new Shake(ticketIdDeleteField);
                shake.shake();
                errorTicketDeleteLabel.setText("Введите число!");
            } else {
                try {
                    connector.writeLine("delete");
                    connector.writeLine("ticketDelete");
                    connector.writeLine(idTicket);
                    final String isAdmin = connector.readLine();
                    if (isAdmin.equals("true")) {
                        errorTicketDeleteLabel.setText("Билет успешно удалён.");
                    } else if (isAdmin.equals("false")) {
                        errorTicketDeleteLabel.setText("Отсутствует билет с данным ID");
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    ticketIdDeleteField.setText("");
                }
            }
        });

        /**Поиск заказов по клиенту*/
        searchOrderClientCodeBtn.setOnAction(ActionEvent -> {
            String clientCode = searchClientCodeField.getText().trim();

            if (clientCode.isEmpty()) {
                Shake shakeSearch = new Shake(searchClientCodeField);
                shakeSearch.shake();

                errorOrderLabel.setText("Введите код клиента!");
                tabViewOrdersSearch.getItems().clear();
            } else {
                try {
                    errorOrderLabel.setText("");

                    connector.writeLine("view");
                    connector.writeLine("viewOrder");
                    ArrayList<Order> orderArrayList = (ArrayList<Order>) connector.readObjList().clone();
                    ArrayList<Order> orders = new ArrayList<>();

                    for (Order o : orderArrayList) {
                        if (clientCode.equals(o.getClientCode())) {
                            orders.add(o);
                        }
                    }

                    if (orders.isEmpty()) {
                        errorOrderLabel.setText("Отсутствуют заказы с данным кодом клиента!");
                        tabViewOrdersSearch.getItems().clear();
                    } else {
                        ObservableList<Order> observableList = FXCollections.observableArrayList(orders);
                        tabViewOrdersSearch.setItems(observableList);
                        tabViewOrdersSearch.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                        tabViewOrdersSearch.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("clientCode"));
                        tabViewOrdersSearch.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("tourCode"));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    searchTourCodeField.setText("");
                    searchClientCodeField.setText("");
                }
            }
        });

        /**Поиск заказов по туру*/
        searchOrderTourCodeBtn.setOnAction(ActionEvent -> {
            String tourCode = searchTourCodeField.getText().trim();

            if (tourCode.isEmpty()) {
                Shake shakeSearch = new Shake(searchTourCodeField);
                shakeSearch.shake();

                errorOrderLabel.setText("Введите код тура!");
                tabViewOrdersSearch.getItems().clear();
            } else {
                try {
                    searchTourCodeField.setText("");

                    connector.writeLine("view");
                    connector.writeLine("viewOrder");
                    ArrayList<Order> orderArrayList = (ArrayList<Order>) connector.readObjList().clone();
                    ArrayList<Order> orders = new ArrayList<>();

                    for (Order o : orderArrayList) {
                        if (tourCode.equals(o.getTourCode())) {
                            orders.add(o);
                        }
                    }

                    if (orders.isEmpty()) {
                        errorOrderLabel.setText("Отсутствуют заказы с данным туром!");
                        tabViewOrdersSearch.getItems().clear();
                    } else {
                        ObservableList<Order> observableList = FXCollections.observableArrayList(orders);
                        tabViewOrdersSearch.setItems(observableList);
                        tabViewOrdersSearch.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
                        tabViewOrdersSearch.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("clientCode"));
                        tabViewOrdersSearch.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("tourCode"));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    searchTourCodeField.setText("");
                    searchClientCodeField.setText("");
                }
            }
        });

        /**Поиск тура*/
        searchTourIdBtn.setOnAction(ActionEvent -> {
            String id = searchIdTourEditFiled.getText().trim();

            if (id.isEmpty() || !Check.isNumber(id)) {
                Shake shake = new Shake(searchIdTourEditFiled);
                shake.shake();
                errorMsgEditTourLabel.setText("ID тура должно быть числом!");
            } else {
                errorMsgEditTourLabel.setText("");

                try {
                    int idTour = Integer.parseInt(id);
                    connector.writeLine("view");
                    connector.writeLine("viewTour");
                    ArrayList<Tour> tours = (ArrayList<Tour>) connector.readObjList().clone();

                    int i = 0;
                    for (Tour t : tours) {
                        if (idTour == t.getId()) {
                            ++i;
                            idTourEditFiled.setText(String.valueOf(t.getId()));
                            countryTourEditField.setText(t.getCountryName());
                            cityTourEditField.setText(t.getCityName());
                            priceTourEditField.setText(String.valueOf(t.getPrice()));
                            durationTourEditField.setText(t.getDuration());
                            codeTourEditField.setText(t.getTourCode());
                            nameTourEditField.setText(t.getTourName());
                            typeTourEditField.setText(t.getTourType());
                        }
                    }

                    if (i == 0) errorMsgEditTourLabel.setText("Не найден тур с данным ID!");

                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    searchIdTourEditFiled.setText("");
                }
            }
        });

        /**Изменение тура*/
        editTourBtn.setOnAction(ActionEvent -> {
            String idTour = idTourEditFiled.getText().trim();
            String countryTour = countryTourEditField.getText().trim();
            String cityTour = cityTourEditField.getText().trim();
            String priceTour = priceTourEditField.getText().trim();
            String durationTour = durationTourEditField.getText().trim();
            String codeTour = codeTourEditField.getText().trim();
            String nameTour = nameTourEditField.getText().trim();
            String typeTour = typeTourEditField.getText().trim();
            LocalDate localDate = dateTourEditField.getValue();

            if (!Check.isNumber(idTour)) {
                Shake shake = new Shake(idTourEditFiled);
                shake.shake();
            } else if (countryTour.isEmpty()) {
                Shake shake = new Shake(countryTourEditField);
                shake.shake();
            } else if (cityTour.isEmpty()) {
                Shake shake = new Shake(cityTourEditField);
                shake.shake();
            } else if (!Check.isFloat(priceTour)) {
                Shake shake = new Shake(priceTourEditField);
                shake.shake();
            } else if (!Check.isNumber(durationTour)) {
                Shake shake = new Shake(durationTourEditField);
                shake.shake();
            } else if (codeTour.isEmpty()) {
                Shake shake = new Shake(codeTourEditField);
                shake.shake();
            } else if (nameTour.isEmpty()) {
                Shake shake = new Shake(nameTourEditField);
                shake.shake();
            } else if (typeTour.isEmpty()) {
                Shake shake = new Shake(typeTourEditField);
                shake.shake();
            } else if (localDate.toString().isEmpty()) {
                Shake shake = new Shake(dateTourEditField);
                shake.shake();
            } else {
                errorMsgEditTourLabel.setText("");

                try {
                    Tour t = new Tour();
                    t.setId(Integer.parseInt(idTour));
                    t.setCountryName(countryTour);
                    t.setCityName(cityTour);
                    t.setPrice(Float.parseFloat(priceTour));
                    t.setDuration(durationTour);
                    t.setTourCode(codeTour);
                    t.setTourName(nameTour);
                    t.setTourType(typeTour);
                    t.setTourDate(localDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));

                    connector.writeLine("edit");
                    connector.writeLine("editTour");
                    connector.writeObj(t);
                    String flag = connector.readLine();

                    if (flag.equals("true")) {
                        errorMsgEditTourLabel.setText("Тур умпешно изменён!");
                    } else {
                        errorMsgEditTourLabel.setText("Не удалось изменить тур");
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                } finally {
                    idTourEditFiled.setText("");
                    countryTourEditField.setText("");
                    cityTourEditField.setText("");
                    priceTourEditField.setText("");
                    durationTourEditField.setText("");
                    codeTourEditField.setText("");
                    nameTourEditField.setText("");
                    typeTourEditField.setText("s");
                }
            }
        });
    }

    @FXML
    void deleteTour(ActionEvent event) {
        String flagDeleteTourServer;
        String id = deleteIdTourField.getText().trim();

        if (!Check.isNumber(id)) {
            Shake shakeTourDelete = new Shake(deleteIdTourField);
            shakeTourDelete.shake();
            errorTourDeleteId.setText("ID тура должно быть числом!");
        } else {
            errorTourDeleteId.setText("");

            try {
                connector.writeLine("delete");
                connector.writeLine("deleteTour");
                connector.writeLine(id);
                flagDeleteTourServer = connector.readLine();

                if (flagDeleteTourServer.equals("true")) {
                    errorTourDeleteId.setText("Тур успешно удален!");
                } else {
                    errorTourDeleteId.setText("Не удалось удалить тур!");
                    System.out.println("Тур не удалён");
                }
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            } finally {
                deleteIdTourField.setText("");
            }
        }
    }

    /**
     * Просмотр Туров
     */
    @FXML
    void getToursView(ActionEvent event) {
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
    }

    /**
     * Просмотр Заказов
     */
    @FXML
    void getOrderView(ActionEvent event) {
        try {
            connector.writeLine("view");
            connector.writeLine("viewOrder");
            ArrayList<Order> orderArrayList = (ArrayList<Order>) connector.readObjList().clone();
            ObservableList<Order> observableList = FXCollections.observableArrayList(orderArrayList);
            ordersTableView.setItems(observableList);
            ordersTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
            ordersTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("clientCode"));
            ordersTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("tourCode"));
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    /**
     * Добавить тур
     */
    @FXML
    void addTour(ActionEvent event) {
        new InputDialog(event, "views/add-tour-view.fxml", 530, 475);
    }

    /**
     * Создать билет
     */
    @FXML
    void checkAndCreateTicket(ActionEvent event) {
        new InputDialog(event, "views/check-create-ticket-view.fxml", 400, 400);
    }


}
