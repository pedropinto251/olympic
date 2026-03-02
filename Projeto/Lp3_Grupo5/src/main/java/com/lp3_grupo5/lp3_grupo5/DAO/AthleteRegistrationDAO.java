package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IAthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.Model.AthleteRegistration;
import com.lp3_grupo5.lp3_grupo5.Model.Calendar;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;



/**
 * Classe DAO responsável pelas operações relacionadas às inscrições de atletas em equipas.
 * Implementa a interface {@link IAthleteRegistrationDAO} e interage com a base de dados
 * para realizar ações como obter todas as inscrições pendentes, aprovar ou rejeitar inscrições
 * e registar atletas.
 */
public class AthleteRegistrationDAO implements IAthleteRegistrationDAO {

    /**
     * Recupera todas as inscrições pendentes de atletas nas equipas.
     * Realiza uma consulta SQL para obter todas as inscrições de atletas que ainda não foram aprovadas
     * ou rejeitadas, juntamente com os detalhes da equipa e do atleta, incluindo o país do atleta.
     *
     * @return Uma lista observável de {@link AthleteRegistration} contendo todas as inscrições pendentes.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public ObservableList<AthleteRegistration> getAllRegistrations() throws SQLException {
        ObservableList<AthleteRegistration> registrations = FXCollections.observableArrayList();

        // Atualizando a consulta para pegar o campo 'genre' da tabela Athletes
        String query = "SELECT ta.id AS registration_id, ta.athlete_id, ta.status, " +
                "s.id AS sport_id, s.name AS sport_name, ta.application_date, " +
                "a.country AS athlete_country, a.genre AS athlete_genre " +  // Adicionando o campo 'genre'
                "FROM teamApplications ta " +
                "INNER JOIN Athletes a ON ta.athlete_id = a.id " +  // INNER JOIN com a tabela Athletes
                "INNER JOIN Sports s ON ta.sport_id = s.id " +    // INNER JOIN com a tabela Sports
                "WHERE ta.status = 'pending' AND ta.inative = 0 AND a.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Criar um novo objeto AthleteRegistration com o campo 'genre' incluído
                AthleteRegistration registration = new AthleteRegistration(
                        rs.getInt("registration_id"),    // ID da inscrição
                        rs.getInt("athlete_id"),         // ID do atleta
                        rs.getString("status"),          // Status da inscrição
                        rs.getInt("sport_id"),           // ID do esporte
                        rs.getString("sport_name"),      // Nome do esporte
                        0,                               // Evento ano (pode ser definido ou nulo, dependendo da lógica)
                        rs.getString("application_date"),// Data da inscrição
                        rs.getString("athlete_country"), // País do atleta
                        rs.getString("athlete_genre")    // Adicionando o gênero do atleta
                );
                registrations.add(registration);
            }
        }

        return registrations;
    }

    /**
     * Aprova a inscrição de um atleta numa equipa, alterando o status da inscrição para 'approved'.
     * Após a aprovação, o atleta é automaticamente inserido na tabela de membros da equipa através de uma trigger.
     *
     * @param applicationId O ID da inscrição a ser aprovada.
     * @return verdadeiro se a inscrição foi aprovada com sucesso, falso caso contrário.
     */
    @Override
    public boolean aproveAthlete(int applicationId, int teamId) {  // Agora aceita o ID da equipe
        String updateQuery = "UPDATE TeamApplications SET status = 'approved', team_id = ? " +
                "WHERE id = ? AND status = 'pending' AND inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, teamId);  // Define o ID da equipe
            stmt.setInt(2, applicationId);  // Passa o ID da inscrição

            int rowsAffected = stmt.executeUpdate();

            // Verifica se pelo menos uma linha foi atualizada
            if (rowsAffected > 0) {
                return true; // A inscrição foi aprovada e associada à equipe
            } else {
                return false; // Não houve alteração (pode ocorrer se o status não estava 'pending')
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false se ocorrer algum erro
        }
    }

