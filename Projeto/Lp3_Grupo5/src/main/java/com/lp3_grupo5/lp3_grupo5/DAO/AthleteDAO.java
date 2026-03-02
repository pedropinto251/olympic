package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IAthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Model.OlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Model.TeamOlympicParticipation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável por realizar operações CRUD (Criar, Ler, Atualizar, Eliminar)
 * relacionadas aos atletas na base de dados. Esta classe implementa a interface IAthleteDAO
 * e utiliza a classe DBConnection para interagir com o banco de dados.
 */
public class AthleteDAO implements IAthleteDAO {

    /**
     * Recupera um atleta a partir do seu ID.
     * Realiza uma consulta SQL para obter os dados do atleta com o ID especificado.
     *
     * @param id O ID do atleta a ser recuperado.
     * @return O objeto {@link Athlete} correspondente ao ID fornecido, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public Athlete getAthleteById(int id) throws SQLException {
        String query = "SELECT id, name, country, genre, height, weight, dateOfBirth, inative, photo FROM Athletes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Athlete(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("genre"),
                        rs.getInt("height"),
                        rs.getInt("weight"),
                        rs.getString("dateOfBirth"),
                        rs.getBoolean("inative"),  // Carrega o valor do campo `inative`
                        rs.getString("photo")
                );
            }
        }
        return null;
    }

    /**
     * Atualiza os dados de um atleta na base de dados.
     * Realiza uma consulta SQL para atualizar os dados do atleta com base no seu ID.
     *
     * @param athlete O objeto {@link Athlete} contendo as informações a serem atualizadas.
     * @return verdadeiro se a atualização for bem-sucedida, falso caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */


