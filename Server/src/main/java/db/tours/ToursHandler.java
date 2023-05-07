package db.tours;

import db.IHandler;
import db.orders.OrdersHandler;
import models.Tour;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ToursHandler extends ToursTable implements IHandler {
    Logger logger = LogManager.getLogger(ToursHandler.class);

    @Override
    public boolean addObj(Object obj) {
        try {
            Tour tour = (Tour) obj;
            String insert = "INSERT INTO " + ToursTable.TABLE_NAME + "("
                    + ToursTable.COUNTRY_NAME_FIELD + ","
                    + ToursTable.CITY_NAME_FIELD + ","
                    + ToursTable.PRICE_FIELD + ","
                    + ToursTable.DURATION_FIELD + ","
                    + ToursTable.CODE_FIELD + ","
                    + ToursTable.DATE_FIELD + ","
                    + ToursTable.NAME_FIELD + ","
                    + ToursTable.TYPE_FIELD + ")" + "VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, tour.getCountryName());
            prSt.setString(2, tour.getCityName());
            prSt.setFloat(3, tour.getPrice());
            prSt.setString(4, tour.getDuration());
            prSt.setString(5, tour.getTourCode());
            prSt.setString(6, tour.getTourDate());
            prSt.setString(7, tour.getTourName());
            prSt.setString(8, tour.getTourType());
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteObj(Object obj) {
        try {
            Tour tour = (Tour) obj;
            String DELETE = "DELETE FROM " + ToursTable.TABLE_NAME + " WHERE " + ToursTable.ID_FIELD + "='" + tour.getId() + "'; ";
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
            Tour t = (Tour) obj;
            System.out.println(t);
            String update = "UPDATE " + ToursTable.TABLE_NAME +
                    " SET "
                    + ToursTable.COUNTRY_NAME_FIELD + "=?, "
                    + ToursTable.CITY_NAME_FIELD + "=?, "
                    + ToursTable.PRICE_FIELD + "=?, "
                    + ToursTable.DURATION_FIELD + "=?, "
                    + ToursTable.CODE_FIELD + "=?, "
                    + ToursTable.DATE_FIELD + "=?, "
                    + ToursTable.NAME_FIELD + "=?, "
                    + ToursTable.TYPE_FIELD+ "=? " +
                    " WHERE " + ToursTable.ID_FIELD + "=?";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, t.getCountryName());
            preparedStatement.setString(2, t.getCityName());
            preparedStatement.setFloat(3, t.getPrice());
            preparedStatement.setString(4, t.getDuration());
            preparedStatement.setString(5, t.getTourCode());
            preparedStatement.setString(6, t.getTourDate());
            preparedStatement.setString(7, t.getTourName());
            preparedStatement.setString(8, t.getTourType());
            preparedStatement.setInt(9, t.getId());
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
            String select = "SELECT * FROM " + ToursTable.TABLE_NAME;
            Statement statement = getDbConnection().createStatement();
            ResultSet resSet = statement.executeQuery(select);
            while (resSet.next()) {
                Tour t = new Tour();
                t.setId(resSet.getInt(1));
                t.setCountryName(resSet.getString(2));
                t.setCityName(resSet.getString(3));
                t.setPrice(resSet.getFloat(4));
                t.setDuration(resSet.getString(5));
                t.setTourCode(resSet.getString(6));
                t.setTourDate(resSet.getString(7));
                t.setTourName(resSet.getString(8));
                t.setTourType(resSet.getString(9));
                arrayList.add(t);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        return arrayList;
    }
}
