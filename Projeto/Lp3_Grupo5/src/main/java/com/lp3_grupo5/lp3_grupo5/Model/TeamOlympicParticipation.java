package com.lp3_grupo5.lp3_grupo5.Model;

import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;

/**
 * Classe que representa a participação olímpica de uma equipa.
 * Esta classe contém informações sobre o ano da participação, o resultado alcançado,
 * a métrica de desempenho (como tempo ou distância), o estado da participação e as chaves estrangeiras
 * relacionadas à equipa e ao evento olímpico.
 */
public class TeamOlympicParticipation {
    private int year;          // Ano da participação
    private String result;     // Resultado da participação (ex.: Ouro, Prata, Bronze)
    private String resultTPD;  // Tempo ou outra métrica de desempenho
    private int teamId;        // Chave estrangeira para Equipa
    private int eventId;       // Chave estrangeira para Evento
    private boolean inactive;  // Estado da participação (ativa ou inativa)
    private String team_sport;  // Novo campo
    private String athleteName;
    private String event_country;
    private Team team;

    /**
     * Construtor para inicialização com os dados necessários.
     * Este construtor assume a participação como ativa por padrão.
     *
     * @param year O ano da participação olímpica.
     * @param result O resultado alcançado pela equipa (ex.: Ouro, Prata, Bronze).
     * @param resultTPD O desempenho ou métrica do resultado (ex.: tempo, distância, pontos).
     * @param teamId O ID da equipa que participou.
     * @param eventId O ID do evento olímpico em que a equipa participou.
     */
    public TeamOlympicParticipation(int year, String result, String resultTPD, int teamId, int eventId) {
        this.year = year;
        this.result = result;
        this.resultTPD = resultTPD;
        this.teamId = teamId;
        this.eventId = eventId;
        this.inactive = false; // Assume como ativo por padrão
        this.team = getTeamById(teamId); // Inicialize o campo team
    }

    /**
     * Construtor com todos os atributos, incluindo o ID da participação e o estado de inatividade.
     *
     * @param year O ano da participação olímpica.
     * @param result O resultado alcançado pela equipa.
     * @param resultTPD A métrica de desempenho (ex.: tempo ou distância).
     * @param teamId O ID da equipa que participou.
     * @param eventId O ID do evento olímpico.
     * @param inactive O estado de inatividade da participação (verdadeiro se inativa).
     */
    public TeamOlympicParticipation(int year, String result, String resultTPD, int teamId, int eventId, boolean inactive) {
        this.year = year;
        this.result = result;
        this.resultTPD = resultTPD;
        this.teamId = teamId;
        this.eventId = eventId;
        this.inactive = inactive;
        this.team = getTeamById(teamId); // Inicialize o campo team
    }

    public TeamOlympicParticipation(int participationYear, String result, int teamId, int eventId, boolean inative, String event_country, String team_sport, String athleteName) {
        this.year = participationYear;
        this.result = result;
        this.teamId = teamId;
        this.eventId = eventId;
        this.inactive = inative;
        this.event_country = event_country;
        this.team_sport = team_sport;
        this.athleteName = athleteName;
        this.team = getTeamById(teamId); // Inicialize o campo team
    }

    /**
     * Construtor padrão sem inicialização dos atributos.
     * Utilizado quando a inicialização dos valores ocorrer de outra forma.
     */
    public TeamOlympicParticipation() {
        // Construtor padrão sem inicialização
    }

    /**
     * Construtor alternativo com parâmetros básicos e o estado de inatividade.
     *
     * @param year O ano da participação olímpica.
     * @param result O resultado alcançado pela equipa.
     * @param teamId O ID da equipa.
     * @param eventId O ID do evento.
     * @param inactive O estado de inatividade da participação (verdadeiro se inativa).
     */
    public TeamOlympicParticipation(int year, String result, int teamId, int eventId, boolean inactive) {
        this.year = year;
        this.result = result;
        this.teamId = teamId;
        this.eventId = eventId;
        this.inactive = inactive;
        this.team = getTeamById(teamId); // Inicialize o campo team
    }

    // Getters e Setters

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultTPD() {
        return resultTPD;
    }

    public void setResultTPD(String resultTPD) {
        this.resultTPD = resultTPD;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public String getEvent_country() {
        return event_country;
    }

    public void setEvent_country(String event_country) {
        this.event_country = event_country;
    }

    public String getTeam_sport() {
        return team_sport;
    }

    public void setTeam_sport(String team_sport) {
        this.team_sport = team_sport;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    // Método para obter a equipe pelo ID
    private Team getTeamById(int teamId) {
        return new TeamDAO().findById(teamId);
    }

    // Getter para o campo team
    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "TeamOlympicParticipation{" +
                "year=" + year +
                ", result='" + result + '\'' +
                ", resultTPD='" + resultTPD + '\'' +
                ", teamId=" + teamId +
                ", eventId=" + eventId +
                ", inactive=" + inactive +
                '}';
    }
}