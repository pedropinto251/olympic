package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ITeamApplicationDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.TeamApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface {@link ITeamApplicationDAO} para gerenciar operações relacionadas às candidaturas de equipes.
 * Este DAO interage com a tabela "TeamApplications" na base de dados para realizar operações de CRUD (Create, Read, Update, Delete).
 */
public class TeamApplicationDAO implements ITeamApplicationDAO {

    /**
     * Cria uma nova candidatura para uma equipe, inserindo os dados na tabela "TeamApplications".
     *
     * @param application Objeto {@link TeamApplication} que contém os dados da candidatura.
     * @param connection Conexão com a base de dados.
     * @return {@code true} se a candidatura foi inserida com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public boolean createApplication(TeamApplication application, Connection connection) throws SQLException {
        // SQL de inserção, sem o campo 'team_id' (será nulo)
        String sql = "INSERT INTO TeamApplications (athlete_id, team_id, status, inative, sport_id) " +
                "VALUES (?, NULL, ?, 'false', ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Define os valores restantes
            stmt.setInt(1, application.getAthleteId()); // ID do atleta
            stmt.setString(2, application.getStatus()); // Status da aplicação
            stmt.setInt(3, application.getSport_id());  // ID do esporte associado

            // Executa a inserção
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Obtém o ID gerado pela base de dados
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Atribui o ID gerado ao objeto TeamApplication
                        application.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Recupera uma candidatura de equipe pelo seu ID.
     *
     * @param id O ID da candidatura a ser recuperada.
     * @param connection Conexão com a base de dados.
     * @return O objeto {@link TeamApplication} correspondente ao ID fornecido, ou {@code null} caso não seja encontrado.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public TeamApplication getApplicationById(int id, Connection connection) throws SQLException {
        String sql = "SELECT * FROM TeamApplications WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToApplication(rs);
            }
        }
        return null;
    }

    /**
     * Recupera todas as candidaturas de equipes.
     *
     * @param connection Conexão com a base de dados.
     * @return Uma lista de objetos {@link TeamApplication} contendo todas as candidaturas registradas.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public List<TeamApplication> getAllApplications(Connection connection) throws SQLException {
        List<TeamApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM TeamApplications";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        }
        return applications;
    }

    /**
     * Atualiza o status de uma candidatura de equipe.
     *
     * @param id O ID da candidatura a ser atualizada.
     * @param status O novo status da candidatura.
     * @param connection Conexão com a base de dados.
     * @return {@code true} se o status foi atualizado com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public boolean updateApplicationStatus(int id, String status, Connection connection) throws SQLException {
        String sql = "UPDATE TeamApplications SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui uma candidatura de equipe.
     *
     * @param id O ID da candidatura a ser excluída.
     * @param connection Conexão com a base de dados.
     * @return {@code true} se a candidatura foi excluída com sucesso, {@code false} caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public boolean deleteApplication(int id, Connection connection) throws SQLException {
        String sql = "DELETE FROM TeamApplications WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Mapeia um {@link ResultSet} para um objeto {@link TeamApplication}.
     *
     * @param rs O {@link ResultSet} contendo os dados da candidatura.
     * @return O objeto {@link TeamApplication} correspondente aos dados do {@link ResultSet}.
     * @throws SQLException Se ocorrer um erro ao acessar os dados no {@link ResultSet}.
     */
    private TeamApplication mapResultSetToApplication(ResultSet rs) throws SQLException {
        TeamApplication application = new TeamApplication();
        application.setId(rs.getInt("id"));
        application.setAthleteId(rs.getInt("athlete_id"));
        application.setTeamId(rs.getInt("team_id"));
        application.setStatus(rs.getString("status"));
        application.setApplicationDate(rs.getTimestamp("application_date").toLocalDateTime());
        return application;
    }

    public ObservableList<TeamApplication> getCollectiveInscriptions() throws SQLException {
        ObservableList<TeamApplication> inscriptions = FXCollections.observableArrayList();
        String query = """
        SELECT 
            ta.id AS application_id,
            a.name AS athlete_name,
            a.country AS athlete_country,
            a.genre AS athlete_genre,
            ta.sport_id,
            s.name AS sport_name,
            ta.team_id AS team_id,
            ta.status AS application_status,
            ta.inative AS application_inative
        FROM TeamApplications ta
        INNER JOIN Athletes a ON ta.athlete_id = a.id
        INNER JOIN Sports s ON ta.sport_id = s.id
        WHERE s.type = 'Collective' AND ta.inative = 'false';
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inscriptions.add(new TeamApplication(
                        rs.getInt("application_id"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("sport_id"),
                        rs.getString("sport_name"),
                        rs.getInt("team_id"),
                        rs.getString("application_status"),
                        rs.getBoolean("application_inative")
                ));
            }
        }

        return inscriptions;
    }
    public boolean deactivateApplication(int id) throws SQLException {
        String query = "UPDATE TeamApplications SET inative = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean activateApplication(int id) throws SQLException {
        String query = "UPDATE TeamApplications SET inative = 0 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
