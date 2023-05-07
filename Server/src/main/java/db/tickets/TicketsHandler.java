package db.tickets;

import db.IHandler;
import db.orders.OrdersHandler;
import models.Ticket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TicketsHandler extends TicketsTable implements IHandler {
    Logger logger = LogManager.getLogger(TicketsHandler.class);

    @Override
    public boolean addObj(Object obj) {
        try {
            Ticket ticket = (Ticket) obj;
            String insert = "INSERT INTO " + TicketsTable.TABLE_NAME + "("
                    + TicketsTable.CODE_FIELD + ","
                    + TicketsTable.USER_CODE_FIELD + ","
                    + TicketsTable.TRANSPORT_TYPE_FIELD + ","
                    + TicketsTable.DEPARTURE_POINT_FIELD + ","
                    + TicketsTable.ARRIVAL_POINT_FIELD + ","
                    + TicketsTable.DEPARTURE_DATE_FIELD + ")" + "VALUES(?,?,?,?,?,?)";

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, ticket.getTicketCode());
            prSt.setString(2, ticket.getUserCode());
            prSt.setString(3, ticket.getTransportType());
            prSt.setString(4, ticket.getDeparturePoint());
            prSt.setString(5, ticket.getArrivalPoint());
            prSt.setString(6, ticket.getDepartureDate());
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
            Ticket ticket = (Ticket) obj;
            String DELETE = "DELETE FROM " + TicketsTable.TABLE_NAME + " WHERE " + TicketsTable.ID_FIELD + "='" + ticket.getId() + "'; ";
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
            String select = "SELECT * FROM " + TicketsTable.TABLE_NAME;
            Statement statement = getDbConnection().createStatement();
            ResultSet resSet = statement.executeQuery(select);
            while (resSet.next()) {
                Ticket t = new Ticket();
                t.setId(resSet.getInt(1));
                t.setTicketCode(resSet.getString(2));
                t.setUserCode(resSet.getString(3));
                t.setTransportType(resSet.getString(4));
                t.setDeparturePoint(resSet.getString(5));
                t.setArrivalPoint(resSet.getString(6));
                t.setDepartureDate(resSet.getString(7));
                arrayList.add(t);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        return arrayList;
    }
}
