package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface para operações de banco de dados relacionadas a locais.
 */
public interface ILocalDAO {

    /**
     * Insere um novo local na base de dados.
     *
     * @param location O objeto {@link Location} a ser inserido.
     * @return verdadeiro se a inserção foi bem-sucedida, falso caso contrário.
     */
    boolean insert(Location location);

    /**
     * Recupera um local da base de dados com base no seu ID.
     *
     * @param id O ID do local a ser recuperado.
     * @return O objeto {@link Location} correspondente ao ID fornecido, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    Location getLocationById(int id) throws SQLException;

    /**
     * Atualiza os dados de um local na base de dados.
     *
     * @param location O objeto {@link Location} contendo as informações a serem atualizadas.
     * @return verdadeiro se a atualização for bem-sucedida, falso caso contrário.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    boolean updateLocation(Location location) throws SQLException;

    /**
     * Exclui logicamente um local, marcando-o como excluído na base de dados.
     *
     * @param locationId O ID do local a ser marcado como excluído.
     * @return verdadeiro se a exclusão foi bem-sucedida, falso caso contrário.
     */
    boolean deleteLocation(int locationId);

    /**
     * Carrega todos os locais da base de dados que não foram marcados como excluídos.
     *
     * @return Uma lista observável ({@link ObservableList}) contendo todos os locais ativos.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    ObservableList<Location> loadAll() throws SQLException;

    /**
     * Recupera todos os locais da base de dados que não são inativos.
     *
     * @return Uma lista observável ({@link ObservableList}) contendo os locais ativos.
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    ObservableList<Location> getLocationsFromDatabase() throws SQLException;

    /**
     * Recupera todos os eventos relacionados aos locais.
     *
     * @return Uma lista de eventos.
     */
    List<Event> getAllEvents();
}
