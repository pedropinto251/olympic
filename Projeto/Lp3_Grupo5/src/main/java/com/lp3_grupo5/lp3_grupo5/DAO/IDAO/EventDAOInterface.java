package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Event;
import javafx.collections.ObservableList;

/**
 * Interface que define as operações CRUD (Criar, Ler, Atualizar, Eliminar) para os eventos.
 * Esta interface é implementada por classes DAO responsáveis por interagir com a base de dados,
 * permitindo a manipulação dos dados dos eventos.
 */
public interface EventDAOInterface {

    /**
     * Adiciona um novo evento à base de dados.
     *
     * @param event O objeto {@link Event} que contém os dados do evento a ser adicionado.
     * @return true se o evento foi adicionado com sucesso, false caso contrário.
     */
    boolean addEvent(Event event);

    /**
     * Recupera um evento da base de dados pelo seu ID.
     *
     * @param eventId O ID do evento a ser recuperado.
     * @return O objeto {@link Event} correspondente ao ID fornecido, ou null se não for encontrado.
     */
    Event getEventById(int eventId);

    /**
     * Carrega um evento da base de dados pelo seu ID, incluindo o estado de exclusão.
     *
     * @param eventId O ID do evento a ser carregado.
     * @return O objeto {@link Event} com os dados do evento, incluindo o estado de exclusão,
     * ou null se o evento não for encontrado.
     */
    Event loadEventById(int eventId);

    /**
     * Atualiza os dados de um evento na base de dados.
     *
     * @param event O objeto {@link Event} contendo os dados a serem atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean updateEvent(Event event);

    /**
     * Carrega todos os eventos armazenados na base de dados.
     * Obtém uma lista com os eventos, incluindo dados como o ano, país, mascote, localidade
     * e estado de exclusão (inativo).
     *
     * @return Uma lista observável contendo todos os eventos.
     */
    ObservableList<Event> loadAllEvents();

    /**
     * Marca um evento como inativo na base de dados, alterando o seu estado "inativo" para true.
     *
     * @param eventId O ID do evento a ser marcado como inativo.
     * @return true se a alteração foi bem-sucedida, false caso contrário.
     */
    boolean markEventAsinative(int eventId);

    /**
     * Verifica se existe um evento armazenado na base de dados com o ano fornecido.
     *
     * @param year O ano do evento a ser verificado.
     * @return true se existir um evento com o ano especificado, false caso contrário.
     */
    boolean eventExistsByYear(int year);
}
