package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ISportDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.*;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de implementação de acesso a dados (DAO) para a tabela "Sports".
 * Responsável por interagir com o banco de dados.
 */
public class SportDAO implements ISportDAO {

    /**
     * Insere uma nova modalidade esportiva no banco de dados.
     *
     * @param sport O objeto Sport a ser inserido no banco de dados.
     * @return `true` se a inserção foi bem-sucedida, `false` caso contrário.
     * @throws SQLException Se ocorrer algum erro durante a inserção.
     */
    @Override
    public boolean insertSport(Sport sport, String rule) throws SQLException {
        String querySport = "INSERT INTO Sports (name, type, genre, description, minParticipants, scoringMeasure, oneGame, olympicRecord_time, olympicRecord_year, olympicRecord_holder, winnerOlympic_year, winnerOlympic_time, winnerOlympic_holder, inative) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        String queryRule = "INSERT INTO Rules (sport_id, [rule], inative) VALUES (?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtSport = conn.prepareStatement(querySport, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtRule = conn.prepareStatement(queryRule)) {

            // Inserção do desporto
            stmtSport.setString(1, sport.getName());
            stmtSport.setString(2, sport.getType());
            stmtSport.setString(3, sport.getGenre());
            stmtSport.setString(4, sport.getDescription());
            stmtSport.setInt(5, sport.getMinParticipants());
            stmtSport.setString(6, sport.getScoringMeasure());
            stmtSport.setString(7, sport.getOneGame());
            stmtSport.setString(8, sport.getOlympicRecordTime());
            stmtSport.setInt(9, sport.getOlympicRecordYear());
            stmtSport.setString(10, sport.getOlympicRecordHolder());
            stmtSport.setInt(11, sport.getWinnerOlympicYear());
            stmtSport.setString(12, sport.getWinnerOlympicTime());
            stmtSport.setString(13, sport.getWinnerOlympicHolder());

            int rowsAffectedSport = stmtSport.executeUpdate();
            if (rowsAffectedSport > 0) {
                // Obtém o ID gerado para o desporto inserido
                ResultSet generatedKeys = stmtSport.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int sportId = generatedKeys.getInt(1);
                    sport.setId(sportId); // Define o ID gerado no objeto Sport

                    // Inserção da regra associada ao desporto
                    stmtRule.setInt(1, sportId);
                    stmtRule.setString(2, rule);
                    int rowsAffectedRule = stmtRule.executeUpdate();

                    return rowsAffectedRule > 0; // Se a regra for inserida com sucesso
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Caso haja erro na execução
        }
    }

    @Override
    public List<Sport> findAll() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT * FROM Sports WHERE inative = 0 AND type = 'Individual' "; // Apenas registros não deletados

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("genre"),
                        rs.getString("description"),
                        rs.getInt("minParticipants"),
                        rs.getString("scoringMeasure"),
                        rs.getString("oneGame"),
                        rs.getString("olympicRecord_time"),
                        rs.getInt("olympicRecord_year"),
                        rs.getString("olympicRecord_holder"),
                        rs.getInt("winnerOlympic_year"),
                        rs.getString("winnerOlympic_time"),
                        rs.getString("winnerOlympic_holder"),
                        rs.getBoolean("inative")
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }

    @Override
    public List<Sport> findAllColletive() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT * FROM Sports WHERE inative = 0 AND type = 'Collective' "; // Apenas registros não deletados

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("genre"),
                        rs.getString("description"),
                        rs.getInt("minParticipants"),
                        rs.getString("scoringMeasure"),
                        rs.getString("oneGame"),
                        rs.getString("olympicRecord_time"),
                        rs.getInt("olympicRecord_year"),
                        rs.getString("olympicRecord_holder"),
                        rs.getInt("winnerOlympic_year"),
                        rs.getString("winnerOlympic_time"),
                        rs.getString("winnerOlympic_holder"),
                        rs.getBoolean("inative")
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }
    @Override
    public List<Sport> findAllColletiveInative() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT * FROM Sports WHERE inative = 1 AND type = 'Collective' "; // Apenas registros não deletados

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("genre"),
                        rs.getString("description"),
                        rs.getInt("minParticipants"),
                        rs.getString("scoringMeasure"),
                        rs.getString("oneGame"),
                        rs.getString("olympicRecord_time"),
                        rs.getInt("olympicRecord_year"),
                        rs.getString("olympicRecord_holder"),
                        rs.getInt("winnerOlympic_year"),
                        rs.getString("winnerOlympic_time"),
                        rs.getString("winnerOlympic_holder"),
                        rs.getBoolean("inative")
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }
    @Override
    public List<Sport> findAllForGenerator() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT * FROM Sports WHERE inative = 0"; // Apenas registros não deletados

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("genre"),
                        rs.getString("description"),
                        rs.getInt("minParticipants"),
                        rs.getString("scoringMeasure"),
                        rs.getString("oneGame"),
                        rs.getString("olympicRecord_time"),
                        rs.getInt("olympicRecord_year"),
                        rs.getString("olympicRecord_holder"),
                        rs.getInt("winnerOlympic_year"),
                        rs.getString("winnerOlympic_time"),
                        rs.getString("winnerOlympic_holder"),
                        rs.getBoolean("inative")
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }

    //findAll1 inclui as regras dos desportos
    public List<Sport> findAll1() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT " +
                "s.id AS sport_id, " +
                "s.name AS sport_name, " +
                "s.type AS sport_type, " +
                "s.genre AS sport_genre, " +
                "s.description AS sport_description, " +
                "s.minParticipants AS sport_minParticipants, " +
                "s.scoringMeasure AS sport_scoringMeasure, " +
                "s.oneGame AS sport_oneGame, " +
                "s.olympicRecord_time AS sport_olympicRecord_time, " +
                "s.olympicRecord_year AS sport_olympicRecord_year, " +
                "s.olympicRecord_holder AS sport_olympicRecord_holder, " +
                "s.winnerOlympic_time AS sport_winnerOlympic_time, " +
                "s.winnerOlympic_year AS sport_winnerOlympic_year, " +
                "s.winnerOlympic_holder AS sport_winnerOlympic_holder, " +
                "s.inative AS sport_inative, " +
                "(SELECT STUFF((SELECT ', ' + CAST(r.[rule] AS VARCHAR(MAX)) " +
                " FROM Rules r " +
                " WHERE r.sport_id = s.id " +
                " FOR XML PATH('')), 1, 2, '')) AS sport_rules " +
                "FROM Sports s";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("sport_id"),
                        rs.getString("sport_name"),
                        rs.getString("sport_type"),
                        rs.getString("sport_genre"),
                        rs.getString("sport_description"),
                        rs.getInt("sport_minParticipants"),
                        rs.getString("sport_scoringMeasure"),
                        rs.getString("sport_oneGame"),
                        rs.getString("sport_olympicRecord_time"),
                        rs.getInt("sport_olympicRecord_year"),
                        rs.getString("sport_olympicRecord_holder"),
                        rs.getInt("sport_winnerOlympic_year"),
                        rs.getString("sport_winnerOlympic_time"),
                        rs.getString("sport_winnerOlympic_holder"),
                        rs.getBoolean("sport_inative"),
                        rs.getString("sport_rules")  // Adicionando todas as regras concatenadas
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }


    //findAllEvcent para apenas retornar os deportos relativos ao evento ativo
    public List<Sport> findAllEvent() {
        List<Sport> sports = new ArrayList<>();
        String query = "SELECT " +
                "s.id AS sport_id, " +
                "s.name AS sport_name, " +
                "s.type AS sport_type, " +
                "s.genre AS sport_genre, " +
                "s.description AS sport_description, " +
                "s.minParticipants AS sport_minParticipants, " +
                "s.scoringMeasure AS sport_scoringMeasure, " +
                "s.oneGame AS sport_oneGame, " +
                "s.olympicRecord_time AS sport_olympicRecord_time, " +
                "s.olympicRecord_year AS sport_olympicRecord_year, " +
                "s.olympicRecord_holder AS sport_olympicRecord_holder, " +
                "s.winnerOlympic_time AS sport_winnerOlympic_time, " +
                "s.winnerOlympic_year AS sport_winnerOlympic_year, " +
                "s.winnerOlympic_holder AS sport_winnerOlympic_holder, " +
                "s.inative AS sport_inative " +
                "FROM Sports s " +
                "WHERE s.inative = 0";  // Filtra apenas os desportos ativos

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("sport_id"),
                        rs.getString("sport_name"),
                        rs.getString("sport_type"),
                        rs.getString("sport_genre"),
                        rs.getString("sport_description"),
                        rs.getInt("sport_minParticipants"),
                        rs.getString("sport_scoringMeasure"),
                        rs.getString("sport_oneGame"),
                        rs.getString("sport_olympicRecord_time"),
                        rs.getInt("sport_olympicRecord_year"),
                        rs.getString("sport_olympicRecord_holder"),
                        rs.getInt("sport_winnerOlympic_year"),
                        rs.getString("sport_winnerOlympic_time"),
                        rs.getString("sport_winnerOlympic_holder"),
                        rs.getBoolean("sport_inative")
                );
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }

    @Override
    public boolean deleteSport(int id) throws SQLException {
        String query = "UPDATE Sports SET inative = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    @Override
    public boolean activateSport(int id) throws SQLException {
        String query = "UPDATE Sports SET inative = 0 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }



    @Override
    public List<Event> getActiveEvents() throws SQLException {
        String query = "SELECT * FROM Events WHERE inative = 0";
        List<Event> events = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();
              PreparedStatement stmt =conn.prepareStatement(query);
              ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(rs.getInt("id"), rs.getInt("year"), rs.getString("country"),
                        rs.getString("mascot"), rs.getBoolean("inative")));
            }
        }
        return events;
    }

    public int insertSportEvent(Sport sport, int eventId, LocalDateTime startTime, LocalDateTime endTime, int locationId) throws SQLException {
        String insertQuery = "INSERT INTO SportEvents (sport_id, start_time, end_time, local, event_id, inative) VALUES (?, ?, ?, ?, ?, 0)";
        int scheduleId = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, sport.getId());
            insertStmt.setTimestamp(2, Timestamp.valueOf(startTime));
            insertStmt.setTimestamp(3, Timestamp.valueOf(endTime));
            insertStmt.setInt(4, locationId);
            insertStmt.setInt(5, eventId);
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    scheduleId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter o scheduleId gerado.");
                }
            }
        }
        return scheduleId;
    }



    @Override
    public List<Athlete> getAthletesBySport(int sportId) throws SQLException {
        // Agora a consulta vai buscar os atletas na tabela AthleteRegistrations
        String query = "SELECT Athletes.* FROM Athletes " +
                "JOIN AthleteRegistrations ON Athletes.id = AthleteRegistrations.athlete_id " +
                "WHERE AthleteRegistrations.sport_id = ? AND Athletes.inative = 0 " +
                "AND AthleteRegistrations.inative = 0";
        List<Athlete> athletes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sportId);  // Definir o sport_id desejado
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Remover a parte do age, já que não é necessário
                    athletes.add(new Athlete(rs.getInt("id"), rs.getString("name"),
                            rs.getString("country")));  // Construtor atualizado
                }
            }
        }
        return athletes;
    }




    @Override
    public List<Team> getTeamsBySport(int sportId) throws SQLException {
        String query = "SELECT DISTINCT t.* " +
                "FROM Teams t " +
                "JOIN TeamApplications ta ON t.id = ta.team_id " +
                "WHERE t.sports_id = ? " +
                "AND t.inative = 0 " +
                "AND ta.status = 'approved' " +
                "AND ta.inative = 0";

        List<Team> teams = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sportId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(new Team(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("country"),
                            rs.getString("genre"),
                            rs.getInt("sports_id"),
                            rs.getInt("foundationYear")
                    ));
                }
            }
        }
        return teams;
    }

    @Override
    public void saveIndividualParticipation(OlympicParticipation participation) throws SQLException {

    }


    @Override
    public void saveIndividualParticipation(OlympicParticipation participation, String medal) throws SQLException {
        // Query de inserção com medalhas
        String insertQuery = "INSERT INTO OlympicParticipations (year, result, athlete_id, event_id, inative, gold, silver, bronze, diploma, sport_id) VALUES (?, ?, ?, ?, 0, ?, ?, ?, ?,?)";
        String updateAthleteQuery = "UPDATE AthleteRegistrations SET inative = 1 WHERE athlete_id = ? AND sport_id = ?";

        // Iniciar a conexão
        try (Connection conn = DBConnection.getConnection()) {
            // Iniciar transação
            conn.setAutoCommit(false); // Desativa o autocommit para gerenciar transações manualmente

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                // Inserção da participação
                insertStmt.setInt(1, participation.getYear()); // Ano
                insertStmt.setString(2, participation.getResult()); // Resultado
                insertStmt.setInt(3, participation.getAthleteId()); // ID do atleta
                insertStmt.setInt(4, participation.getEventId()); // ID do evento

                // Atribuindo os valores para as medalhas
                int gold = 0, silver = 0, bronze = 0, diploma = 0;
                switch (medal) {
                    case "Gold":
                        gold = 1;
                        break;
                    case "Silver":
                        silver = 1;
                        break;
                    case "Bronze":
                        bronze = 1;
                        break;
                    case "Diploma":
                        diploma = 1;
                        break;
                }

                // Definindo os valores das medalhas na consulta
                insertStmt.setInt(5, gold); // Gold
                insertStmt.setInt(6, silver); // Silver
                insertStmt.setInt(7, bronze); // Bronze
                insertStmt.setInt(8, diploma); // Diploma
                insertStmt.setInt(9, participation.getSportId());

                // Executa a inserção da participação
                insertStmt.executeUpdate();
            }

            // Marcar o registro do atleta como inativo
            try (PreparedStatement updateAthleteStmt = conn.prepareStatement(updateAthleteQuery)) {
                updateAthleteStmt.setInt(1, participation.getAthleteId()); // ID do atleta
                updateAthleteStmt.setInt(2, participation.getSportId()); // ID do esporte

                // Executar o update e verificar se foi aplicado corretamente
                int rowsUpdated = updateAthleteStmt.executeUpdate();

                if (rowsUpdated == 0) {
                }
            }

            // Commit da transação
            conn.commit();
        } catch (SQLException e) {
            // Rollback em caso de erro
            e.printStackTrace();
            try (Connection conn = DBConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                e.printStackTrace();
            }
            throw e; // Re-throw da exceção original
        }
    }

    @Override
    public void saveTeamParticipation(TeamOlympicParticipation participation) throws SQLException {

    }

    @Override
    public void saveTeamParticipation(TeamOlympicParticipation participation, Event event, String medal) throws SQLException {
        String insertQuery = "INSERT INTO TeamOlympicParticipations (year, result, resultTDP, team_id, event_id, inative) VALUES (?, ?, ?, ?, ?, 0)";
        String updateSportQuery = "UPDATE TeamApplications SET inative = 1 WHERE athlete_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Inserção da participação
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, participation.getYear()); // Ano
                insertStmt.setString(2, medal); // Medalha (Gold, Silver, Bronze ou Diploma)
                insertStmt.setString(3, participation.getResultTPD()); // Resultado (TPD)
                insertStmt.setInt(4, participation.getTeamId()); // ID da equipe
                insertStmt.setInt(5, event.getId()); // ID do evento
                insertStmt.executeUpdate();
            }

            // Marcar as inscrições da equipe como inativas
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSportQuery)) {
                updateStmt.setInt(1, participation.getTeamId());
                updateStmt.executeUpdate();
            }
        }
    }

    public boolean updateSport(Sport sport, String rule) throws SQLException {
        // Atualiza as informações do esporte na tabela Sports
        String sql = "UPDATE Sports SET type = ?, description = ?, minParticipants = ?, scoringMeasure = ?, " +
                "oneGame = ?, olympicRecord_time = ?, olympicRecord_year = ?, olympicRecord_holder = ?, " +
                "winnerOlympic_year = ?, winnerOlympic_time = ?, winnerOlympic_holder = ? WHERE name = ?";

        // Atualiza a regra associada ao esporte na tabela Rules
        String sqlRule = "UPDATE Rules SET [rule] = ? WHERE sport_id = (SELECT TOP 1 id FROM Sports WHERE name = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtSport = conn.prepareStatement(sql);
             PreparedStatement stmtRule = conn.prepareStatement(sqlRule)) {

            // Atualização do esporte
            stmtSport.setString(1, sport.getType());
            stmtSport.setString(2, sport.getDescription());
            stmtSport.setInt(3, sport.getMinParticipants());
            stmtSport.setString(4, sport.getScoringMeasure());
            stmtSport.setString(5, sport.getOneGame());
            stmtSport.setString(6, sport.getOlympicRecordTime());
            stmtSport.setInt(7, sport.getOlympicRecordYear());
            stmtSport.setString(8, sport.getOlympicRecordHolder());
            stmtSport.setInt(9, sport.getWinnerOlympicYear());
            stmtSport.setString(10, sport.getWinnerOlympicTime());
            stmtSport.setString(11, sport.getWinnerOlympicHolder());
            stmtSport.setString(12, sport.getName());

            int rowsUpdatedSport = stmtSport.executeUpdate();

            // Atualização da regra associada ao esporte
            stmtRule.setString(1, rule); // A nova regra
            stmtRule.setString(2, sport.getName()); // Usando o nome do esporte para encontrar o id

            int rowsUpdatedRule = stmtRule.executeUpdate();

            // Retorna true se ambas as atualizações ocorrerem com sucesso
            return rowsUpdatedSport > 0 && rowsUpdatedRule > 0;
        }
    }

    public boolean checkMinParticipants(int sportId) throws SQLException {
        String query = "SELECT COUNT(*) AS teamCount, s.minParticipants " +
                "FROM Teams t " +
                "JOIN Sports s ON t.sports_id = s.id " +
                "WHERE t.sports_id = ? AND t.inative = 0 " +
                "GROUP BY s.minParticipants"; // Agora inclui minParticipants no GROUP BY

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sportId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int teamCount = rs.getInt("teamCount");
                int minParticipants = rs.getInt("minParticipants");
                return teamCount >= minParticipants; // Verifica se o número de equipes é suficiente
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar número de equipes: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public boolean checkMinParticipantsForIndividual(int sportId) throws SQLException {
        String query = "SELECT COUNT(*) AS num_participants, s.minParticipants "
                + "FROM AthleteRegistrations ar "
                + "JOIN Sports s ON ar.sport_id = s.id "
                + "WHERE ar.sport_id = ? AND ar.status = 'approved' AND ar.inative = 0 "
                + "GROUP BY s.minParticipants "
                + "HAVING COUNT(*) >= s.minParticipants";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sportId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Se houver pelo menos uma linha no resultado e o número de participantes for suficiente
                    return true;
                } else {
                    return false;  // Caso contrário, o número de participantes não foi atingido
                }
            }
        }
    }




    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Carregar XML **/

    @Override
    public void insertOrUpdateSports(List<Sport> sports) {
        for (Sport sport : sports) {
            if (sportExists(sport.getName(), sport.getGenre())) {
                updateSport(sport);
            } else {
                insertSport(sport);
            }
        }
    }

    @Override
    public boolean sportExists(String name, String genre) {
        String query = "SELECT COUNT(*) FROM Sports WHERE name = ? AND genre = ?";
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
    public void updateSport(Sport sport) {
        String updateSportSQL = "UPDATE Sports SET type = ?, description = ?, minParticipants = ?, scoringMeasure = ?, oneGame = ?, olympicRecord_time = ?, olympicRecord_year = ?, olympicRecord_holder = ?, winnerOlympic_time = ?, winnerOlympic_year = ?, winnerOlympic_holder = ? WHERE name = ? AND genre = ?";
        String deleteRulesSQL = "DELETE FROM Rules WHERE sport_id = (SELECT TOP 1 id FROM Sports WHERE name = ? AND genre = ?)";
        String insertRuleSQL = "INSERT INTO Rules (sport_id, [rule], inative) VALUES ((SELECT TOP 1 id FROM Sports WHERE name = ? AND genre = ?), ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement sportStmt = conn.prepareStatement(updateSportSQL);
             PreparedStatement deleteRulesStmt = conn.prepareStatement(deleteRulesSQL);
             PreparedStatement insertRuleStmt = conn.prepareStatement(insertRuleSQL)) {

            // Atualizar o desporto
            sportStmt.setString(1, sport.getType());
            sportStmt.setString(2, sport.getDescription());
            sportStmt.setInt(3, sport.getMinParticipants());
            sportStmt.setString(4, sport.getScoringMeasure());
            sportStmt.setString(5, sport.getOneGame());
            sportStmt.setString(6, sport.getOlympicRecordTime());
            sportStmt.setInt(7, sport.getOlympicRecordYear());
            sportStmt.setString(8, sport.getOlympicRecordHolder());
            sportStmt.setString(9, sport.getWinnerOlympicTime());
            sportStmt.setInt(10, sport.getWinnerOlympicYear());
            sportStmt.setString(11, sport.getWinnerOlympicHolder());
            sportStmt.setString(12, sport.getName());
            sportStmt.setString(13, sport.getGenre());
            sportStmt.executeUpdate();

            // Deletar regras antigas
            deleteRulesStmt.setString(1, sport.getName());
            deleteRulesStmt.setString(2, sport.getGenre());
            deleteRulesStmt.executeUpdate();

            // Inserir novas regras
            for (Rule rule : sport.getRules()) {
                insertRuleStmt.setString(1, sport.getName());
                insertRuleStmt.setString(2, sport.getGenre());
                insertRuleStmt.setString(3, rule.getRule());
                insertRuleStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertSport(Sport sport) {
        String insertSportSQL = "INSERT INTO Sports (type, genre, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord_time, olympicRecord_year, olympicRecord_holder, winnerOlympic_time, winnerOlympic_year, winnerOlympic_holder, inative) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        String insertRuleSQL = "INSERT INTO Rules (sport_id, [rule], inative) VALUES ((SELECT TOP 1 id FROM Sports WHERE name = ? AND genre = ?), ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement sportStmt = conn.prepareStatement(insertSportSQL);
             PreparedStatement insertRuleStmt = conn.prepareStatement(insertRuleSQL)) {

            // Inserir o desporto
            sportStmt.setString(1, sport.getType());
            sportStmt.setString(2, sport.getGenre());
            sportStmt.setString(3, sport.getName());
            sportStmt.setString(4, sport.getDescription());
            sportStmt.setInt(5, sport.getMinParticipants());
            sportStmt.setString(6, sport.getScoringMeasure());
            sportStmt.setString(7, sport.getOneGame());
            sportStmt.setString(8, sport.getOlympicRecordTime());
            sportStmt.setInt(9, sport.getOlympicRecordYear());
            sportStmt.setString(10, sport.getOlympicRecordHolder());
            sportStmt.setString(11, sport.getWinnerOlympicTime());
            sportStmt.setInt(12, sport.getWinnerOlympicYear());
            sportStmt.setString(13, sport.getWinnerOlympicHolder());
            sportStmt.executeUpdate();

            // Inserir regras associadas ao desporto
            for (Rule rule : sport.getRules()) {
                insertRuleStmt.setString(1, sport.getName());
                insertRuleStmt.setString(2, sport.getGenre());
                insertRuleStmt.setString(3, rule.getRule());
                insertRuleStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public String getSportName(int sportId) {
        String sql = "SELECT name FROM sports WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Sport"; // Retorna um nome genérico caso não encontre
    }



    /**
     * Marca um desporto como inativo.
     *
     * @param sportId O ID do desporto.
     * @return true se a operação for bem-sucedida, false caso contrário.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    public boolean markSportAsInactive(int sportId) throws SQLException {
        String query = "UPDATE Sports SET inative = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, sportId);
            return statement.executeUpdate() > 0;
        }
    }

    public double[] getTimeInterval() throws SQLException {
        return getInterval("Time");
    }

    public double[] getDistanceInterval() throws SQLException {
        return getInterval("Distance");
    }

    public double[] getPointsInterval() throws SQLException {
        return getInterval("Points");
    }

    public double[] getInterval(String type) throws SQLException {
        String query = "SELECT min_value, max_value FROM ValueIntervals WHERE type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new double[]{rs.getDouble("min_value"), rs.getDouble("max_value")};
                } else {
                    throw new SQLException("Intervalo não encontrado para o tipo: " + type);
                }
            }
        }
    }

    public void updateInterval(String type, double minValue, double maxValue) throws SQLException {
        String query = "UPDATE ValueIntervals SET min_value = ?, max_value = ? WHERE type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, minValue);
            stmt.setDouble(2, maxValue);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }
    // Método para atualizar o vencedor olímpico
    public void updateWinnerOlympic(int sportId, String winnerTime, int winnerYear, String winnerHolder) throws SQLException {
        String sql = "UPDATE Sports SET winnerOlympic_time = ?, winnerOlympic_year = ?, winnerOlympic_holder = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, winnerTime);
            pstmt.setInt(2, winnerYear);
            pstmt.setString(3, winnerHolder);
            pstmt.setInt(4, sportId);
            pstmt.executeUpdate();
        }
    }

    // Método para atualizar o recorde olímpico
    public void updateOlympicRecord(int sportId, String recordTime, int recordYear, String recordHolder) throws SQLException {
        String sql = "UPDATE Sports SET olympicRecord_time = ?, olympicRecord_year = ?, olympicRecord_holder = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recordTime);
            pstmt.setInt(2, recordYear);
            pstmt.setString(3, recordHolder);
            pstmt.setInt(4, sportId);
            pstmt.executeUpdate();
        }
    }
    public Sport getSportById(int sportId) {
        String query = "SELECT * FROM Sports WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sportId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return new Sport(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("genre"),
                        resultSet.getString("description"),
                        resultSet.getInt("minParticipants"),
                        resultSet.getString("scoringMeasure"),
                        resultSet.getString("oneGame"),
                        resultSet.getString("olympicRecord_time"),
                        resultSet.getInt("olympicRecord_year"),
                        resultSet.getString("olympicRecord_holder"),
                        resultSet.getInt("winnerOlympic_year"),
                        resultSet.getString("winnerOlympic_time"),
                        resultSet.getString("winnerOlympic_holder"),
                        resultSet.getBoolean("inative")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar desporto por ID. Query: " + query + ". Erro: " + e.getMessage());
        }

        return null;
    }

    public boolean realDeleteSport(int sportId) {
        String deleteRulesQuery = "DELETE FROM Rules WHERE sport_id = ?";
        String deleteSportQuery = "DELETE FROM Sports WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtDeleteRules = conn.prepareStatement(deleteRulesQuery);
             PreparedStatement stmtDeleteSport = conn.prepareStatement(deleteSportQuery)) {

            // Excluir regras associadas ao desporto, se existirem
            stmtDeleteRules.setInt(1, sportId);
            stmtDeleteRules.executeUpdate();

            // Excluir o desporto
            stmtDeleteSport.setInt(1, sportId);
            return stmtDeleteSport.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir permanentemente o desporto. Query: " + deleteSportQuery + ". Erro: " + e.getMessage());
        }
        return false;
    }

    public List<Location> findAllActiveLocations() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM Locations WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setAddress(rs.getString("address"));
                location.setCity(rs.getString("city"));
                location.setCapacity(rs.getInt("capacity"));
                location.setYearBuilt(rs.getInt("year_built"));
                location.setEventId(rs.getInt("event_id"));
                location.setInative(rs.getBoolean("inative"));
                location.setType(rs.getString("type"));
                locations.add(location);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return locations;
    }

    public boolean hasActiveSchedule(int sportId) {
        String sql = "SELECT COUNT(*) FROM SportEvents WHERE sport_id = ? AND inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sportId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLocationReserved(int locationId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String sql = "SELECT COUNT(*) FROM SportEvents WHERE local = ? AND inative = 0 AND " +
                "((start_time <= ? AND end_time >= ?) OR (start_time <= ? AND end_time >= ?) OR " +
                "(start_time >= ? AND end_time <= ?))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(3, Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(4, Timestamp.valueOf(endDateTime));
            stmt.setTimestamp(5, Timestamp.valueOf(endDateTime));
            stmt.setTimestamp(6, Timestamp.valueOf(startDateTime));
            stmt.setTimestamp(7, Timestamp.valueOf(endDateTime));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Sport findSportById(int id) throws SQLException {
        String query = "SELECT * FROM Sports WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Sport(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("genre"),
                            rs.getString("description"),
                            rs.getInt("minParticipants"),
                            rs.getString("scoringMeasure"),
                            rs.getString("oneGame"),
                            rs.getString("olympicRecord_time"),
                            rs.getInt("olympicRecord_year"),
                            rs.getString("olympicRecord_holder"),
                            rs.getInt("winnerOlympic_year"),
                            rs.getString("winnerOlympic_time"),
                            rs.getString("winnerOlympic_holder"),
                            rs.getBoolean("inative")
                    );
                } else {
                    return null; // Retorna null se o desporto não for encontrado
                }
            }
        }
    }

    public List<Sport> findAllWithSchedule() {
        List<Sport> sports = new ArrayList<>();
        String query = """
        SELECT s.id, s.name, se.start_time, se.end_time
        FROM Sports s
        JOIN SportEvents se ON s.id = se.sport_id
        WHERE s.inative = 0 AND s.type = 'Individual'
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport();
                sport.setId(rs.getInt("id"));
                sport.setName(rs.getString("name"));
                sport.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                sport.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao carregar os desportos da base de dados: " + e.getMessage());
        }

        return sports;
    }

    public List<Sport> findAllWithScheduleColective() {
        List<Sport> sports = new ArrayList<>();
        String query = """
        SELECT s.id, s.name, se.start_time, se.end_time
        FROM Sports s
        JOIN SportEvents se ON s.id = se.sport_id
        WHERE s.inative = 0 AND s.type = 'Collective'
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport();
                sport.setId(rs.getInt("id"));
                sport.setName(rs.getString("name"));
                sport.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                sport.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sports;
    }

}