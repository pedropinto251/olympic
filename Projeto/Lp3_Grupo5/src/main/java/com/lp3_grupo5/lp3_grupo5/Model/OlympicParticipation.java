package com.lp3_grupo5.lp3_grupo5.Model;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;

import java.util.Date;

/**
 * Representa a participação de um atleta nas Olimpíadas.
 * <p>
 * Contém informações detalhadas sobre a participação de um atleta em um evento olímpico, incluindo o ano,
 * o resultado (ouro, prata, bronze, diploma), a flag de inatividade e o esporte ao qual a participação está relacionada.
 * </p>
 */
public class OlympicParticipation {

    /**
     * ID da participação olímpica (geralmente gerado pelo banco de dados).
     */
    private int id;

    /**
     * Ano da participação nas Olimpíadas.
     */
    private int year;

    /**
     * Resultado da participação ("Gold", "Silver", "Bronze", "Diploma").
     */
    private String result;

    /**
     * ID do atleta que participou.
     */
    private int athleteId;

    /**
     * ID do evento olímpico.
     */
    private int eventId;

    /**
     * Flag indicando se a participação está inativa (deletada).
     */
    private boolean inactive;

    /**
     * ID do esporte relacionado à participação.
     */
    private int sportId;

    /**
     * Número de medalhas de ouro conquistadas pelo atleta.
     */
    private int gold;

    /**
     * Número de medalhas de prata conquistadas pelo atleta.
     */
    private int silver;

    /**
     * Número de medalhas de bronze conquistadas pelo atleta.
     */
    private int bronze;

    /**
     * Número de diplomas recebidos (caso o atleta não tenha ganhado uma medalha).
     */
    private int diploma;

    private String athleteName;
    private String athleteCountry;
    private String athleteGenre;
    private int athleteHeight;
    private int athleteWeight;
    private Date athleteDateOfBirth;
    private String eventCountry;
    private int eventYear;
    private String eventMascot;

    private int sport_id;
    private Athlete athlete;

    private String sportName;

    public OlympicParticipation(int year, String result, int gold, int silver, int bronze, int diploma,
                                int athleteId, int eventId, boolean inactive, String athleteName,
                                String athleteCountry, String athleteGenre, int athleteHeight,
                                int athleteWeight, Date athleteDateOfBirth, String eventCountry,
                                int eventYear, String eventMascot, String sportName) {
        this.year = year;
        this.result = result;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.diploma = diploma;
        this.athleteId = athleteId;
        this.eventId = eventId;
        this.inactive = inactive;
        this.athleteName = athleteName;
        this.athleteCountry = athleteCountry;
        this.athleteGenre = athleteGenre;
        this.athleteHeight = athleteHeight;
        this.athleteWeight = athleteWeight;
        this.athleteDateOfBirth = athleteDateOfBirth;
        this.eventCountry = eventCountry;
        this.eventYear = eventYear;
        this.eventMascot = eventMascot;
        this.sportName = sportName;
    }

    public OlympicParticipation(int year, String result, int athleteId, int eventId, boolean inactive,
                                String athleteName, String eventCountry, String sportName) {
        this.year = year;
        this.result = result;
        this.athleteId = athleteId;
        this.eventId = eventId;
        this.inactive = inactive;
        this.athleteName = athleteName;
        this.eventCountry = eventCountry;
        this.sportName = sportName;
    }

    /**
     * Construtor alternativo sem o ID, sendo o ID gerado automaticamente.
     * Assume a flag inactive como false por padrão.
     *
     * @param year      Ano da participação.
     * @param result    Resultado da participação ("Gold", "Silver", "Bronze", "Diploma").
     * @param athleteId ID do atleta.
     * @param eventId   ID do evento olímpico.
     * @param sportId   ID do esporte.
     */
    public OlympicParticipation(int year, String result, int athleteId, int eventId, int sportId) {
        this.year = year;
        this.result = result;
        this.athleteId = athleteId;
        this.eventId = eventId;
        this.inactive = false; // Assume como ativo por padrão
        this.sportId = sportId;
        this.athlete = getAthleteById(athleteId); // Inicialize o campo athlete
    }

