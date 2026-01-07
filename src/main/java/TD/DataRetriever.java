package TD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

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
        while (resultSet.next()) {
            Team team = new Team();
            Integer teamId = resultSet.getInt("id");
            team.setId(resultSet.getInt("id"));
            team.setName(resultSet.getString("name"));
            team.setContinent(ContinentEnum.valueOf(resultSet.getString("continent")));
            team.setPlayers(findPlayerByTeamId(teamId));
            dbConnection.close(connection);
            return team;

        }

        return null;
    }
    public List<Player> findPlayerByTeamId(Integer idTeam) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();
        List<Player> players = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement("""
select id, name, age, position from player where id_team = ?
""");

        preparedStatement.setInt(1, idTeam);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Player player = new Player(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    PlayerPositionEnum.valueOf(resultSet.getString("position"))
            );

            players.add(player);
        }

        dbConnection.close(connection);

        return players;
    }
    public List<Player> findPlayers(int page, int size) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();

        List<Player> players = new ArrayList<>();
        int offset = (page - 1) * size;

        PreparedStatement preparedStatement = connection.prepareStatement("""
select Player.id, Player.name, Player.age, Player.position, Team.id as t_id, Team.name as t_name, Team.continent as t_continent from Player left join Team on Player.id_team = Team.id limit ? offset ?
""") ;
        preparedStatement.setInt(1, size);
        preparedStatement.setInt(2, offset);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Player player = new Player(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    PlayerPositionEnum.valueOf(resultSet.getString("position")),
                    new Team(
                            resultSet.getInt("t_id"),
                            resultSet.getString("t_name"),
                            ContinentEnum.valueOf(resultSet.getString("t_continent")),
                            new ArrayList<>()

                    )
            );
            players.add(player);
        }

        dbConnection.close(connection);

        return players;
    }
    public List<Player> createPlayers(List<Player> newPlayers) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();

        String select = """
                select count(player.id) from player where id = ?
                """;
        String insert = """
                insert into player(id, name, age, position, id_team, goal_nb) values (?,?,?,?,?,?)
                """;

        connection.setAutoCommit(false);
        PreparedStatement selectStatement = connection.prepareStatement(select);
        PreparedStatement insertStatement = connection.prepareStatement(insert);

        for (Player player : newPlayers) {
            selectStatement.setInt(1, player.getId());
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                throw new RuntimeException("Player already exists");
            }
        }
        for (Player player : newPlayers) {
            insertStatement.setInt(1, player.getId());
            insertStatement.setString(2, player.getName());
            insertStatement.setInt(3, player.getAge());
            insertStatement.setString(4, player.getPosition().toString());
            insertStatement.setInt(5, player.getTeam().getId());
            insertStatement.setInt(6, player.getGoalNb());
            insertStatement.executeUpdate();
        }
        connection.commit();
        dbConnection.close(connection);
        return newPlayers;
    }
    public Team saveTeam(Team teamToSave) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();

        String select = """
                select 1 from team where id = ?
                """;

        String insertTeam = """
                insert into team(id, name, continent) values (?,?,?)
                """;
        String updateTeam = """
                update team set name = ?, continent = ? where id = ?
                """;
        String updatePlayer = """
                update player set id_team = ? where id = ?
                """;

        connection.setAutoCommit(false);

        PreparedStatement selectStatement = connection.prepareStatement(select);
        PreparedStatement insertTeamStatement = connection.prepareStatement(insertTeam);
        PreparedStatement updateTeamStatement = connection.prepareStatement(updateTeam);
        PreparedStatement updatePlayerStatement = connection.prepareStatement(updatePlayer);

        selectStatement.setInt(1, teamToSave.getId());
        ResultSet resultSet = selectStatement.executeQuery();
        if (resultSet.next()) {
            updateTeamStatement.setString(1, teamToSave.getName());
            updateTeamStatement.setString(2, teamToSave.getContinent().toString());
            updateTeamStatement.setInt(3, teamToSave.getId());
            updateTeamStatement.executeUpdate() ;
        }
        else {
            insertTeamStatement.setInt(1, teamToSave.getId());
            insertTeamStatement.setString(2, teamToSave.getName());
            insertTeamStatement.setString(3, teamToSave.getContinent().toString());
            insertTeamStatement.executeUpdate() ;
        }
        for(Player player : teamToSave.getPlayers()){
            updatePlayerStatement.setInt(1, teamToSave.getId());
            updatePlayerStatement.setInt(2, player.getId());
            updatePlayerStatement.executeUpdate();
        }
        connection.commit();
        dbConnection.close(connection);
        return teamToSave;

    }
    public List<Team> findTeamsByPlayerName(String playerName) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();
        List<Team> teams = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(
                """
select Team.id as t_id, Team.name as t_name, Team.continent as t_continent from Team inner join Player on Team.id = Player.id_team where Player.name ilike ? 
"""
        );
        preparedStatement.setString(1, "%" + playerName + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Team team = new Team(
                    resultSet.getInt("t_id"),
                    resultSet.getString("t_name"),
                    ContinentEnum.valueOf(resultSet.getString("t_continent")),
                    new ArrayList<>()
            );
            teams.add(team);
        }

        dbConnection.close(connection);
        return teams;
    }
    public List<Player> findPlayersByCriteria(String playerName,
                                            PlayerPositionEnum position, String teamName, ContinentEnum
                                            continent, int page, int size) throws SQLException {
        Connection connection;
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getDBConnection();
        List<Player> players = new ArrayList<>();

        int offset = (page - 1) * size;

        StringBuilder select = new StringBuilder("""
        SELECT 
            p.id, p.name, p.age, p.position, p.id_team
        FROM player p
        JOIN team t ON p.id_team = t.id
        WHERE 1 = 1
    """);
        List<Object> params = new ArrayList<>();

        if (playerName != null && !playerName.isBlank()) {
            select.append(" AND LOWER(p.name) LIKE LOWER(?)");
            params.add("%" + playerName + "%");
        }

        if (position != null) {
            select.append(" AND p.position = ?");
            params.add(position.name());
        }

        if (teamName != null && !teamName.isBlank()) {
            select.append(" AND LOWER(t.name) LIKE LOWER(?)");
            params.add("%" + teamName + "%");
        }

        if (continent != null) {
            select.append(" AND t.continent = ?");
            params.add(continent.name());
        }

        select.append(" ORDER BY p.id LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

             PreparedStatement preparedStatements = connection.prepareStatement(select.toString());

            for (int i = 0; i < params.size(); i++) {
                preparedStatements.setObject(i + 1, params.get(i));
            }

            ResultSet rs = preparedStatements.executeQuery();

            while (rs.next()) {
                Player player = new Player();
                player.setId(rs.getInt("id"));
                player.setName(rs.getString("name"));
                player.setAge(rs.getInt("age"));
                player.setPosition(
                        PlayerPositionEnum.valueOf(rs.getString("position"))
                );

                Team team = new Team();
                team.setId(rs.getInt("id_team"));
                player.setTeam(team);

                players.add(player);
            }

        dbConnection.close(connection);

        return players;
    }
    public Player mapPlayer(ResultSet rs, Team team) throws SQLException {
        Player player = new Player();

        player.setId(rs.getInt("id"));
        player.setName(rs.getString("name"));
        player.setAge(rs.getInt("age"));
        player.setPosition(
                PlayerPositionEnum.valueOf(rs.getString("position"))
        );

        player.setGoalNb(rs.getObject("goal_nb", Integer.class));
        player.setTeam(team);

        return player;
    }

}
