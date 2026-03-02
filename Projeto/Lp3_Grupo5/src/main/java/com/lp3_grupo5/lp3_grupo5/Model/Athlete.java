package com.lp3_grupo5.lp3_grupo5.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um atleta.
 * <p>
 * Esta classe contém informações detalhadas sobre um atleta, incluindo nome, país, género,
 * altura, peso, data de nascimento e participações olímpicas. Oferece métodos para gerir
 * estas informações e permite adicionar participações olímpicas.
 * </p>
 */
public class Athlete {

    /**
     * Identificador único do atleta.
     */
    private int id;

    /**
     * Nome do atleta.
     */
    private String name;

    /**
     * País de origem do atleta.
     */
    private String country;

    /**
     * Género do atleta (e.g., "Masculino", "Feminino").
     */
    private String genre;

    /**
     * Altura do atleta em centímetros.
     */
    private int height;

    /**
     * Peso do atleta em quilogramas.
     */
    private int weight;

    /**
     * Data de nascimento do atleta no formato de string.
     */
    private String dateOfBirth;

    /**
     * Indica se o atleta está inativo ou foi excluído.
     */
    private boolean inative;

    /**
     * Foto do atleta no formato de string.
     */
    private String photo;

    /**
     * Lista de participações olímpicas do atleta.
     */
    private List<Participation> olympicParticipations;

    /**
     * Identificador do desporto praticado pelo atleta.
     */
    private int sportId;

    /**
     * Resultado associado ao desempenho do atleta (não especificado detalhadamente).
     */
    private String resultTPD;

    // ======================== CONSTRUTORES ========================

