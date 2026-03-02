package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.TeamApplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface para definir os métodos de manipulação de inscrições de equipas.
 */
public interface ITeamApplicationDAO {

    /**
     * Insere uma nova inscrição na base de dados.
     *
     * @param application A inscrição a ser criada.
     * @param connection  A conexão com o banco de dados.
     * @return true se a inscrição foi criada com sucesso, false caso contrário.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    boolean createApplication(TeamApplication application, Connection connection) throws SQLException;

    /**
     * Recupera uma inscrição pelo ID.
     *
     * @param id         O ID da inscrição.
     * @param connection A conexão com o banco de dados.
     * @return A inscrição correspondente, ou null se não for encontrada.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    TeamApplication getApplicationById(int id, Connection connection) throws SQLException;

    /**
     * Recupera todas as inscrições na base de dados.
     *
     * @param connection A conexão com o banco de dados.
     * @return Uma lista de todas as inscrições.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    List<TeamApplication> getAllApplications(Connection connection) throws SQLException;

    /**
     * Atualiza o status de uma inscrição.
     *
     * @param id         O ID da inscrição a ser atualizada.
     * @param status     O novo status da inscrição (approved, pending, rejected).
     * @param connection A conexão com o banco de dados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    boolean updateApplicationStatus(int id, String status, Connection connection) throws SQLException;

    /**
     * Remove uma inscrição pelo ID.
     *
     * @param id         O ID da inscrição a ser removida.
     * @param connection A conexão com o banco de dados.
     * @return true se a remoção foi bem-sucedida, false caso contrário.
     * @throws SQLException Caso ocorra um erro ao acessar o banco de dados.
     */
    boolean deleteApplication(int id, Connection connection) throws SQLException;
}
