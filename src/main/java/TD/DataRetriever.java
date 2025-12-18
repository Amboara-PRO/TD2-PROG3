package TD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataRetriever {
    public Team findTeamById(Integer id) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("""
select id, name, continent from team where id = ?
""");

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Team team = new Team();
            team.setId(resultSet.getInt("id"));
            team.setName(resultSet.getString("name"));
            team.setContinent(ContinentEnum.valueOf(resultSet.getString("continent")));
            dbConnection.close(connection);
            return team;


        }

        return null;
    }
    public List<Player> findPlayers(int page, int size){
      ;  throw new UnsupportedOperationException("Not supported yet.");
    }
    public List<Player> createPlayers(List<Player> newPlayers){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Team saveTeam(Team teamToSave){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public List<Team> findTeamsByPlayerName(String playerName){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public List<Player> findPlayersByCriteria(String playerName,
                                            PlayerPositionEnum position, String teamName, ContinentEnum
                                            continent, int page, int size){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
