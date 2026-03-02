package com.lp3_grupo5.lp3_grupo5.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa um desporto.
 */
public class Sport {
    private int id; // Identificador do desporto
    private String type; // Tipo de desporto (ex.: individual, coletivo)
    private String genre; // Género do desporto (ex.: masculino, feminino)
    private String name; // Nome do desporto
    private String description; // Descrição do desporto
    private int minParticipants; // Número mínimo de participantes
    private String scoringMeasure; // Método de pontuação
    private String oneGame; // Indica se é um jogo único
    private String olympicRecordTime; // Tempo do recorde olímpico
    private int olympicRecordYear; // Ano do recorde olímpico
    private String olympicRecordHolder; // Detentor do recorde olímpico
    private String winnerOlympicTime; // Tempo do vencedor olímpico
    private int winnerOlympicYear; // Ano do vencedor olímpico
    private String winnerOlympicHolder; // Detentor do título olímpico do vencedor
    private boolean inative; // Estado de eliminação do desporto

    private LocalDateTime startTime; // Hora de início do desporto
    private LocalDateTime endTime; // Hora de término do desporto

    private String rule;
    private List<Rule> rules; // Lista para armazenar as regras do desporto

    /**
     * Construtor que inicializa todos os atributos da classe Sport.
     *
     * @param id                  O identificador do desporto
     * @param name                O nome do desporto
     * @param type                O tipo do desporto (ex: individual, coletivo)
     * @param genre               O género do desporto (ex: masculino, feminino)
     * @param description         A descrição do desporto
     * @param minParticipants     Número mínimo de participantes
     * @param scoringMeasure      Método de pontuação do desporto
     * @param oneGame             Indica se o desporto é um jogo único
     * @param olympicRecordTime   Tempo do recorde olímpico
     * @param olympicRecordYear   Ano do recorde olímpico
     * @param olympicRecordHolder Detentor do recorde olímpico
     * @param winnerOlympicYear   Ano do vencedor olímpico
     * @param winnerOlympicTime   Tempo do vencedor olímpico
     * @param winnerOlympicHolder Detentor do título olímpico do vencedor
     * @param inative             Estado de eliminação do desporto
     */
    public Sport(int id, String name, String type, String genre, String description, int minParticipants,
                 String scoringMeasure, String oneGame, String olympicRecordTime, int olympicRecordYear,
                 String olympicRecordHolder, int winnerOlympicYear, String winnerOlympicTime,
                 String winnerOlympicHolder, boolean inative) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.description = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecordTime = olympicRecordTime;
        this.olympicRecordYear = olympicRecordYear;
        this.olympicRecordHolder = olympicRecordHolder;
        this.winnerOlympicYear = winnerOlympicYear;
        this.winnerOlympicTime = winnerOlympicTime;
        this.winnerOlympicHolder = winnerOlympicHolder;
        this.inative = inative;
    }

    public Sport(int id, String name, String type, String genre, String description, int minParticipants,
                 String scoringMeasure, String oneGame, String olympicRecordTime, int olympicRecordYear,
                 String olympicRecordHolder, int winnerOlympicYear, String winnerOlympicTime,
                 String winnerOlympicHolder, boolean inative, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.description = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecordTime = olympicRecordTime;
        this.olympicRecordYear = olympicRecordYear;
        this.olympicRecordHolder = olympicRecordHolder;
        this.winnerOlympicYear = winnerOlympicYear;
        this.winnerOlympicTime = winnerOlympicTime;
        this.winnerOlympicHolder = winnerOlympicHolder;
        this.inative = inative;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Sport() {
    }

    /**
     * Construtor simplificado para inicializar o desporto sem o ID e outras informações.
     *
     * @param name            Nome do desporto
     * @param type            Tipo do desporto (ex: individual, coletivo)
     * @param genre           Género do desporto (ex: masculino, feminino)
     * @param description     Descrição do desporto
     * @param minParticipants Número mínimo de participantes
     * @param scoringMeasure  Método de pontuação do desporto
     * @param oneGame         Indica se o desporto é um jogo único
     */
    public Sport(String name, String type, String genre, String description, int minParticipants,
                 String scoringMeasure, String oneGame) {
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.description = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
    }

    /**
     * Verifica se o desporto está marcado como eliminado.
     *
     * @return true se o desporto estiver eliminado, false caso contrário
     */
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

    /**
     * Obtém o identificador do desporto.
     *
     * @return O identificador do desporto
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do desporto.
     *
     * @param id O identificador a definir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o tipo do desporto.
     *
     * @return O tipo do desporto
     */
    public String getType() {
        return type;
    }

    /**
     * Define o tipo do desporto.
     *
     * @param type O tipo a definir
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtém o género do desporto.
     *
     * @return O género do desporto
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Define o género do desporto.
     *
     * @param genre O género a definir
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Obtém o nome do desporto.
     *
     * @return O nome do desporto
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do desporto.
     *
     * @param name O nome a definir
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém a descrição do desporto.
     *
     * @return A descrição do desporto
     */
    public String getDescription() {
        return description;
    }

    /**
     * Define a descrição do desporto.
     *
     * @param description A descrição a definir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtém o número mínimo de participantes do desporto.
     *
     * @return O número mínimo de participantes
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Define o número mínimo de participantes do desporto.
     *
     * @param minParticipants O número mínimo a definir
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Obtém o método de pontuação do desporto.
     *
     * @return O método de pontuação
     */
    public String getScoringMeasure() {
        return scoringMeasure;
    }

    /**
     * Define o método de pontuação do desporto.
     *
     * @param scoringMeasure O método de pontuação a definir
     */
    public void setScoringMeasure(String scoringMeasure) {
        this.scoringMeasure = scoringMeasure;
    }

    /**
     * Obtém a informação se o desporto é um jogo único.
     *
     * @return Informação sobre o jogo único
     */
    public String getOneGame() {
        return oneGame;
    }

    /**
     * Define se o desporto é um jogo único.
     *
     * @param oneGame A informação a definir
     */
    public void setOneGame(String oneGame) {
        this.oneGame = oneGame;
    }

    /**
     * Obtém o tempo do recorde olímpico.
     *
     * @return O tempo do recorde olímpico
     */
    public String getOlympicRecordTime() {
        return olympicRecordTime;
    }

    /**
     * Define o tempo do recorde olímpico.
     *
     * @param olympicRecordTime O tempo a definir
     */
    public void setOlympicRecordTime(String olympicRecordTime) {
        this.olympicRecordTime = olympicRecordTime;
    }

    /**
     * Obtém o ano do recorde olímpico.
     *
     * @return O ano do recorde olímpico
     */
    public int getOlympicRecordYear() {
        return olympicRecordYear;
    }

    /**
     * Define o ano do recorde olímpico.
     *
     * @param olympicRecordYear O ano a definir
     */
    public void setOlympicRecordYear(int olympicRecordYear) {
        this.olympicRecordYear = olympicRecordYear;
    }

    /**
     * Obtém o detentor do recorde olímpico.
     *
     * @return O detentor do recorde olímpico
     */
    public String getOlympicRecordHolder() {
        return olympicRecordHolder;
    }

    /**
     * Define o detentor do recorde olímpico.
     *
     * @param olympicRecordHolder O detentor a definir
     */
    public void setOlympicRecordHolder(String olympicRecordHolder) {
        this.olympicRecordHolder = olympicRecordHolder;
    }

    /**
     * Obtém o tempo do vencedor olímpico.
     *
     * @return O tempo do vencedor olímpico
     */
    public String getWinnerOlympicTime() {
        return winnerOlympicTime;
    }

    /**
     * Define o tempo do vencedor olímpico.
     *
     * @param winnerOlympicTime O tempo a definir
     */
    public void setWinnerOlympicTime(String winnerOlympicTime) {
        this.winnerOlympicTime = winnerOlympicTime;
    }

    /**
     * Obtém o ano do vencedor olímpico.
     *
     * @return O ano do vencedor olímpico
     */
    public int getWinnerOlympicYear() {
        return winnerOlympicYear;
    }

    /**
     * Define o ano do vencedor olímpico.
     *
     * @param winnerOlympicYear O ano a definir
     */
    public void setWinnerOlympicYear(int winnerOlympicYear) {
        this.winnerOlympicYear = winnerOlympicYear;
    }

    /**
     * Obtém o detentor do título olímpico do vencedor.
     *
     * @return O detentor do título olímpico do vencedor
     */
    public String getWinnerOlympicHolder() {
        return winnerOlympicHolder;
    }

    /**
     * Define o detentor do título olímpico do vencedor.
     *
     * @param winnerOlympicHolder O detentor a definir
     */
    public void setWinnerOlympicHolder(String winnerOlympicHolder) {
        this.winnerOlympicHolder = winnerOlympicHolder;
    }

    /**
     * Obtém a lista de regras do desporto.
     *
     * @return A lista de regras
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Define a lista de regras do desporto.
     *
     * @param rules A lista de regras a definir
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Retorna uma representação textual do desporto.
     *
     * @return O nome do desporto
     */


    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Sport(int id, String name, String type, String genre, String description, int minParticipants,
                 String scoringMeasure, String oneGame, String olympicRecordTime, int olympicRecordYear,
                 String olympicRecordHolder, int winnerOlympicYear, String winnerOlympicTime,
                 String winnerOlympicHolder, boolean inative, String rule) {
        // Inicializar os atributos existentes
        this.id = id;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.description = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecordTime = olympicRecordTime;
        this.olympicRecordYear = olympicRecordYear;
        this.olympicRecordHolder = olympicRecordHolder;
        this.winnerOlympicYear = winnerOlympicYear;
        this.winnerOlympicTime = winnerOlympicTime;
        this.winnerOlympicHolder = winnerOlympicHolder;
        this.inative = inative;
        this.rule = rule; // Inicializando a lista de regras
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sport sport = (Sport) o;
        return id == sport.id &&
                minParticipants == sport.minParticipants &&
                olympicRecordYear == sport.olympicRecordYear &&
                winnerOlympicYear == sport.winnerOlympicYear &&
                inative == sport.inative &&
                Objects.equals(type, sport.type) &&
                Objects.equals(genre, sport.genre) &&
                Objects.equals(name, sport.name) &&
                Objects.equals(description, sport.description) &&
                Objects.equals(scoringMeasure, sport.scoringMeasure) &&
                Objects.equals(oneGame, sport.oneGame) &&
                Objects.equals(olympicRecordTime, sport.olympicRecordTime) &&
                Objects.equals(olympicRecordHolder, sport.olympicRecordHolder) &&
                Objects.equals(winnerOlympicTime, sport.winnerOlympicTime) &&
                Objects.equals(winnerOlympicHolder, sport.winnerOlympicHolder) &&
                Objects.equals(rule, sport.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, genre, name, description, minParticipants, scoringMeasure, oneGame, olympicRecordTime, olympicRecordYear, olympicRecordHolder, winnerOlympicTime, winnerOlympicYear, winnerOlympicHolder, inative, rule);
    }

    /*
    @Override
    public String toString() {
        return "Sport{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", genre='" + genre + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minParticipants=" + minParticipants +
                ", scoringMeasure='" + scoringMeasure + '\'' +
                ", oneGame='" + oneGame + '\'' +
                ", olympicRecordTime='" + olympicRecordTime + '\'' +
                ", olympicRecordYear=" + olympicRecordYear +
                ", olympicRecordHolder='" + olympicRecordHolder + '\'' +
                ", winnerOlympicTime='" + winnerOlympicTime + '\'' +
                ", winnerOlympicYear=" + winnerOlympicYear +
                ", winnerOlympicHolder='" + winnerOlympicHolder + '\'' +
                ", inative=" + inative +
                ", rule='" + rule + '\'' +
                '}';
    }*/
    @Override
    public String toString() {
        return name; // O nome do desporto será exibido }
    }


}
