package com.lp3_grupo5.lp3_grupo5.Model;

import java.util.Objects;

/**
 * A classe Event representa um evento com informações detalhadas, como o ano de realização,
 * país de origem, mascote associado e o local.
 */
public class Event {
    /**
     * Identificador único do evento
     */
    private int id;

    /**
     * Ano de realização do evento
     */
    private int year;

    /**
     * País onde o evento é realizado
     */
    private String country;

    /**
     * Mascote representativa do evento
     */
    private String mascot;

    /**
     * Identificador do local onde o evento acontece
     */

    private boolean inative;

    /**
     * Construtor completo para criar um novo evento com todos os detalhes.
     *
     * @param id          Identificador do evento
     * @param year        Ano de realização do evento
     * @param country     País onde o evento é realizado
     * @param mascot      Mascote associada ao evento
     */


    /**
     * Construtor padrão sem argumentos.
     * Permite criar um evento sem inicializar os campos imediatamente.
     */
    public Event() {
    }

    public Event(int id, int year, String country, String mascot, boolean inative) {

        this.id = id;
        this.year = year;
        this.country = country;
        this.mascot = mascot;
        this.inative = inative;
    }

    /**
     * Construtor sem o campo id, usado para criar novos eventos antes de serem persistidos no banco de dados.
     *
     * @param year        Ano de realização do evento
     * @param country     País onde o evento é realizado
     * @param mascot      Mascote associada ao evento
     * @param inative     Estado de inatividade do evento
     */
    public Event(int year, String country, String mascot, boolean inative) {
        this.year = year;
        this.country = country;
        this.mascot = mascot;
        this.inative = inative;
    }
    /**
     * Obtém o identificador do evento.
     *
     * @return id do evento.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do evento.
     *
     * @param id Novo identificador para o evento.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o ano de realização do evento.
     *
     * @return ano do evento.
     */
    public int getYear() {
        return year;
    }

    /**
     * Define o ano de realização do evento.
     *
     * @param year Novo ano para o evento.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Obtém o país onde o evento é realizado.
     *
     * @return país do evento.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Define o país onde o evento é realizado.
     *
     * @param country Novo país para o evento.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Obtém a mascote associada ao evento.
     *
     * @return mascote do evento.
     */
    public String getMascot() {
        return mascot;
    }

    /**
     * Define a mascote associada ao evento.
     *
     * @param mascot Nova mascote para o evento.
     */
    public void setMascot(String mascot) {
        this.mascot = mascot;
    }

    /**
     * Obtém o identificador do local onde o evento é realizado.
     *
     * @return identificador do local do evento.
     */




    public boolean isInative() {

        return inative;
    }

    public void setInative(boolean inative) {
        this.inative = inative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                year == event.year &&
                inative == event.inative &&
                Objects.equals(country, event.country) &&
                Objects.equals(mascot, event.mascot);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", year=" + year +
                ", country='" + country + '\'' +
                ", mascot='" + mascot + '\'' +
                ", inative=" + inative +
                '}';
    }
}
