package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public interface IHandler {
    DbConfig dbConfig = new DbConfig();

    default Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = dbConfig.getDbURL();
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(connectionString, dbConfig.getDbUser(), dbConfig.getDbPassword());
    }

    boolean addObj(Object obj);

    boolean deleteObj(Object obj);

    boolean editObj(Object obj);

    ArrayList<Object> getList();
}
