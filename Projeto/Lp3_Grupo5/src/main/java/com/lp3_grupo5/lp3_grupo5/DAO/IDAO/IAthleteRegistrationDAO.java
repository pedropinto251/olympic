package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.AthleteRegistration;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IAthleteRegistrationDAO {

    /**
     * Obtém todas as inscrições de atletas, incluindo dados sobre o desporto e o evento.
     *
     * @return Uma lista observável de {@link AthleteRegistration} contendo todas as inscrições.
     * @throws SQLException Se ocorrer algum erro durante a consulta.
     */
    ObservableList<AthleteRegistration> getAllRegistrations() throws SQLException;


    /**
     * Aprova a inscrição de um atleta.
     *
     * @param applicationId O ID da inscrição do atleta a ser aprovado.
     * @return true se a inscrição foi aprovada com sucesso, false caso contrário.
     */
    boolean aproveAthlete(int applicationId, int teamId);

    /**
     * Rejeita a inscrição de um atleta.
     *
     * @param registrationId O ID da inscrição do atleta a ser rejeitado.
     * @return true se a inscrição foi rejeitada com sucesso, false caso contrário.
     */
    boolean rejectAthlete(int registrationId);
}