    /**
     * Rejeita a inscrição de um atleta numa equipa, alterando o status da inscrição para 'rejected'.
     *
     * @param applicationId O ID da inscrição a ser rejeitada.
     * @return verdadeiro se a inscrição foi rejeitada com sucesso, falso caso contrário.
     */
    @Override
    public boolean rejectAthlete(int applicationId) {
        String updateQuery = "UPDATE TeamApplications SET status = 'rejected' WHERE id = ? AND status = 'pending' AND inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, applicationId);  // Passa o ID da inscrição
            int rowsAffected = stmt.executeUpdate();

            // Retorna true se a inscrição foi rejeitada com sucesso
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false se ocorrer algum erro
        }
    }

    /**
     * Regista um atleta numa modalidade específica, com o status 'approved'.
     * Insere uma nova entrada na tabela AthleteRegistrations com o ID do atleta, a modalidade e o evento.
     *
     * @param athleteId O ID do atleta a ser registado.
     * @param sportId   O ID da modalidade à qual o atleta se está a inscrever.
     * @return verdadeiro se o atleta foi registado com sucesso, falso caso contrário.
     */
    public boolean registerAthlete(int athleteId, int sportId) {
        // Query para verificar se o gênero do atleta e do esporte são compatíveis
        String validationQuery = """
                    SELECT COUNT(*)
                    FROM Athletes a
                    JOIN Sports s ON a.genre = s.genre
                    WHERE a.id = ? AND s.id = ? AND a.inative = 0 AND s.inative = 0
                """;

        // Query para realizar a inscrição
        String insertQuery = """
                    INSERT INTO AthleteRegistrations (athlete_id, sport_id, status, inative, event_id)
                    VALUES (?, ?, 'approved', 'false', '1')
                """;

        try (Connection conn = DBConnection.getConnection()) {
            // Validar compatibilidade de gênero
            try (PreparedStatement validationStmt = conn.prepareStatement(validationQuery)) {
                validationStmt.setInt(1, athleteId);
                validationStmt.setInt(2, sportId);

                try (ResultSet rs = validationStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // Gêneros incompatíveis ou atleta ou esporte inativo
                        return false;
                    }
                }
            }

            // Realizar a inscrição
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, athleteId);
                insertStmt.setInt(2, sportId);

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasTimeConflict(int athleteId, int sportId) {
        String query = """
        SELECT COUNT(*)
        FROM AthleteRegistrations ar
        JOIN SportEvents se ON ar.sport_id = se.sport_id
        WHERE ar.athlete_id = ? AND ar.inative = 0
        AND (
            (se.start_time BETWEEN DATEADD(HOUR, -2, (SELECT start_time FROM SportEvents WHERE sport_id = ?)) AND DATEADD(HOUR, 2, (SELECT start_time FROM SportEvents WHERE sport_id = ?)))
            OR
            (se.end_time BETWEEN DATEADD(HOUR, -2, (SELECT start_time FROM SportEvents WHERE sport_id = ?)) AND DATEADD(HOUR, 2, (SELECT start_time FROM SportEvents WHERE sport_id = ?)))
        )
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            stmt.setInt(2, sportId);
            stmt.setInt(3, sportId);
            stmt.setInt(4, sportId);
            stmt.setInt(5, sportId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Método para obter o país da equipe a partir do ID da equipe
    public String getTeamCountry(int teamId) {
        String teamCountry = null;

        String query = "SELECT country FROM Teams WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teamId);  // Passa o ID da equipe
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                teamCountry = rs.getString("country");  // Recupera o país
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teamCountry;
    }

    // Método para obter o país do atleta a partir da inscrição
    public String getAthleteCountry(int applicationId) {
        String athleteCountry = null;

        String query = "SELECT a.country FROM Athletes a " +
                "INNER JOIN TeamApplications ta ON ta.athlete_id = a.id " +
                "WHERE ta.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicationId);  // Passa o ID da inscrição
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                athleteCountry = rs.getString("country");  // Recupera o país do atleta
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return athleteCountry;
    }

    public int getTeamSportId(int teamId) {
        String query = "SELECT sports_id FROM Teams WHERE id = ?";
        int teamSportId = -1;  // Valor padrão indicando que não foi encontrado

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                teamSportId = rs.getInt("sports_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teamSportId;
    }

    public ObservableList<AthleteRegistration> getIndividualInscriptions() throws SQLException {
        ObservableList<AthleteRegistration> individualInscriptions = FXCollections.observableArrayList();

        String query = "SELECT ar.id AS registration_id, " +
                "       a.id AS athlete_id, " +
                "       a.name AS athlete_name, " +
                "       a.country AS athlete_country, " +
                "       a.genre AS athlete_genre, " +
                "       s.id AS sport_id, " +
                "       s.name AS sport_name, " +
                "       ar.status AS registration_status, " +
                "       ar.inative AS registration_inative " +
                "FROM AthleteRegistrations ar " +
                "INNER JOIN Athletes a ON ar.athlete_id = a.id " +
                "INNER JOIN Sports s ON ar.sport_id = s.id " +
                "WHERE s.type = 'Individual' AND " +
                "      ar.inative = 0 AND a.inative = 0 AND s.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AthleteRegistration registration = new AthleteRegistration(
                        rs.getInt("registration_id"),
                        rs.getInt("athlete_id"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("sport_id"),
                        rs.getString("sport_name"),
                        rs.getString("registration_status"),
                        rs.getBoolean("registration_inative")
                );
                individualInscriptions.add(registration);
            }
        }

        return individualInscriptions;
    }

    public boolean deactivateRegistration(int id) throws SQLException {
        String query = "UPDATE AthleteRegistrations SET inative = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean activateRegistration(int id) throws SQLException {
        String query = "UPDATE AthleteRegistrations SET inative = 0 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }


    public static List<Calendar> getCalendarByAthleteId(int athleteId) {
        List<Calendar> calendar = new ArrayList<>();
        String query = "SELECT " +
                "se.start_time AS inicio, " +
                "se.end_time AS fim, " +
                "s.name AS modalidade, " +
                "l.address AS local, " +
                "ar.inative AS inativo, " +
                "e.year AS ano, " +
                "s.name AS nome_desporto " +
                "FROM AthleteRegistrations ar " +
                "JOIN Sports s ON ar.sport_id = s.id " +
                "JOIN SportEvents se ON s.id = se.sport_id " +
                "JOIN Events e ON se.event_id = e.id " +
                "JOIN Locations l ON se.local = l.id " +
                "WHERE ar.athlete_id = ? " +
                "AND ar.inative = 0 " +
                "ORDER BY se.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, athleteId); // Configura o ID do atleta no parâmetro

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Cria uma entrada de calendário com os dados retornados
                    Calendar calendarEntry = new Calendar(
                            rs.getString("inicio"),       // Horário de início
                            rs.getString("fim"),          // Horário de fim
                            rs.getString("modalidade"),   // Nome da modalidade
                            rs.getString("local"),        // Endereço/local do evento
                            rs.getBoolean("inativo"),     // Status inativo
                            rs.getInt("ano"),             // Ano do evento
                            rs.getString("nome_desporto") // Nome do desporto
                    );
                    calendar.add(calendarEntry); // Adiciona à lista
                }
            }

        } catch (SQLException e) {
            // Log de erro para depuração
            e.printStackTrace();
        }

        return calendar; // Retorna a lista de entradas
    }

    public static List<Calendar> getAllCalendars() {
        List<Calendar> calendar = new ArrayList<>();
        String query = "SELECT " +
                "se.start_time AS inicio, " +
                "se.end_time AS fim, " +
                "s.name AS modalidade, " +
                "l.address AS local, " +
                "e.year AS ano, " +
                "s.name AS nome_desporto " +
                "FROM SportEvents se " +
                "JOIN Sports s ON se.sport_id = s.id " +
                "JOIN Events e ON se.event_id = e.id " +
                "JOIN Locations l ON se.local = l.id " +
                "WHERE se.inative = 'false' " +
                "ORDER BY se.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Calendar calendarEntry = new Calendar(
                        rs.getString("inicio"),
                        rs.getString("fim"),
                        rs.getString("modalidade"),
                        rs.getString("local"),
                        false, // Assumindo que o campo inativo não é mais necessário
                        rs.getInt("ano"),
                        rs.getString("nome_desporto")
                );
                calendar.add(calendarEntry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public boolean insertCalendar(Calendar calendar) {
        String query = "INSERT INTO SportEvents (start_time, end_time, sport_id, local, event_id, inative) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, calendar.getStartTime());
            stmt.setString(2, calendar.getEndTime());
            stmt.setInt(3, calendar.getSportId());
            stmt.setInt(4, calendar.getLocationId());
            stmt.setInt(5, calendar.getEventId());
            stmt.setBoolean(6, calendar.isInactive());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        calendar.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir evento no calendário: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCalendar(Calendar calendar) {
        String query = "UPDATE SportEvents SET start_time = ?, end_time = ?, sport_id = ?, local = ?, event_id = ?, inative = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, calendar.getStartTime());
            stmt.setString(2, calendar.getEndTime());
            stmt.setInt(3, calendar.getSportId());
            stmt.setInt(4, calendar.getLocationId());
            stmt.setInt(5, calendar.getEventId());
            stmt.setBoolean(6, calendar.isInactive());
            stmt.setInt(7, calendar.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento no calendário: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCalendar(int calendarId) {
        String query = "DELETE FROM SportEvents WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, calendarId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir evento no calendário: " + e.getMessage());
            return false;
        }
    }

    public Calendar findById(int id) {
        String query = "SELECT se.id, se.start_time, se.end_time, se.sport_id, se.local, se.event_id, se.inative " +
                "FROM SportEvents se " +
                "WHERE se.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Calendar(
                        rs.getInt("id"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("sport_id"),
                        rs.getInt("local"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar evento no calendário por ID: " + e.getMessage());
        }
        return null;
    }


    public Integer findMatchingTeam(int sportId, String country, String gender) {
        String query = """
        SELECT t.id FROM Teams t
        JOIN Events e ON e.year = t.foundationYear
        WHERE t.sports_id = ? 
        AND t.country = ? 
        AND t.genre = ? 
        AND t.inative = 0
        AND e.inative = 0
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sportId);
            stmt.setString(2, country);
            stmt.setString(3, gender);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Retorna o ID da equipe encontrada
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se nenhuma equipe correspondente for encontrada
    }

    public int countApprovedAthletes(int sportId, String country, String gender) {
        String sql = "SELECT COUNT(*) " +
                "FROM teamApplications ta " +
                "JOIN Athletes a ON ta.athlete_id = a.id " +
                "WHERE ta.sport_id = ? " +
                "AND a.country = ? " +
                "AND a.genre = ? " +
                "AND ta.status = 'approved'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            stmt.setString(2, country);
            stmt.setString(3, gender);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMinAthletesRequired(int sportId) {
        String sql = "SELECT minParticipants FROM sports WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE; // Valor alto para evitar bloqueios errados
    }

    public void deactivatePendingInscriptions(int sportId, String country, String gender) {
        String sql = "UPDATE teamApplications ta " +
                "JOIN Athletes a ON ta.athlete_id = a.id " + // JOIN para pegar país e gênero do atleta
                "SET ta.status = 'inactive' " +
                "WHERE ta.sport_id = ? " +
                "AND a.country = ? " +
                "AND a.gender = ? " +
                "AND ta.status = 'pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            stmt.setString(2, country);
            stmt.setString(3, gender);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<String> getTeamsByCountry(String country) {
        String query = "SELECT id, name, sport, country FROM Teams WHERE inative = 0 AND country = ?";
        ObservableList<String> teams = FXCollections.observableArrayList();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int teamId = rs.getInt("id");
                String teamName = rs.getString("name");
                String sport = rs.getString("sport");
                String teamCountry = rs.getString("country");
                teams.add(teamId + " - " + teamName + " (" + sport + ", " + teamCountry + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }



}
