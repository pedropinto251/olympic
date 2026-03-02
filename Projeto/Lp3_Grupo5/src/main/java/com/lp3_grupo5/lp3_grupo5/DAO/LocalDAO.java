package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ILocalDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável por realizar operações CRUD (Create, Read, Update, Delete)
 * relacionadas aos locais na base de dados. Esta classe implementa a interface ILocalDAO
 * e utiliza a classe DBConnection para interagir com o banco de dados.
 */
public class LocalDAO implements ILocalDAO {

    /**
     * Insere um novo local na base de dados.
     *
     * @param location O objeto Location a ser inserido.
     * @return true se a inserção foi bem-sucedida, false caso contrário.
     */
    public boolean insert(Location location) {
        String query = "INSERT INTO Locations (address, city, capacity, year_built, inative, type, event_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, location.getAddress());
            stmt.setString(2, location.getCity());
            stmt.setInt(3, location.getCapacity());
            stmt.setInt(4, location.getYearBuilt());
            stmt.setBoolean(5, location.isInative());
            stmt.setString(6, location.getType());
            stmt.setInt(7, location.getEventId()); // Associa o local a um evento

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        location.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir local: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera um local da base de dados com base no seu ID.
     *
     * @param id O ID do local a ser recuperado.
     * @return O objeto {@link Location} correspondente ao ID fornecido, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    @Override
    public Location getLocationById(int id) throws SQLException {
        String query = "SELECT id, address, city, capacity, year_built, inative,type, event_id FROM Locations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Location(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getInt("capacity"),
                        rs.getInt("year_built"),
                        rs.getBoolean("inative"),
                        rs.getString("type"),
                        rs.getInt("event_id") // Recupera o ID do evento associado
                );
            }
        }
        return null;
    }

    /**
     * Atualiza os dados de um local na base de dados.
     *
     * @param location O objeto {@link Location} contendo as informações a serem atualizadas.
     * @return verdadeiro se a atualização foi bem-sucedida, falso caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    public boolean updateLocation(Location location) throws SQLException {
        // Consulta SQL para atualizar os campos, exceto o ID
        String query = "UPDATE Locations SET address = ?, city = ?, capacity = ?, year_built = ?, inative = ?, type = ?, event_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Definindo os parâmetros da consulta
            stmt.setString(1, location.getAddress());  // Atualiza o endereço
            stmt.setString(2, location.getCity());     // Atualiza a cidade
            stmt.setInt(3, location.getCapacity());    // Atualiza a capacidade
            stmt.setInt(4, location.getYearBuilt());   // Atualiza o ano de construção
            stmt.setBoolean(5, location.isInative());  // Atualiza o status de "inativo"
            stmt.setString(6, location.getType());     // Atualiza o tipo (corrigido para receber String, como na consulta SQL)
            stmt.setInt(7, location.getEventId());     // Atualiza o ID do evento associado
            stmt.setInt(8, location.getId());          // Usa o ID apenas como critério para identificar o local a ser atualizado

            // Executa a atualização e verifica se ela foi bem-sucedida
            return stmt.executeUpdate() > 0;
        }
    }



    /**
     * Exclui logicamente um local, marcando-o como excluído na base de dados.
     *
     * @param locationId O ID do local a ser marcado como excluído.
     * @return verdadeiro se a exclusão foi bem-sucedida, falso caso contrário.
     */
    @Override
    public boolean deleteLocation(int locationId) {
        String updateQuery = "UPDATE Locations SET inative = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, locationId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carrega todos os locais da base de dados que não foram marcados como excluídos.
     *
     * @return Uma lista observável ({@link ObservableList}) contendo todos os locais ativos.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    public ObservableList<Location> loadAll() throws SQLException {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        String query = "SELECT id, address, city, capacity, year_built, inative, event_id, type FROM Locations WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Location location = new Location(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getInt("capacity"),
                        rs.getInt("year_built"),
                        rs.getBoolean("inative"),
                        rs.getString("type"),
                        rs.getInt("event_id")// Associa o ID do evento

                );
                locations.add(location);
            }
        }
        return locations;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT id, year, country, mascot, inative FROM Events";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setYear(rs.getInt("year"));
                event.setCountry(rs.getString("country"));
                event.setMascot(rs.getString("mascot"));
                event.setInative(rs.getBoolean("inative"));

                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar eventos: " + e.getMessage());
        }
        return events;
    }
    /**
     * Este método busca todos os locais da base de dados e os retorna como uma lista observável.
     *
     * @return ObservableList<Location> Lista observável com os locais.
     */
    public ObservableList<Location> getLocationsFromDatabase() {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        String sql = "SELECT id, address, city, capacity, year_built, inative, type, event_id FROM Locations WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Location location = new Location(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getInt("capacity"),
                        rs.getInt("year_built"),
                        rs.getBoolean("inative"),
                        rs.getString("type"),
                        rs.getInt("event_id") // Preenche o eventId com o valor da consulta
                );
                locations.add(location);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public void realDeleteLocation(int locationId) {
        String query = "DELETE FROM Locations WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, locationId);
            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir o local: " + e.getMessage());
        }
    }
}
