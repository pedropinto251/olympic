package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.*;

import java.sql.SQLException;
import java.util.List;

public interface ISportDAO {
    /**
     * Insere uma nova modalidade esportiva no banco de dados.
     *
     * @param sport O objeto Sport a ser inserido no banco de dados.
     * @return `true` se a inserção foi bem-sucedida, `false` caso contrário.
     * @throws SQLException Se ocorrer algum erro durante a inserção.
     */
    boolean insertSport(Sport sport, String rule) throws SQLException;

    /**
     * Recupera todas as modalidades esportivas do banco de dados.
     *
     * @return Uma lista de objetos Sport.
     * @throws SQLException Se ocorrer algum erro durante a consulta.
     */
    List<Sport> findAll() throws SQLException;

    List<Sport> findAllColletive();

    List<Sport> findAllColletiveInative();

    List<Sport> findAllForGenerator();

    /**
     * Marca uma modalidade esportiva como deletada no banco de dados.
     *
     * @param id O ID da modalidade a ser excluída.
     * @return `true` se a atualização foi bem-sucedida, `false` caso contrário.
     * @throws SQLException Se ocorrer algum erro durante a atualização.
     */
    boolean deleteSport(int id) throws SQLException;

    boolean activateSport(int id) throws SQLException;

    List<Event> getActiveEvents() throws SQLException; // Busca eventos ativos

    List<Athlete> getAthletesBySport(int sportId) throws SQLException; // Atletas inscritos para um esporte

    List<Team> getTeamsBySport(int sportId) throws SQLException; // Equipes inscritas para um esporte

    void saveIndividualParticipation(OlympicParticipation participation) throws SQLException; // Salvar resultado individual

    void saveIndividualParticipation(OlympicParticipation participation, String medal) throws SQLException;

    void saveTeamParticipation(TeamOlympicParticipation participation) throws SQLException; // Salvar resultado de equipes

    void saveTeamParticipation(TeamOlympicParticipation participation, Event event, String medal) throws SQLException;

    boolean updateSport(Sport sport, String rule) throws SQLException;

    void insertOrUpdateSports(List<Sport> sports);

    boolean sportExists(String name, String genre);

    void updateSport(Sport sport);

    void insertSport(Sport sport);
}
