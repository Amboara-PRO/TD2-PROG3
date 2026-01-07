package TD;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        try {
            Team real = null;
            try {
                real = dataRetriever.findTeamById(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println(
                    "Buts Real Madrid = " + real.getPlayersGoals()
            );
        } catch (RuntimeException e) {
            System.out.println("Exception Real Madrid : " + e.getMessage());
        }

        try {
            Team barca = dataRetriever.findTeamById(2);
            System.out.println(
                    "Buts Barcelone = " + barca.getPlayersGoals()
            );
        } catch (RuntimeException | SQLException e) {
            System.out.println("Exception Barcelone : " + e.getMessage());
        }
    }
}
