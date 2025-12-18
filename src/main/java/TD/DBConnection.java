package TD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection getDBConnection() throws SQLException {
        String URL = "jdbc:postgresql://localhost:5432/mini_football_db";
        String USER = "mini_football_db_manager";
        String PASSWORD = "123456";
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(Connection connection) throws SQLException {
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}