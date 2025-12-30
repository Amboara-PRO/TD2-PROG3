package TD;

import java.lang.foreign.ValueLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Player p1 = new Player();
        p1.setId(6);
        p1.setName("Jude Bellingham");
        p1.setAge(23);
        p1.setPosition(PlayerPositionEnum.STR);
        p1.setTeam(null);

        Player p2 = new Player();
        p2.setId(7);
        p2.setName("Pedri");
        p2.setAge(24);
        p2.setPosition(PlayerPositionEnum.MIDF);
        p2.setTeam(null);

        List<Player> players = List.of(p1, p2);

        DataRetriever dataRetriever = new DataRetriever();

        try {
            dataRetriever.createPlayers(players);
            System.out.println("ERREUR : exception attendue");
        } catch (RuntimeException | SQLException e) {
            System.out.println("RuntimeException attendue : " + e.getMessage());
        }
    }
}
