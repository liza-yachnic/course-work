package db.orders;

import db.IHandler;
import models.Order;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrdersHandler extends OrdersTable implements IHandler {
    Logger logger = LogManager.getLogger(OrdersHandler.class);

    @Override
    public boolean addObj(Object obj) {
        try {
            Order order = (Order) obj;
            String insert = "INSERT INTO " + OrdersTable.TABLE_NAME + "("
                    + OrdersTable.CLIENT_CODE_FIELD + ","
                    + OrdersTable.TOUR_CODE_FIELD + ")" + "VALUES(?,?)";
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, order.getClientCode());
            prSt.setString(2, order.getTourCode());
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
            Order order = (Order) obj;
            String DELETE = "DELETE FROM " + OrdersTable.TABLE_NAME + " WHERE " + OrdersTable.ID_FIELD + "='" + order.getId() + "'; ";
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
        return false;
    }

    @Override
    public ArrayList<Object> getList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        try {
            String select = "SELECT * FROM " + OrdersTable.TABLE_NAME;
            Statement statement = getDbConnection().createStatement();
            ResultSet resSet = statement.executeQuery(select);
            while (resSet.next()) {
                Order order = new Order();
                order.setId(resSet.getInt(1));
                order.setClientCode(resSet.getString(2));
                order.setTourCode(resSet.getString(3));
                arrayList.add(order);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        return arrayList;
    }
}
