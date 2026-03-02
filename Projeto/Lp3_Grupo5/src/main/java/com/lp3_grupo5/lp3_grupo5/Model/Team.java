package com.lp3_grupo5.lp3_grupo5.Model;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Classe que representa uma equipa desportiva.
 * Contém informações sobre a equipa, como nome, país, género, desporto, ano de fundação,
 * e as participações olímpicas, entre outros.
 */
public class Team {
    private int id; // Identificador da equipa
    private String name; // Nome da equipa
    private String country; // País da equipa
    private String genre; // Género da equipa (ex.: Masculino, Feminino)
    private String sport; // Desporto da equipa
    private int foundationYear; // Ano de fundação da equipa
    private List<TeamOlympicParticipation> olympicParticipations; // Lista de participações olímpicas
    private boolean inative; // Estado de eliminação da equipa
    private int sportsId; // ID do desporto
    private String resultTPD; // Resultado gerado aleatoriamente (Tempo, Distância ou Pontos)
    private String result; // Resultado final (Medalha ou Diploma)

    private static final Random random = new Random(); // Instância do gerador de números aleatórios

    /**
     * Construtor padrão para a classe Team.
     */
    public Team() {}

    /**
     * Construtor para inicializar todos os atributos de uma equipa.
     *
     * @param id Identificador da equipa
     * @param name Nome da equipa
     * @param country País da equipa
     * @param genre Género da equipa (ex.: Masculino, Feminino)
     * @param sportsId ID do desporto
     * @param foundationYear Ano de fundação da equipa
     */
    public Team(int id, String name, String country, String genre, int sportsId, int foundationYear) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sportsId = sportsId;
        this.foundationYear = foundationYear;
    }

    /**
     * Construtor para inicializar uma equipa com todos os atributos, incluindo o estado de eliminação.
     *
     * @param id Identificador da equipa
     * @param name Nome da equipa
     * @param country País da equipa
     * @param genre Género da equipa
     * @param sport Desporto da equipa
     * @param sportsId ID do desporto
     * @param foundationYear Ano de fundação da equipa
     * @param inative Estado de eliminação da equipa
     */
    public Team(int id, String name, String country, String genre, String sport, int sportsId, int foundationYear, boolean inative) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.sportsId = sportsId;
        this.foundationYear = foundationYear;
        this.inative = inative;
    }

    /**
     * Construtor para inicializar uma equipa sem o identificador e estado de eliminação.
     *
     * @param name Nome da equipa
     * @param country País da equipa
     * @param genre Género da equipa
     * @param sport Desporto da equipa
     * @param foundationYear Ano de fundação da equipa
     * @param sportsId ID do desporto
     */
    public Team(String name, String country, String genre, String sport, int foundationYear, int sportsId) {
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.foundationYear = foundationYear;
        this.sportsId = sportsId;
    }

    // Getters e Setters

    /**
     * Obtém o identificador da equipa.
     *
     * @return O identificador da equipa
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da equipa.
     *
     * @param id O identificador a definir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome da equipa.
     *
     * @return O nome da equipa
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome da equipa.
     *
     * @param name O nome a definir
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o país da equipa.
     *
     * @return O país da equipa
     */
    public String getCountry() {
        return country;
    }

    /**
     * Define o país da equipa.
     *
     * @param country O país a definir
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Obtém o género da equipa (Masculino ou Feminino).
     *
     * @return O género da equipa
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Define o género da equipa.
     *
     * @param genre O género a definir
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Obtém o desporto praticado pela equipa.
     *
     * @return O desporto da equipa
     */
    public String getSport() {
        return sport;
    }

    /**
     * Define o desporto praticado pela equipa.
     *
     * @param sport O desporto a definir
     */
    public void setSport(String sport) {
        this.sport = sport;
    }

    /**
     * Obtém o ano de fundação da equipa.
     *
     * @return O ano de fundação da equipa
     */
    public int getFoundationYear() {
        return foundationYear;
    }

    /**
     * Define o ano de fundação da equipa.
     *
     * @param foundationYear O ano de fundação a definir
     */
    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    /**
     * Obtém a lista de participações olímpicas da equipa.
     *
     * @return A lista de participações olímpicas
     */
    public List<TeamOlympicParticipation> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Define a lista de participações olímpicas da equipa.
     *
     * @param olympicParticipations A lista de participações olímpicas a definir
     */
    public void setOlympicParticipations(List<TeamOlympicParticipation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }

    /**
     * Verifica se a equipa está eliminada.
     *
     * @return true se a equipa estiver eliminada, false caso contrário
     */
    public boolean isInative() {
        return inative;
    }

    /**
     * Define o estado de eliminação da equipa.
     *
     * @param inative O estado de eliminação a definir
     */
    public void setInative(boolean inative) {
        this.inative = inative;
    }

    /**
     * Obtém o identificador do desporto associado à equipa.
     *
     * @return O identificador do desporto
     */
    public int getSportsId() {
        return sportsId;
    }

    /**
     * Define o identificador do desporto associado à equipa.
     *
     * @param sportsId O identificador do desporto a definir
     */
    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    /**
     * Obtém o resultado gerado aleatoriamente para a equipa.
     *
     * @return O resultado aleatório (Tempo, Distância ou Pontos)
     */
    public String getResultTPD() {
        return resultTPD;
    }

    /**
     * Define o resultado gerado aleatoriamente para a equipa.
     *
     * @param resultTPD O resultado aleatório a definir
     */
    public void setResultTPD(String resultTPD) {
        this.resultTPD = resultTPD;
    }

    /**
     * Obtém o resultado final da equipa (Medalha ou Diploma).
     *
     * @return O resultado final da equipa
     */
    public String getResult() {
        return result;
    }

    /**
     * Define o resultado final da equipa (Medalha ou Diploma).
     *
     * @param result O resultado final a definir
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Retorna uma representação textual da equipa, baseada no seu nome.
     *
     * @return O nome da equipa
     */
    @Override
    public String toString() {
        return this.name; // O nome da equipa será exibido
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id &&
                foundationYear == team.foundationYear &&
                inative == team.inative &&
                sportsId == team.sportsId &&
                Objects.equals(name, team.name) &&
                Objects.equals(country, team.country) &&
                Objects.equals(genre, team.genre) &&
                Objects.equals(sport, team.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, genre, sport, foundationYear, inative, sportsId);
    }
}
