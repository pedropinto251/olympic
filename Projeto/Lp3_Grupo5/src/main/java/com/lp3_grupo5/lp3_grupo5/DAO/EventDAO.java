package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.EventDAOInterface;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection.getConnection;

/**
 * Classe DAO responsável por realizar operações CRUD (Create, Read, Update, Delete)
 * relacionadas aos eventos na base de dados. Esta classe implementa a interface {@link EventDAOInterface}
 * e utiliza a classe {@link DBConnection} para interagir com o banco de dados.
 */
public class EventDAO implements EventDAOInterface {

    /**
     * Adiciona um novo evento à base de dados.
     * Realiza uma consulta SQL para inserir um evento na tabela "Events".
     *
     * @param event O objeto {@link Event} que contém os dados do evento a ser adicionado.
     * @return true se o evento foi adicionado com sucesso, false caso contrário.
     */
    @Override
    public boolean addEvent(Event event) {
        String query = "INSERT INTO Events (year, country, mascot, inative) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Verifica entradas inválidas antes de executar a query
            if (event.getYear() < 0 || event.getCountry() == null || event.getMascot() == null) {
                throw new IllegalArgumentException("Os campos do evento não podem conter valores nulos ou negativos.");
            }

            preparedStatement.setInt(1, event.getYear());
            preparedStatement.setString(2, event.getCountry());
            preparedStatement.setString(3, event.getMascot());
            preparedStatement.setBoolean(4, false); // Define o campo "inative" como falso

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        event.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID do evento.");
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar evento. Query: " + query + ". Erro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recupera um evento da base de dados pelo seu ID.
     *
     * @param eventId O ID do evento a ser recuperado.
     * @return O objeto {@link Event} correspondente ao ID fornecido, ou null se não for encontrado.
     */
    @Override
    public Event getEventById(int eventId) {
        String query = "SELECT * FROM Events WHERE id = ?";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Event(
                        resultSet.getInt("id"),
                        resultSet.getInt("year"),
                        resultSet.getString("country"),
                        resultSet.getString("mascot"),
                        resultSet.getBoolean("inative")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar evento por ID. Query: " + query + ". Erro: " + e.getMessage());
        }

        return null;
    }

    /**
     * Carrega todos os eventos da base de dados.
     *
     * @return Uma lista observável contendo todos os eventos.
     */
    @Override
    public ObservableList<Event> loadAllEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT id, year, country, mascot, inative FROM Events";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getInt("year"),
                        rs.getString("country"),
                        rs.getString("mascot"),
                        rs.getBoolean("inative")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar todos os eventos. Query: " + query + ". Erro: " + e.getMessage());
        }

        return events;
    }

    /**
     * Atualiza os dados de um evento na base de dados.
     *
     * @param event O objeto {@link Event} contendo as informações a serem atualizadas.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    @Override
    public boolean updateEvent(Event event) {
        String query = "UPDATE Events SET year = ?, country = ?, mascot = ? WHERE id = ?";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, event.getYear());
            stmt.setString(2, event.getCountry());
            stmt.setString(3, event.getMascot());
            stmt.setInt(4, event.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento. Query: " + query + ". Erro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Marca o evento como excluído no banco de dados.
     *
     * @param eventId O ID do evento a ser marcado como excluído.
     * @return true se a exclusão lógica for bem-sucedida, false caso contrário.
     */
    @Override
    public boolean markEventAsinative(int eventId) {
        String query = "UPDATE Events SET inative = 1 WHERE id = ?";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao marcar evento como excluído. Query: " + query + ". Erro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica se já existe um evento no banco de dados com o mesmo ano.
     *
     * @param year O ano do evento.
     * @return true se existir, false caso contrário.
     */
    @Override
    public boolean eventExistsByYear(int year) {
        String query = "SELECT COUNT(*) FROM Events WHERE year = ?";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência de evento. Query: " + query + ". Erro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Carrega um evento da base de dados pelo ID.
     * Este é um método adicional que pode ser útil em outros pontos do sistema.
     *
     * @param eventId O ID do evento.
     * @return O evento correspondente ao ID fornecido.
     */
    @Override
    public Event loadEventById(int eventId) {
        return getEventById(eventId);
    }

    /**
     * Exclui um evento do banco de dados.
     *
     * @param eventId O ID do evento a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean deleteEvent(int eventId) {
        String query = "DELETE FROM Events WHERE id = ?";

        try (Connection conn = getConnection(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir evento. Query: " + query + ". Erro: " + e.getMessage());
        }
        return false;
    }
    public boolean hasActiveSports() throws SQLException {
        String query = "SELECT COUNT(DISTINCT s.id) FROM Sports s " +
                "WHERE s.id IN (" +
                "    SELECT sport_id FROM AthleteRegistrations " +
                "    WHERE inative = 0 AND status = 'approved' " +
                "    UNION " +
                "    SELECT sport_id FROM TeamApplications " +
                "    WHERE inative = 0 AND status = 'approved'" +
                ") AND s.inative = 0";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println(resultSet.getInt(1));
                    return resultSet.getInt(1) > 0;  // Verifica se a contagem é maior que 0
                }
            }
        }
        return false;  // Retorna false se não houver desportos ativos
    }

    public int getActiveEventYear() {
        String sql = "SELECT year FROM events WHERE inative = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("year");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return java.time.Year.now().getValue(); // Retorna o ano atual caso não encontre evento ativo
    }

}

