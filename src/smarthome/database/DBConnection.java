
package smarthome.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/SmartHomeDB";

    private static final String USER = "root";

    // Enter your MySQL root password locally.
    // Never upload your real password to GitHub.
    private static final String PASSWORD = "Nandugundu@1819";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