    /**
     * Construtor completo que inclui medalhas e diplomas.
     *
     * @param id        ID da participação olímpica.
     * @param year      Ano da participação.
     * @param result    Resultado da participação ("Gold", "Silver", "Bronze", "Diploma").
     * @param athleteId ID do atleta.
     * @param eventId   ID do evento olímpico.
     * @param inactive  Flag para indicar se a participação está inativa.
     * @param gold      Número de medalhas de ouro.
     * @param silver    Número de medalhas de prata.
     * @param bronze    Número de medalhas de bronze.
     * @param diploma   Número de diplomas recebidos.
     */
    public OlympicParticipation(int id, int year, String result, int athleteId, int eventId, boolean inactive, int gold, int silver, int bronze, int diploma) {
        this.id = id;
        this.year = year;
        this.result = result;
        this.athleteId = athleteId;
        this.eventId = eventId;
        this.inactive = inactive;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.diploma = diploma;
    }

    public OlympicParticipation(int year, String result, int gold, int silver, int bronze, int diploma, String athleteName, String eventCountry, String sportName) {
        this.year = year;
        this.result = result;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.diploma = diploma;
        this.athleteName = athleteName;
        this.eventCountry = eventCountry;
        this.sportName = sportName;
    }

    public OlympicParticipation(int year, String result, int gold, int silver, int bronze, int diploma, boolean inactive, String sportName) {
        this.year = year;
        this.result = result;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.diploma = diploma;
        this.inactive = inactive;
        this.sportName = sportName;
    }

    public OlympicParticipation(int athleteId, int year, String result, boolean inactive, int gold, int silver, int bronze, int diploma, String sportName) {
        this.athleteId = athleteId;
        this.year = year;
        this.result = result;
        this.inactive = inactive;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.diploma = diploma;
        this.sportName = sportName;
    }

    // ===================== MÉTODOS GETTERS E SETTERS ======================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
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

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getDiploma() {
        return diploma;
    }

    public void setDiploma(int diploma) {
        this.diploma = diploma;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getAthleteCountry() {
        return athleteCountry;
    }

    public void setAthleteCountry(String athleteCountry) {
        this.athleteCountry = athleteCountry;
    }

    public String getAthleteGenre() {
        return athleteGenre;
    }

    public void setAthleteGenre(String athleteGenre) {
        this.athleteGenre = athleteGenre;
    }

    public int getAthleteHeight() {
        return athleteHeight;
    }

    public void setAthleteHeight(int athleteHeight) {
        this.athleteHeight = athleteHeight;
    }

    public int getAthleteWeight() {
        return athleteWeight;
    }

    public void setAthleteWeight(int athleteWeight) {
        this.athleteWeight = athleteWeight;
    }

    public Date getAthleteDateOfBirth() {
        return athleteDateOfBirth;
    }

    public void setAthleteDateOfBirth(Date athleteDateOfBirth) {
        this.athleteDateOfBirth = athleteDateOfBirth;
    }

    public String getEventCountry() {
        return eventCountry;
    }

    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    public int getEventYear() {
        return eventYear;
    }

    public void setEventYear(int eventYear) {
        this.eventYear = eventYear;
    }

    public String getEventMascot() {
        return eventMascot;
    }

    public void setEventMascot(String eventMascot) {
        this.eventMascot = eventMascot;
    }

    public int getSport_id() {
        return sport_id;
    }

    public void setSport_id(int sport_id) {
        this.sport_id = sport_id;
    }

    // Método para obter o atleta pelo ID
    private Athlete getAthleteById(int athleteId) {
        // Implemente a lógica para obter o atleta pelo ID
        // Pode ser uma consulta ao banco de dados ou outra fonte de dados
        return new AthleteDAO().findById(athleteId);
    }

    // Getter para o campo athlete
    public Athlete getAthlete() {
        return athlete;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    /**
     * Representação em string da participação olímpica para fins de depuração.
     *
     * @return uma string representando a participação olímpica.
     */
    @Override
    public String toString() {
        return "OlympicParticipation{" +
                "id=" + id +
                ", year=" + year +
                ", result='" + result + '\'' +
                ", athleteId=" + athleteId +
                ", eventId=" + eventId +
                ", inactive=" + inactive +
                ", sportId=" + sportId +
                ", sportName='" + sportName + '\'' +
                '}';
    }
}