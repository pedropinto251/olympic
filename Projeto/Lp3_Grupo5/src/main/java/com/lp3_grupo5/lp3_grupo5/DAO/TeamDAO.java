package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ITeamDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.*;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface {@link ITeamDAO} para gerenciar operações de CRUD na tabela "Teams" do banco de dados.
 * Este DAO permite inserir, atualizar, excluir e recuperar equipes.
 */
public class TeamDAO implements ITeamDAO {

    /**
     * Insere uma nova equipe no banco de dados.
     *
     * @param team O objeto {@link Team} contendo os dados da equipe a ser inserida.
     * @return {@code true} se a equipe foi inserida com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public boolean insertTeam(Team team) throws SQLException {
        String query = "INSERT INTO Teams (name, country, genre, sport, foundationYear, sports_id, inative) VALUES (?, ?, ?, ?, ?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Define os parâmetros da query
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCountry());
            stmt.setString(3, team.getGenre());
            stmt.setString(4, team.getSport());
            stmt.setInt(5, team.getFoundationYear());
            stmt.setInt(6, team.getSportsId());

            // Executa a query de inserção
            int rowsAffected = stmt.executeUpdate();

            // Se a inserção for bem-sucedida, recupera o ID gerado
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        team.setId(generatedKeys.getInt(1));  // Atribui o ID gerado ao objeto team
                    }
                }
                return true;  // Inserção bem-sucedida
            }

            return false;  // Caso não tenha afetado nenhuma linha

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Caso haja erro na execução
        }
    }


    public Integer createTeam(int sportId, String country,String sportName, String gender, String teamName) {
        String sql = "INSERT INTO teams (name, country, sport, sports_id, genre, foundationYear, inative) VALUES (?, ?,?, ?, ?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int currentYear = Year.now().getValue();

            stmt.setString(1, teamName);
            stmt.setString(2, country);
            stmt.setString(3,sportName);
            stmt.setInt(4, sportId);
            stmt.setString(5, gender);
            stmt.setInt(6, currentYear);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Retorna o ID da equipe criada
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(gender);
        }
        return null; // Retorna null se a equipe não for criada
    }



    /**
     * Recupera todas as equipes ativas no banco de dados (onde o campo "inative" é igual a 0).
     *
     * @return Uma lista de objetos {@link Team} contendo todas as equipes ativas.
     */
    @Override
    public List<Team> findAll() {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM Teams WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("genre"),
                        rs.getString("sport"),
                        rs.getInt("sports_id"),
                        rs.getInt("foundationYear"),
                        rs.getBoolean("inative")
                );
                teams.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    /**
     * Recupera todos os atletas de um time específico usando a relação com a tabela "TeamsMemberships".
     *
     * @param teamId O ID do time cujos atletas devem ser recuperados.
     * @return Uma lista de objetos {@link Athlete} representando os atletas do time.
     */
    public List<Athlete> getAthletesByTeamId(int teamId) {
        List<Athlete> athletes = new ArrayList<>();
        String query = "SELECT a.id, a.name, a.photo " +
                "FROM athletes a " +
                "INNER JOIN teammemberships tm ON a.id = tm.athlete_id " +
                "WHERE tm.team_id = ? AND a.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Define o parâmetro da query
            stmt.setInt(1, teamId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Cria um objeto Athlete para cada registro retornado
                    Athlete athlete = new Athlete(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("photo")
                    );
                    athletes.add(athlete);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return athletes;
    }

    /**
     * Exclui uma equipe, marcando-a como inativa (campo "inative" igual a 1) no banco de dados.
     *
     * @param id O ID da equipe a ser excluída.
     * @return {@code true} se a equipe foi marcada como inativa com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public boolean deleteTeam(int id) throws SQLException {
        String updateQuery = "UPDATE Teams SET inative = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a exclusão foi bem-sucedida

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atualiza os dados de uma equipe no banco de dados.
     *
     * @param team O objeto {@link Team} contendo os dados a serem atualizados.
     * @return {@code true} se a equipe foi atualizada com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    public boolean updateTeam(Team team) throws SQLException {
        String query = "UPDATE Teams SET country = ?, sport = ?, foundationYear = ?, sports_id = ? WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Define os parâmetros da query
            stmt.setString(1, team.getCountry());
            stmt.setString(2, team.getSport());
            stmt.setInt(3, team.getFoundationYear());
            stmt.setInt(4, team.getSportsId());
            stmt.setString(5, team.getName());

            // Executa a query de atualização
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0; // Se pelo menos uma linha foi afetada, a atualização foi bem-sucedida
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Caso haja erro na execução
        }
    }

    /** Carregar XML**/
    @Override
    public void insertOrUpdateTeams(List<Team> teams) {
        for (Team team : teams) {
            if (teamExists(team.getName(), team.getGenre())) {
                updateTeamXML(team);
            } else {
                insertTeamXML(team);
            }
        }
    }

    @Override
    public boolean teamExists(String name, String genre) {
        String query = "SELECT COUNT(*) FROM Teams WHERE name = ? AND genre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateTeamXML(Team team) {
        String updateTeamSQL = "UPDATE Teams SET country = ?, sport = ?, foundationYear = ? WHERE name = ? AND genre = ?";
        String deleteParticipationsSQL = "DELETE FROM TeamOlympicParticipations WHERE team_id = (SELECT TOP 1 id FROM Teams WHERE name = ? AND genre = ?)";
        String insertParticipationSQL = "INSERT INTO TeamOlympicParticipations (year, team_id, result, inative) VALUES (?, (SELECT TOP 1 id FROM Teams WHERE name = ? AND genre = ?), ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement teamStmt = conn.prepareStatement(updateTeamSQL);
             PreparedStatement deleteParticipationsStmt = conn.prepareStatement(deleteParticipationsSQL);
             PreparedStatement insertParticipationStmt = conn.prepareStatement(insertParticipationSQL)) {

            // Atualizar a equipe
            teamStmt.setString(1, team.getCountry());
            teamStmt.setString(2, team.getSport());
            teamStmt.setInt(3, team.getFoundationYear());
            teamStmt.setString(4, team.getName());
            teamStmt.setString(5, team.getGenre());
            teamStmt.executeUpdate();

            // Deletar participações antigas
            deleteParticipationsStmt.setString(1, team.getName());
            deleteParticipationsStmt.setString(2, team.getGenre());
            deleteParticipationsStmt.executeUpdate();

            // Inserir novas participações
            for (TeamOlympicParticipation participation : team.getOlympicParticipations()) {
                insertParticipationStmt.setInt(1, participation.getYear());
                insertParticipationStmt.setString(2, team.getName());
                insertParticipationStmt.setString(3, team.getGenre());
                insertParticipationStmt.setString(4, participation.getResult());
                insertParticipationStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertTeamXML(Team team) {
        String insertTeamSQL = "INSERT INTO Teams (name, country, genre, sport, foundationYear, inative) VALUES (?, ?, ?, ?, ?, 0)";
        String insertParticipationSQL = "INSERT INTO TeamOlympicParticipations (year, team_id, result, inative) VALUES (?, (SELECT TOP 1 id FROM Teams WHERE name = ? AND genre = ?), ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement teamStmt = conn.prepareStatement(insertTeamSQL);
             PreparedStatement insertParticipationStmt = conn.prepareStatement(insertParticipationSQL)) {

            // Inserir a equipe
            teamStmt.setString(1, team.getName());
            teamStmt.setString(2, team.getCountry());
            teamStmt.setString(3, team.getGenre());
            teamStmt.setString(4, team.getSport());
            teamStmt.setInt(5, team.getFoundationYear());
            teamStmt.executeUpdate();

            // Inserir participações associadas à equipe
            for (TeamOlympicParticipation participation : team.getOlympicParticipations()) {
                insertParticipationStmt.setInt(1, participation.getYear());
                insertParticipationStmt.setString(2, team.getName());
                insertParticipationStmt.setString(3, team.getGenre());
                insertParticipationStmt.setString(4, participation.getResult());
                insertParticipationStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar uma equipe pelo ID
    public Team findById(int id) {
        String sql = "SELECT * FROM Teams WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Team(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("genre"),
                        rs.getString("sport"),
                        rs.getInt("sports_id"),
                        rs.getInt("foundationYear"),
                        rs.getBoolean("inative")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se a equipe não for encontrada
    }



    public boolean realDeleteTeam(int teamId) {
        String query = "DELETE FROM Teams WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, teamId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir a equipa: " + e.getMessage());
            return false;
        }
    }

}