    @Override
    public Athlete updateAthlete(Athlete athlete) throws SQLException {
        String query = "UPDATE Athletes SET name = ?, country = ?, genre = ?, height = ?, weight = ?, dateOfBirth = ?, inative = ?, photo = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, athlete.getName());
            stmt.setString(2, athlete.getCountry());
            stmt.setString(3, athlete.getGenre());
            stmt.setInt(4, athlete.getHeight());
            stmt.setInt(5, athlete.getWeight());
            stmt.setString(6, athlete.getDateOfBirth());
            stmt.setBoolean(7, athlete.isInative());
            stmt.setString(8, athlete.getPhoto());
            stmt.setInt(9, athlete.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Retorna o atleta atualizado
                return getAthleteById(athlete.getId());
            } else {
                return null; // Ou lança uma exceção
            }
        }
    }



    /**
     * Carrega todos os atletas da base de dados que não foram marcados como excluídos.
     * Realiza uma consulta SQL para obter todos os atletas com a coluna `inative` igual a 0 (não excluídos).
     *
     * @return Uma lista observável ({@link ObservableList}) contendo todos os atletas ativos.
     */
    @Override
    public ObservableList<Athlete> loadAll() {
        ObservableList<Athlete> athletes = FXCollections.observableArrayList();
        String query = "SELECT id, name, country, genre, height, weight, dateOfBirth, inative, photo FROM Athletes WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Athlete athlete = new Athlete(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("genre"),
                        rs.getInt("height"),
                        rs.getInt("weight"),
                        rs.getString("dateOfBirth"),
                        rs.getBoolean("inative"),
                        rs.getString("photo")
                );
                athletes.add(athlete);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    /**
     * Exclui logicamente um atleta, marcando-o como excluído na base de dados.
     * Realiza uma atualização na tabela `Athletes` alterando o campo `inative` para 1
     * para o atleta com o ID especificado.
     *
     * @param athleteId O ID do atleta a ser marcado como excluído.
     * @return verdadeiro se a exclusão foi bem-sucedida, falso caso contrário.
     */
    @Override
    public boolean deleteAthlete(int athleteId) {
        String updateQuery = "UPDATE Athletes SET inative = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, athleteId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna verdadeiro se a exclusão foi bem-sucedida

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean realDeleteAthlete(int athleteId) {
        String deleteUserQuery = "DELETE FROM Users WHERE athlete_id = ?";
        String deleteAthleteQuery = "DELETE FROM Athletes WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery);
             PreparedStatement deleteAthleteStmt = conn.prepareStatement(deleteAthleteQuery)) {

            // Apagar da tabela Users
            deleteUserStmt.setInt(1, athleteId);
            int userRowsAffected = deleteUserStmt.executeUpdate();

            // Apagar da tabela Athletes
            deleteAthleteStmt.setInt(1, athleteId);
            int athleteRowsAffected = deleteAthleteStmt.executeUpdate();

            // Retorna verdadeiro se ambas as exclusões foram bem-sucedidas
            return userRowsAffected > 0 && athleteRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void insertAthletes(List<Athlete> athletes) {
        String insertAthleteQuery = "INSERT INTO Athletes (name, country, genre, height, weight, dateOfBirth, inative, photo) VALUES (?, ?, ?, ?, ?, ?, 0, 'tst.png')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement athleteStmt = conn.prepareStatement(insertAthleteQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            for (Athlete athlete : athletes) {
                athleteStmt.setString(1, athlete.getName());
                athleteStmt.setString(2, athlete.getCountry());
                athleteStmt.setString(3, athlete.getGenre());
                athleteStmt.setInt(4, athlete.getHeight());
                athleteStmt.setInt(5, athlete.getWeight());
                athleteStmt.setDate(6, java.sql.Date.valueOf(athlete.getDateOfBirth()));

                // Executa a atualização da consulta
                int rowsAffected = athleteStmt.executeUpdate();
                if (rowsAffected > 0) {
                    int athleteId = getGeneratedAthleteId(athleteStmt);
                    insertOlympicParticipations(athlete.getOlympicParticipations(), athleteId, conn);
                } else {
                    throw new SQLException("Nenhum atleta foi inserido.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getGeneratedAthleteId(PreparedStatement athleteStmt) throws SQLException {
        try (var generatedKeys = athleteStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao obter o ID do atleta inserido.");
            }
        }
    }

    @Override
    public void insertOlympicParticipations(List<Athlete.Participation> participations, int athleteId, Connection conn) {
        String insertParticipationQuery = "INSERT INTO OlympicParticipations (year, gold, silver, bronze, athlete_id, inative) VALUES (?, ?, ?, ?, ?, 0)";
        try (PreparedStatement participationStmt = conn.prepareStatement(insertParticipationQuery)) {
            for (Athlete.Participation participation : participations) {
                participationStmt.setInt(1, participation.getYear());
                participationStmt.setInt(2, participation.getGold());
                participationStmt.setInt(3, participation.getSilver());
                participationStmt.setInt(4, participation.getBronze());
                participationStmt.setInt(5, athleteId);

                participationStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrUpdateAthletes(List<Athlete> athletes) {
        String insertAthleteQuery = "INSERT INTO Athletes (name, country, genre, height, weight, dateOfBirth, inative, photo) VALUES (?, ?, ?, ?, ?, ?, 0, 'tst.png')";
        String updateAthleteQuery = "UPDATE Athletes SET genre = ?, height = ?, weight = ?, dateOfBirth = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Falha ao obter conexão com o banco de dados.");
            }
            insertStmt = conn.prepareStatement(insertAthleteQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            updateStmt = conn.prepareStatement(updateAthleteQuery);

            for (Athlete athlete : athletes) {
                Athlete existingAthlete = findByNameAndCountry(conn, athlete.getName(), athlete.getCountry());
                if (existingAthlete != null) {
                    // Atualizar atleta existente
                    updateStmt.setString(1, athlete.getGenre());
                    updateStmt.setInt(2, athlete.getHeight());
                    updateStmt.setInt(3, athlete.getWeight());
                    updateStmt.setDate(4, java.sql.Date.valueOf(athlete.getDateOfBirth()));
                    updateStmt.setInt(5, existingAthlete.getId());
                    updateStmt.executeUpdate();
                    insertOlympicParticipations(athlete.getOlympicParticipations(), existingAthlete.getId(), conn);
                } else {
                    // Inserir novo atleta
                    insertStmt.setString(1, athlete.getName());
                    insertStmt.setString(2, athlete.getCountry());
                    insertStmt.setString(3, athlete.getGenre());
                    insertStmt.setInt(4, athlete.getHeight());
                    insertStmt.setInt(5, athlete.getWeight());
                    insertStmt.setDate(6, java.sql.Date.valueOf(athlete.getDateOfBirth()));
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        int athleteId = getGeneratedAthleteId(insertStmt);
                        insertOlympicParticipations(athlete.getOlympicParticipations(), athleteId, conn);
                    } else {
                        throw new SQLException("Nenhum atleta foi inserido.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (insertStmt != null) {
                try {
                    insertStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (updateStmt != null) {
                try {
                    updateStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Athlete findByNameAndCountry(String name, String country) {
        return null;
    }

    public Athlete findByNameAndCountry(Connection conn, String name, String country) throws SQLException {
        String query = "SELECT * FROM Athletes WHERE name = ? AND country = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, country);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Athlete athlete = new Athlete();
                    athlete.setId(rs.getInt("id"));
                    athlete.setName(rs.getString("name"));
                    athlete.setCountry(rs.getString("country"));
                    athlete.setGenre(rs.getString("genre"));
                    athlete.setHeight(rs.getInt("height"));
                    athlete.setWeight(rs.getInt("weight"));
                    athlete.setDateOfBirth(rs.getString("dateOfBirth"));
                    // Adicionar outras propriedades conforme necessário
                    return athlete;
                }
            }
        }
        return null;
    }
    // Método para buscar as participações do atleta logado
    public List<OlympicParticipation> getParticipationsByAthleteId(int athleteId) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT * FROM OlympicParticipations WHERE athlete_id = ? AND inative = 0"; // Apenas participações ativas

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("id"),                // ID da participação
                        rs.getInt("year"),              // Ano da participação
                        rs.getString("result"),         // Resultado (Gold, Silver, Bronze, etc.)
                        rs.getInt("athlete_id"),        // ID do atleta
                        rs.getInt("event_id"),          // ID do evento
                        rs.getBoolean("inative"),       // Flag de inatividade
                        rs.getInt("gold"),              // Medalha de ouro
                        rs.getInt("silver"),            // Medalha de prata
                        rs.getInt("bronze"),            // Medalha de bronze
                        rs.getInt("diploma")            // Diploma
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public static List<TeamOlympicParticipation> getTeamParticipationsByAthleteId(int athleteId) {
        List<TeamOlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT tp.year, tp.result, tp.team_id, tp.event_id, tp.inative " +
                "FROM TeamOlympicParticipations tp " +
                "INNER JOIN TeamMemberships tm ON tm.team_id = tp.team_id " +
                "WHERE tm.athlete_id = ? AND tm.inative = 0 AND tp.inative = 0";  // Considerando que 'inative' = 0 é ativo

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);  // Definir o ID do atleta

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TeamOlympicParticipation participation = new TeamOlympicParticipation(
                        rs.getInt("year"),
                        rs.getString("result"),
                        rs.getInt("team_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<String> getSportsByAthleteId(int athleteId) {
        List<String> sports = new ArrayList<>();
        String query = "SELECT DISTINCT s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.athlete_id = ? AND op.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sports.add(rs.getString("sport_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }

    public List<String> getYearsByAthleteId(int athleteId) {
        List<String> years = new ArrayList<>();
        String query = "SELECT DISTINCT op.year " +
                "FROM OlympicParticipations op " +
                "WHERE op.athlete_id = ? AND op.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                years.add(String.valueOf(rs.getInt("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }

    public List<OlympicParticipation> getOlympicParticipationsByAthleteAndSport(int athleteId, String sportName) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.inative, s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.athlete_id = ? AND s.name = ? AND op.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            stmt.setString(2, sportName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getBoolean("inative"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<OlympicParticipation> getOlympicParticipationsByAthleteYearAndSport(int athleteId, int year, String sportName) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.inative, s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.athlete_id = ? AND op.year = ? AND s.name = ? AND op.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            stmt.setInt(2, year);
            stmt.setString(3, sportName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getBoolean("inative"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }




    public List<TeamOlympicParticipation> getAllTeamParticipations() {
        List<TeamOlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT tp.year AS participation_year, tp.result, tp.team_id, tp.event_id, tp.inative, " +
                "e.country AS event_country, t.sport AS team_sport, a.name AS athlete_name " +
                "FROM TeamOlympicParticipations tp " +
                "INNER JOIN TeamMemberships tm ON tm.team_id = tp.team_id " +
                "INNER JOIN Events e ON e.id = tp.event_id " +
                "INNER JOIN Teams t ON t.id = tp.team_id " +
                "INNER JOIN Athletes a ON a.id = tm.athlete_id " +
                "WHERE tm.inative = 0 AND tp.inative = 0 AND e.inative = 0 AND a.inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TeamOlympicParticipation participation = new TeamOlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("team_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("event_country"),  // Novo campo para o país do evento
                        rs.getString("team_sport"),     // Novo campo para o desporto da equipe
                        rs.getString("athlete_name")    // Novo campo para o nome do atleta
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<TeamOlympicParticipation> getParticipationsBySport(String sport) {
        List<TeamOlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT tp.year AS participation_year, tp.result, tp.team_id, tp.event_id, tp.inative, " +
                "e.country AS event_country, t.sport AS team_sport, a.name AS athlete_name " +
                "FROM TeamOlympicParticipations tp " +
                "INNER JOIN TeamMemberships tm ON tm.team_id = tp.team_id " +
                "INNER JOIN Events e ON e.id = tp.event_id " +
                "INNER JOIN Teams t ON t.id = tp.team_id " +
                "INNER JOIN Athletes a ON a.id = tm.athlete_id " +
                "WHERE tm.inative = 0 AND tp.inative = 0 AND e.inative = 0 AND a.inative = 0 " +
                "AND t.sport = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sport);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TeamOlympicParticipation participation = new TeamOlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("team_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("event_country"),  // Novo campo para o país do evento
                        rs.getString("team_sport"),     // Novo campo para o desporto da equipe
                        rs.getString("athlete_name")    // Novo campo para o nome do atleta
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    // Método para atualizar o estado da participação (exemplo de desativar)
    public void updateParticipationStatus(OlympicParticipation participation) {
        String query = "UPDATE OlympicParticipations SET inative = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, participation.isInactive());
            stmt.setInt(2, participation.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Recupera as participações olímpicas de um atleta a partir do seu ID.
     * Realiza uma consulta SQL para obter todas as participações olímpicas relacionadas ao atleta.
     *
     * @param athleteId O ID do atleta para o qual as participações serão recuperadas.
     * @return Uma lista de participações olímpicas (List<Participation>), ou uma lista vazia se não houver participações.
     */
    public List<Athlete.Participation> getOlympicParticipations(int athleteId) {
        List<Athlete.Participation> participations = new ArrayList<>();
        String query = "SELECT year, gold, silver, bronze FROM OlympicParticipations WHERE athlete_id = ? AND inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);  // Define o ID do atleta

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Athlete.Participation participation = new Athlete.Participation(
                            rs.getInt("year"),
                            rs.getInt("gold"),
                            rs.getInt("silver"),
                            rs.getInt("bronze")
                    );
                    participations.add(participation);  // Add participation to the list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return participations;
    }


    public List<TeamOlympicParticipation> getParticipationsByAthleteName(String athleteName) {
        List<TeamOlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT tp.year AS participation_year, tp.result, tp.team_id, tp.event_id, tp.inative, " +
                "e.country AS event_country, t.sport AS team_sport, a.name AS athlete_name " +
                "FROM TeamOlympicParticipations tp " +
                "INNER JOIN TeamMemberships tm ON tm.team_id = tp.team_id " +
                "INNER JOIN Events e ON e.id = tp.event_id " +
                "INNER JOIN Teams t ON t.id = tp.team_id " +
                "INNER JOIN Athletes a ON a.id = tm.athlete_id " +
                "WHERE tm.inative = 0 AND tp.inative = 0 AND e.inative = 0 AND a.inative = 0 " +
                "AND a.name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + athleteName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TeamOlympicParticipation participation = new TeamOlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("team_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("event_country"),  // Novo campo para o país do evento
                        rs.getString("team_sport"),     // Novo campo para o desporto da equipe
                        rs.getString("athlete_name")    // Novo campo para o nome do atleta
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<OlympicParticipation> getAllOlympicParticipations() {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year AS participation_year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.athlete_id, op.event_id, op.inative, " +
                "a.name AS athlete_name, a.country AS athlete_country, a.genre AS athlete_genre, " +
                "a.height AS athlete_height, a.weight AS athlete_weight, a.dateOfBirth AS athlete_dateOfBirth, " +
                "e.country AS event_country, e.year AS event_year, e.mascot AS event_mascot, " +
                "s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Athletes a ON op.athlete_id = a.id " +
                "INNER JOIN Events e ON op.event_id = e.id " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.inative = 0 AND a.inative = 0 ";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getInt("athlete_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("athlete_height"),
                        rs.getInt("athlete_weight"),
                        rs.getDate("athlete_dateOfBirth"),
                        rs.getString("event_country"),
                        rs.getInt("event_year"),
                        rs.getString("event_mascot"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }


    public List<OlympicParticipation> getOlympicParticipationsBySport(String sportName) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year AS participation_year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.athlete_id, op.event_id, op.inative, " +
                "a.name AS athlete_name, a.country AS athlete_country, a.genre AS athlete_genre, " +
                "a.height AS athlete_height, a.weight AS athlete_weight, a.dateOfBirth AS athlete_dateOfBirth, " +
                "e.country AS event_country, e.year AS event_year, e.mascot AS event_mascot, " +
                "s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Athletes a ON op.athlete_id = a.id " +
                "INNER JOIN Events e ON op.event_id = e.id " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.inative = 0 AND a.inative = 0 AND e.inative = 0 AND s.name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sportName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getInt("athlete_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("athlete_height"),
                        rs.getInt("athlete_weight"),
                        rs.getDate("athlete_dateOfBirth"),
                        rs.getString("event_country"),
                        rs.getInt("event_year"),
                        rs.getString("event_mascot"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<OlympicParticipation> getIndividualParticipationsByAthleteName(String athleteName) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "a.name AS athlete_name, e.country AS event_country, s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Athletes a ON a.id = op.athlete_id " +
                "INNER JOIN Events e ON e.id = op.event_id " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.inative = 0 AND a.inative = 0 AND e.inative = 0 " +
                (athleteName != null && !athleteName.isEmpty() ? " AND a.name LIKE ?" : "");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (athleteName != null && !athleteName.isEmpty()) {
                stmt.setString(1, "%" + athleteName + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getString("athlete_name"),
                        rs.getString("event_country"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }


    /* public List<OlympicParticipation> getOlympicParticipationsByYear(int year) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year AS participation_year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.athlete_id, op.event_id, op.inative, " +
                "a.name AS athlete_name, a.country AS athlete_country, a.genre AS athlete_genre, " +
                "a.height AS athlete_height, a.weight AS athlete_weight, a.dateOfBirth AS athlete_dateOfBirth, " +
                "e.country AS event_country, e.year AS event_year, e.mascot AS event_mascot, " +
                "s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Athletes a ON op.athlete_id = a.id " +
                "INNER JOIN Events e ON op.event_id = e.id " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.inative = 0 AND a.inative = 0 AND op.year = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getInt("athlete_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("athlete_height"),
                        rs.getInt("athlete_weight"),
                        rs.getDate("athlete_dateOfBirth"),
                        rs.getString("event_country"),
                        rs.getInt("event_year"),
                        rs.getString("event_mascot"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    } */

    public List<OlympicParticipation> getOlympicParticipationsByYearAndSport(int year, String sportName) {
        List<OlympicParticipation> participations = new ArrayList<>();
        String query = "SELECT op.year AS participation_year, op.result, op.gold, op.silver, op.bronze, op.diploma, " +
                "op.athlete_id, op.event_id, op.inative, " +
                "a.name AS athlete_name, a.country AS athlete_country, a.genre AS athlete_genre, " +
                "a.height AS athlete_height, a.weight AS athlete_weight, a.dateOfBirth AS athlete_dateOfBirth, " +
                "e.country AS event_country, e.year AS event_year, e.mascot AS event_mascot, " +
                "s.name AS sport_name " +
                "FROM OlympicParticipations op " +
                "INNER JOIN Athletes a ON op.athlete_id = a.id " +
                "INNER JOIN Events e ON op.event_id = e.id " +
                "INNER JOIN Sports s ON op.sport_id = s.id " +
                "WHERE op.inative = 0 AND a.inative = 0 AND op.year = ? AND s.name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            stmt.setString(2, sportName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OlympicParticipation participation = new OlympicParticipation(
                        rs.getInt("participation_year"),
                        rs.getString("result"),
                        rs.getInt("gold"),
                        rs.getInt("silver"),
                        rs.getInt("bronze"),
                        rs.getInt("diploma"),
                        rs.getInt("athlete_id"),
                        rs.getInt("event_id"),
                        rs.getBoolean("inative"),
                        rs.getString("athlete_name"),
                        rs.getString("athlete_country"),
                        rs.getString("athlete_genre"),
                        rs.getInt("athlete_height"),
                        rs.getInt("athlete_weight"),
                        rs.getDate("athlete_dateOfBirth"),
                        rs.getString("event_country"),
                        rs.getInt("event_year"),
                        rs.getString("event_mascot"),
                        rs.getString("sport_name")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }




    public Athlete findById(int id) {
        String sql = "SELECT * FROM Athletes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Athlete(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("genre")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se o atleta não for encontrado
    }
    /**
     * Adiciona um novo atleta à base de dados.
     * Realiza uma consulta SQL para inserir um atleta na tabela "Athletes".
     *
     * @param athlete O objeto {@link Athlete} que contém os dados do atleta a ser adicionado.
     * @return verdadeiro se o atleta foi adicionado com sucesso, falso caso contrário.
     */
    public boolean addAthlete(Athlete athlete) throws SQLException {
        String query = "INSERT INTO Athletes (name, country, genre, height, weight, dateOfBirth, inative, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, athlete.getName());
            stmt.setString(2, athlete.getCountry());
            stmt.setString(3, athlete.getGenre());
            stmt.setInt(4, athlete.getHeight());
            stmt.setInt(5, athlete.getWeight());
            stmt.setString(6, athlete.getDateOfBirth());
            stmt.setBoolean(7, athlete.isInative());
            stmt.setString(8, athlete.getPhoto());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    athlete.setId(generatedKeys.getInt(1));
                }
                return true;
            } else {
                return false;
            }
        }
    }
}


