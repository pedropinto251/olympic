package com.lp3_grupo5.lp3_grupo5.Model;

/**
 * Classe que representa o registo de um atleta em eventos desportivos.
 * <p>
 * Contém informações sobre o registo de um atleta, como o estado da inscrição,
 * detalhes do desporto, evento, equipa, e outros dados relevantes.
 * </p>
 */
public class AthleteRegistration {

    /**
     * Identificador único do registo.
     */
    private int id;

    /**
     * Identificador do atleta associado ao registo.
     */
    private int athleteId;

    /**
     * Estado do registo (e.g., "pendente", "aprovado").
     */
    private String status;

    /**
     * Nome do desporto associado ao registo.
     */
    private String sportName;

    /**
     * Identificador do desporto.
     */
    private int sportId;

    /**
     * Ano do evento desportivo.
     */
    private int eventYear;

    /**
     * País onde o evento desportivo ocorre.
     */
    private String eventCountry;

    /**
     * Nome da equipa associada ao registo.
     */
    private String teamName;

    /**
     * Data de aplicação ou registo.
     */
    private String applicationDate;

    /**
     * País de origem do atleta.
     */
    private String athleteCountry;

    /**
     * Identificador da equipa associada ao registo.
     */
    private int teamId;

    private String name;

    private String genre;

    private boolean inative;

    // ======================== CONSTRUTORES ========================

    /**
     * Construtor para criar um registo com os campos básicos.
     *
     * @param id           o ID do registo.
     * @param athleteId    o ID do atleta.
     * @param status       o estado do registo.
     * @param sportName    o nome do desporto.
     * @param eventYear    o ano do evento desportivo.
     * @param eventCountry o país do evento.
     */
    public AthleteRegistration(int id, int athleteId, String status, String sportName, int eventYear, String eventCountry) {
        this.id = id;
        this.athleteId = athleteId;
        this.status = status;
        this.sportName = sportName;
        this.eventYear = eventYear;
        this.eventCountry = eventCountry;
    }

    /**
     * Construtor que inicializa todos os atributos, incluindo equipa e país do atleta.
     *
     * @param id              o ID do registo.
     * @param athleteId       o ID do atleta.
     * @param status          o estado do registo.
     * @param sportName       o nome do desporto.
     * @param eventYear       o ano do evento desportivo.
     * @param eventCountry    o país do evento.
     * @param teamName        o nome da equipa.
     * @param applicationDate a data de aplicação.
     * @param athleteCountry  o país de origem do atleta.
     */
    public AthleteRegistration(int id, int athleteId, String status, String sportName, int eventYear, String eventCountry, String teamName, String applicationDate, String athleteCountry) {
        this(id, athleteId, status, sportName, eventYear, eventCountry); // Chama o construtor original
        this.teamName = teamName;
        this.applicationDate = applicationDate;
        this.athleteCountry = athleteCountry;
    }

    /**
     * Construtor alternativo que inclui o ID do desporto.
     *
     * @param registrationId   o ID do registo.
     * @param athleteId        o ID do atleta.
     * @param status           o estado do registo.
     * @param sportId          o ID do desporto.
     * @param sportName        o nome do desporto.
     * @param eventYear        o ano do evento desportivo.
     * @param applicationDate  a data de aplicação.
     * @param athleteCountry   o país de origem do atleta.
     */
    public AthleteRegistration(int registrationId, int athleteId, String status,
                               int sportId, String sportName, int eventYear,
                               String applicationDate, String athleteCountry) {
        this.id = registrationId;
        this.athleteId = athleteId;
        this.status = status;
        this.sportId = sportId;
        this.sportName = sportName;
        this.eventYear = eventYear;
        this.applicationDate = applicationDate;
        this.athleteCountry = athleteCountry;
    }

    public AthleteRegistration(int id, int athleteId, String name, String athleteCountry, String genre, int sportId, String sportName, String status, boolean inative){
        this.id = id;
        this.athleteId = athleteId;
        this.name = name;
        this.athleteCountry = athleteCountry;
        this.genre = genre;
        this.sportId = sportId;
        this.sportName = sportName;
        this.status = status;
        this.inative = inative;
    }
    public AthleteRegistration(int registrationId, int athleteId, String status,
                               int sportId, String sportName, int eventYear,
                               String applicationDate, String athleteCountry, String genre) {
        this.id = registrationId;
        this.athleteId = athleteId;
        this.status = status;
        this.sportId = sportId;
        this.sportName = sportName;
        this.eventYear = eventYear;
        this.applicationDate = applicationDate;
        this.athleteCountry = athleteCountry;
        this.genre = genre;
    }
    // ======================== MÉTODOS GETTERS E SETTERS ========================

    /**
     * @return o ID do registo.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do registo.
     *
     * @param id o novo ID do registo.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return o ID do atleta associado ao registo.
     */
    public int getAthleteId() {
        return athleteId;
    }

    /**
     * Define o ID do atleta associado ao registo.
     *
     * @param athleteId o novo ID do atleta.
     */
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    /**
     * @return o estado do registo.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o estado do registo.
     *
     * @param status o novo estado do registo.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return o nome do desporto associado ao registo.
     */
    public String getSportName() {
        return sportName;
    }

    /**
     * Define o nome do desporto associado ao registo.
     *
     * @param sportName o novo nome do desporto.
     */
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    /**
     * @return o ID do desporto associado ao registo.
     */
    public int getSportId() {
        return sportId;
    }

    /**
     * Define o ID do desporto associado ao registo.
     *
     * @param sportId o novo ID do desporto.
     */
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    /**
     * @return o ano do evento desportivo.
     */
    public int getEventYear() {
        return eventYear;
    }

    /**
     * Define o ano do evento desportivo.
     *
     * @param eventYear o novo ano do evento.
     */
    public void setEventYear(int eventYear) {
        this.eventYear = eventYear;
    }

    /**
     * @return o país onde o evento desportivo ocorre.
     */
    public String getEventCountry() {
        return eventCountry;
    }

    /**
     * Define o país onde o evento desportivo ocorre.
     *
     * @param eventCountry o novo país do evento.
     */
    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    /**
     * @return o nome da equipa associada ao registo.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Define o nome da equipa associada ao registo.
     *
     * @param teamName o novo nome da equipa.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return a data de aplicação ou registo.
     */
    public String getApplicationDate() {
        return applicationDate;
    }

    /**
     * Define a data de aplicação ou registo.
     *
     * @param applicationDate a nova data de aplicação.
     */
    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * @return o ID da equipa associada ao registo.
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Define o ID da equipa associada ao registo.
     *
     * @param teamId o novo ID da equipa.
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * @return o país de origem do atleta.
     */
    public String getAthleteCountry() {
        return athleteCountry;
    }

    /**
     * Define o país de origem do atleta.
     *
     * @param athleteCountry o novo país do atleta.
     */
    public void setAthleteCountry(String athleteCountry) {
        this.athleteCountry = athleteCountry;
    }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name;}

    public String getGenre(){ return genre; }

    public void setGenre(String genre){ this.genre = genre;}

    public boolean isInative() {
        return inative;
    }

    /**
     * Define o estado de eliminação do desporto.
     *
     * @param inative true se o desporto deve ser marcado como eliminado, false caso contrário
     */
    public void setInative(boolean inative) {
        this.inative = inative;
    }


}
