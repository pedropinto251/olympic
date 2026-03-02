package com.lp3_grupo5.lp3_grupo5.Model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma inscrição numa equipa.
 * A inscrição contém detalhes sobre o atleta, a equipa, o status da inscrição
 * e a data da inscrição.
 */
public class TeamApplication {
    private int id; // ID da inscrição
    private int athleteId; // ID do atleta
    private int teamId; // ID da equipa
    private String status; // Status da inscrição (approved, pending, rejected)
    private LocalDateTime applicationDate; // Data da inscrição
    private int sport_id; // ID do desporto

    private String name;

    private String country;

    private String genre;

    private String sportName;

    private boolean inative;

    /**
     * Construtor padrão para a classe TeamApplication.
     * Inicializa os valores com valores padrão.
     */
    public TeamApplication() {
    }

    /**
     * Construtor completo para inicializar todos os atributos da inscrição.
     *
     * @param athleteId ID do atleta que se inscreve
     * @param teamId ID da equipa à qual o atleta está a se inscrever
     * @param status Status da inscrição (aprovada, pendente, rejeitada)
     * @param applicationDate Data e hora da inscrição
     */
    public TeamApplication(int athleteId, int teamId, String status, LocalDateTime applicationDate) {
        this.athleteId = athleteId;
        this.teamId = teamId;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public TeamApplication(int id, String name, String country,
                              String genre, int sport_id, String sportName, int teamId,
                              String status, boolean inative) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport_id = sport_id;
        this.sportName = sportName;
        this.teamId = teamId;
        this.status = status;
        this.inative = inative;
    }

    /**
     * Construtor completo para inicializar todos os atributos, incluindo o ID do desporto.
     *
     * @param athleteId ID do atleta
     * @param teamId ID da equipa
     * @param status Status da inscrição (aprovada, pendente, rejeitada)
     * @param applicationDate Data da inscrição
     * @param sport_id ID do desporto relacionado à inscrição
     */
    public TeamApplication(int athleteId, int teamId, String status, LocalDateTime applicationDate, int sport_id) {
        this.athleteId = athleteId;
        this.teamId = teamId;
        this.status = status;
        this.applicationDate = applicationDate;
        this.sport_id = sport_id;
    }

    // Getters e Setters

    /**
     * Obtém o identificador da inscrição.
     *
     * @return O ID da inscrição
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da inscrição.
     *
     * @param id O ID a definir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o ID do desporto relacionado à inscrição.
     *
     * @return O ID do desporto
     */
    public int getSport_id() {
        return sport_id;
    }

    /**
     * Define o ID do desporto relacionado à inscrição.
     *
     * @param sport_id O ID do desporto a definir
     */
    public void setSport_id(int sport_id) {
        this.sport_id = sport_id;
    }

    /**
     * Obtém o ID do atleta que fez a inscrição.
     *
     * @return O ID do atleta
     */
    public int getAthleteId() {
        return athleteId;
    }

    /**
     * Define o ID do atleta que fez a inscrição.
     *
     * @param athleteId O ID do atleta a definir
     */
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    /**
     * Obtém o ID da equipa à qual o atleta se inscreveu.
     *
     * @return O ID da equipa
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Define o ID da equipa à qual o atleta se inscreveu.
     *
     * @param teamId O ID da equipa a definir
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * Obtém o status da inscrição (aprovada, pendente, rejeitada).
     *
     * @return O status da inscrição
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status da inscrição.
     *
     * @param status O status a definir (approved, pending, rejected)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtém a data e hora da inscrição.
     *
     * @return A data e hora da inscrição
     */
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    /**
     * Define a data e hora da inscrição.
     *
     * @param applicationDate A data e hora da inscrição a definir
     */
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name;}

    public String getGenre(){ return genre; }

    public void setGenre(String genre){ this.genre = genre;}

    public String getSportName(){ return sportName; }

    public void setSportName(String sportName){ this.sportName = sportName;}

    public String getCountry(){ return country; }

    public void setCountry(String country){ this.country = country;}



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
