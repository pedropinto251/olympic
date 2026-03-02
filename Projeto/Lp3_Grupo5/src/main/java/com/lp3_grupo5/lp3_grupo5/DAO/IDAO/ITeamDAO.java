package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Team;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface que define as operações relacionadas aos times no banco de dados.
 * Esta interface é implementada pela classe que fará a persistência dos dados dos times.
 */
public interface ITeamDAO {

    void insertOrUpdateTeams(List<Team> teams);

    boolean teamExists(String name, String genre);

    boolean insertTeam(Team team) throws SQLException;

    /**
     * Recupera todos os times armazenados na base de dados.
     *
     * @return Uma lista de {@link Team} contendo todos os times.
     * @throws SQLException Caso ocorra algum erro na comunicação com a base de dados.
     */
    List<Team> findAll() throws SQLException;

    /**
     * Exclui um time da base de dados.
     *
     * @param id O ID do time a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     * @throws SQLException Caso ocorra algum erro na comunicação com a base de dados.
     */
    boolean deleteTeam(int id) throws SQLException;

    void updateTeamXML(Team team);

    void insertTeamXML(Team team);
}
