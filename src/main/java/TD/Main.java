package TD;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnection dbConnection = new DBConnection();

        Connection connection;
        try {
            connection = dbConnection.getDBConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dbConnection.close(connection);
    }
}