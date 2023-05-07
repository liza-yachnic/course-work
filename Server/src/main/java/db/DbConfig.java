package db;

import io.github.cdimascio.dotenv.Dotenv;

public class DbConfig {
    private final String dbHost;
    private final String dbPort;
    private final String dbUser;
    private final String dbPassword;
    private final String dbName;

    public DbConfig() {
        Dotenv dotenv = Dotenv.configure().directory("Server\\src\\main\\resources").load();
        dbHost = dotenv.get("DB_HOST");
        dbPort = dotenv.get("DB_PORT", "3306");
        dbUser = dotenv.get("DB_USER");
        dbPassword = dotenv.get("DB_PASSWORD");
        dbName = dotenv.get("DB_NAME");
    }

    public String getDbURL() {
        return "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }
}
