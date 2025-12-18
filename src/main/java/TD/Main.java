package TD;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnection dbConnection = new DBConnection();

        DataRetriever dataRetriever = new DataRetriever();

        Team team = dataRetriever.findTeamById(2);
        System.out.println(team);
    }
}