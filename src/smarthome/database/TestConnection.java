
package smarthome.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {
        try (Connection connection =
                     DBConnection.getConnection()) {

            System.out.println(
                "MySQL Database Connected Successfully!"
            );

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            System.out.println(e.getMessage());
        }
    }
}
