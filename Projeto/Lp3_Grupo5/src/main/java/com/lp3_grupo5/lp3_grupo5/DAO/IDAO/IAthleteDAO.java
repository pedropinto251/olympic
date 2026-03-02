package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface que define as operações CRUD (Criar, Ler, Atualizar, Eliminar) para os atletas.
 * Esta interface é implementada por classes DAO responsáveis por interagir com a base de dados,
 * permitindo a manipulação dos dados dos atletas.
 */
public interface IAthleteDAO {

    /**
     * Adiciona um novo atleta à base de dados.
     *
     * @param athlete O objeto {@link Athlete} contendo os dados do atleta a ser adicionado.
     * @return true se o atleta foi adicionado com sucesso, false caso contrário.
     * @throws SQLException Se ocorrer um erro durante a inserção no banco de dados.
     */
    boolean addAthlete(Athlete athlete) throws SQLException;

    /**
     * Recupera um atleta da base de dados pelo seu ID.
     *
     * @param id O ID do atleta a ser recuperado.
     * @return O objeto {@link Athlete} correspondente ao ID fornecido, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro durante a consulta no banco de dados.
     */
    Athlete getAthleteById(int id) throws SQLException;

    /**
     * Atualiza os dados de um atleta na base de dados.
     *
     * @param athlete O objeto {@link Athlete} contendo os dados a serem atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro durante a atualização no banco de dados.
     */

    Athlete updateAthlete(Athlete athlete) throws SQLException;

    /**
     * Carrega todos os atletas armazenados na base de dados.
     *
     * @return Uma lista observável contendo todos os atletas.
     */
    ObservableList<Athlete> loadAll();

    /**
     * Marca um atleta como excluído na base de dados, alterando o seu estado "inativo".
     *
     * @param athleteId O ID do atleta a ser excluído.
     * @return true se a exclusão lógica foi bem-sucedida, false caso contrário.
     */
    boolean deleteAthlete(int athleteId);

    boolean realDeleteAthlete(int athleteId);

    /**
     * Insere uma lista de atletas na base de dados.
     *
     * @param athletes A lista de {@link Athlete} a ser inserida na base de dados.
     */
    void insertAthletes(List<Athlete> athletes);

    /**
     * Insere as participações olímpicas de um atleta na base de dados.
     *
     * @param participations A lista de participações do atleta.
     * @param athleteId O ID do atleta.
     * @param conn A conexão com a base de dados.
     */
    void insertOlympicParticipations(List<Athlete.Participation> participations, int athleteId, Connection conn);

    void insertOrUpdateAthletes(List<Athlete> athletes);

    Athlete findByNameAndCountry(String name, String country);
}
