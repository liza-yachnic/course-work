package db.users;

import db.IHandler;
import models.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersHandler extends UsersTable implements IHandler {
    Logger logger = LogManager.getLogger(UsersHandler.class);

    @Override
    public boolean addObj(Object obj) {
        try {
            User client = (User) obj;
            String insert = "INSERT INTO " + UsersTable.TABLE_NAME + "("
                    + UsersTable.FIO_FIELD + ","
                    + UsersTable.CODE_FIELD + ","
                    + UsersTable.PASSPORT_ID_FIELD + ","
                    + UsersTable.MAIL_FIELD + ","
                    + UsersTable.MOBILE_NUMBER_FIELD + ","
                    + UsersTable.LOGIN_FIELD + ","
                    + UsersTable.PASSWORD_HASH_FIELD + ","
                    + UsersTable.IS_ADMIN_FIELD + ")" + "VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, client.getFIO());
            prSt.setString(2, client.getClientCode());
            prSt.setString(3, client.getPassportId());
            prSt.setString(4, client.getMail());
            prSt.setString(5, client.getMobileNumber());
            prSt.setString(6, client.getLogin());
            prSt.setString(7, client.getPasswordHash());
            prSt.setBoolean(8, client.getIsAdmin());
            prSt.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteObj(Object obj) {
        try {
            User client = (User) obj;
            String DELETE = "DELETE FROM " + UsersTable.TABLE_NAME + " WHERE " + UsersTable.ID_FIELD + "='" + client.getId() + "'; ";
            PreparedStatement preparedStatementDelete = getDbConnection().prepareStatement(DELETE);
            preparedStatementDelete.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean editObj(Object obj) {
        try {
            User client = (User) obj;
            String update = "UPDATE " + UsersTable.TABLE_NAME +
                    " SET " + UsersTable.FIO_FIELD + "=?, " + UsersTable.CODE_FIELD + "=?, " + UsersTable.PASSPORT_ID_FIELD + "=?, " + UsersTable.MAIL_FIELD + "=?, " + UsersTable.MOBILE_NUMBER_FIELD + "=?, " + UsersTable.LOGIN_FIELD + "=?, " + UsersTable.PASSWORD_HASH_FIELD + "=?, " + UsersTable.IS_ADMIN_FIELD + "=? " +
                    " WHERE " + UsersTable.ID_FIELD + "=?";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, client.getFIO());
            preparedStatement.setString(2, client.getClientCode());
            preparedStatement.setString(3, client.getPassportId());
            preparedStatement.setString(4, client.getMail());
            preparedStatement.setString(5, client.getMobileNumber());
            preparedStatement.setString(6, client.getLogin());
            preparedStatement.setString(7, client.getPasswordHash());
            preparedStatement.setBoolean(8, client.getIsAdmin());
            preparedStatement.setInt(9, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Object> getList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        try {
            String select = "SELECT * FROM " + UsersTable.TABLE_NAME;
            Statement statement = getDbConnection().createStatement();
            ResultSet resSet = statement.executeQuery(select);
            while (resSet.next()) {
                User client = new User();
                client.setId(resSet.getInt(1));
                client.setFIO(resSet.getString(2));
                client.setClientCode(resSet.getString(3));
                client.setPassportId(resSet.getString(4));
                client.setMail(resSet.getString(5));
                client.setMobileNumber(resSet.getString(6));
                client.setLogin(resSet.getString(7));
                client.setPasswordHash(resSet.getString(8));
                client.setIsAdmin(resSet.getBoolean(9));
                arrayList.add(client);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        return arrayList;
    }
}