    /**
     * Construtor completo para inicializar um atleta com todos os atributos.
     *
     * @param id           o ID do atleta.
     * @param name         o nome do atleta.
     * @param country      o país de origem do atleta.
     * @param genre        o género do atleta.
     * @param height       a altura do atleta em centímetros.
     * @param weight       o peso do atleta em quilogramas.
     * @param dateOfBirth  a data de nascimento do atleta como String.
     * @param inative      indica se o atleta está inativo (true para inativo).
     * @param photo        a foto do atleta.
     */
    public Athlete(int id, String name, String country, String genre, int height, int weight, String dateOfBirth, boolean inative, String photo) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.inative = inative;
        this.photo = photo;
        this.olympicParticipations = new ArrayList<>();
    }

    /**
     * Construtor para criar um atleta sem especificar o ID.
     *
     * @param name         o nome do atleta.
     * @param country      o país de origem do atleta.
     * @param genre        o género do atleta.
     * @param height       a altura do atleta em centímetros.
     * @param weight       o peso do atleta em quilogramas.
     * @param dateOfBirth  a data de nascimento do atleta como String.
     * @param photo        a foto do atleta.
     */
    public Athlete(String name, String country, String genre, int height, int weight, String dateOfBirth, String photo) {
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.inative = false; // Assume que novos atletas não estão inativos
        this.photo = photo;
        this.olympicParticipations = new ArrayList<>();
    }

    /**
     * Construtor para criar um atleta com ID, nome e país apenas.
     *
     * @param id      o ID do atleta.
     * @param name    o nome do atleta.
     * @param country o país de origem do atleta.
     */
    public Athlete(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.inative = false;  // Define como ativo por padrão
        this.olympicParticipations = new ArrayList<>();
    }

    public Athlete(int id, String name, String country, String gender) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.genre = gender;

    }

    /**
     * Construtor padrão para criar um atleta vazio.
     */
    public Athlete() {
        this.olympicParticipations = new ArrayList<>();
    }

    /**
     * Construtor alternativo utilizando {@link LocalDate} para a data de nascimento.
     *
     * @param athleteId    o ID do atleta.
     * @param name         o nome do atleta.
     * @param country      o país de origem do atleta.
     * @param genre        o género do atleta.
     * @param height       a altura do atleta em centímetros.
     * @param weight       o peso do atleta em quilogramas.
     * @param dateOfBirth  a data de nascimento como um objeto {@link LocalDate}.
     * @param inative      indica se o atleta está inativo.
     * @param photo        a foto do atleta.
     */
    public Athlete(int athleteId, String name, String country, String genre, int height, int weight, LocalDate dateOfBirth, boolean inative, String photo) {
        this.id = athleteId;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = String.valueOf(dateOfBirth);
        this.inative = inative;
        this.photo = photo;
    }

    // ======================== MÉTODOS GETTERS E SETTERS ========================

    /** @return o ID do atleta. */
    public int getId() {
        return id;
    }

    /** @param id define o ID do atleta. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return o nome do atleta. */
    public String getName() {
        return name;
    }

    /** @param name define o nome do atleta. */
    public void setName(String name) {
        this.name = name;
    }

    /** @return o país de origem do atleta. */
    public String getCountry() {
        return country;
    }

    /** @param country define o país de origem do atleta. */
    public void setCountry(String country) {
        this.country = country;
    }

    /** @return o género do atleta. */
    public String getGenre() {
        return genre;
    }

    /** @param genre define o género do atleta. */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /** @return a altura do atleta em centímetros. */
    public int getHeight() {
        return height;
    }

    /** @param height define a altura do atleta em centímetros. */
    public void setHeight(int height) {
        this.height = height;
    }

    /** @return o peso do atleta em quilogramas. */
    public int getWeight() {
        return weight;
    }

    /** @param weight define o peso do atleta em quilogramas. */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /** @return a data de nascimento do atleta como String. */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /** @param dateOfBirth define a data de nascimento do atleta como String. */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /** @return true se o atleta estiver inativo, false caso contrário. */
    public boolean isInative() {
        return inative;
    }

    /** @param inative define se o atleta está inativo. */
    public void setInative(boolean inative) {
        this.inative = inative;
    }

    /** @return a foto do atleta. */
    public String getPhoto() {
        return photo;
    }

    /** @param photo define a foto do atleta. */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /** @return a lista de participações olímpicas do atleta. */
    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    /** @param olympicParticipations define a lista de participações olímpicas do atleta. */
    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }

    /** @return o ID do desporto do atleta. */
    public int getSportId() {
        return sportId;
    }

    /** @param sportId define o ID do desporto do atleta. */
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id); // Retorna apenas o ID
    }


    // ======================== CLASSE INTERNA PARTICIPATION ========================

    /**
     * Classe interna que representa uma participação olímpica de um atleta.
     */
    public static class Participation {

        private int year;   // Ano da participação
        private int gold;   // Medalhas de ouro
        private int silver; // Medalhas de prata
        private int bronze; // Medalhas de bronze

        /** Construtor padrão para criar uma participação vazia. */
        public Participation() {
        }

        /**
         * Construtor para inicializar uma participação olímpica.
         *
         * @param year   o ano da participação.
         * @param gold   o número de medalhas de ouro conquistadas.
         * @param silver o número de medalhas de prata conquistadas.
         * @param bronze o número de medalhas de bronze conquistadas.
         */
        public Participation(int year, int gold, int silver, int bronze) {
            this.year = year;
            this.gold = gold;
            this.silver = silver;
            this.bronze = bronze;
        }

        /** @return o ano da participação. */
        public int getYear() {
            return year;
        }

        /** @param year define o ano da participação. */
        public void setYear(int year) {
            this.year = year;
        }

        /** @return o número de medalhas de ouro conquistadas. */
        public int getGold() {
            return gold;
        }

        /** @param gold define o número de medalhas de ouro conquistadas. */
        public void setGold(int gold) {
            this.gold = gold;
        }

        /** @return o número de medalhas de prata conquistadas. */
        public int getSilver() {
            return silver;
        }

        /** @param silver define o número de medalhas de prata conquistadas. */
        public void setSilver(int silver) {
            this.silver = silver;
        }

        /** @return o número de medalhas de bronze conquistadas. */
        public int getBronze() {
            return bronze;
        }

        /** @param bronze define o número de medalhas de bronze conquistadas. */
        public void setBronze(int bronze) {
            this.bronze = bronze;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Athlete athlete = (Athlete) o;

        if (id != athlete.id) return false;
        if (height != athlete.height) return false;
        if (weight != athlete.weight) return false;
        if (inative != athlete.inative) return false;
        if (name != null ? !name.equals(athlete.name) : athlete.name != null) return false;
        if (country != null ? !country.equals(athlete.country) : athlete.country != null) return false;
        if (genre != null ? !genre.equals(athlete.genre) : athlete.genre != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(athlete.dateOfBirth) : athlete.dateOfBirth != null) return false;
        return photo != null ? photo.equals(athlete.photo) : athlete.photo == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + height;
        result = 31 * result + weight;
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (inative ? 1 : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        return result;
    }

}
