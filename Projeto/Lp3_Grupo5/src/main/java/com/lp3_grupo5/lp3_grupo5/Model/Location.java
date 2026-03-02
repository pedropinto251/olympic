package com.lp3_grupo5.lp3_grupo5.Model;

import java.util.Objects;

/**
 * Classe que representa um local (Location).
 * <p>
 * Contém informações sobre locais utilizados para eventos, incluindo morada, cidade,
 * capacidade, ano de construção, tipo, status de atividade e ID de evento associado.
 * </p>
 */
public class Location {

    /**
     * ID do local.
     */
    private int id;

    /**
     * Morada do local.
     */
    private String address;

    /**
     * Cidade onde o local está situado.
     */
    private String city;

    /**
     * Capacidade máxima do local.
     */
    private int capacity;

    /**
     * Ano de construção do local.
     */
    private int yearBuilt;

    /**
     * Indica se o local foi excluído/inativo.
     */
    private boolean inative;

    /**
     * ID do evento associado ao local.
     */
    private int eventId;

    /**
     * Tipo do local (e.g., "indoor", "outdoor").
     */
    private String type;

    /**
     * Construtor que inicializa todos os campos.
     *
     * @param id        ID do local.
     * @param address   Morada do local.
     * @param city      Cidade onde o local está situado.
     * @param capacity  Capacidade máxima do local.
     * @param yearBuilt Ano de construção do local.
     * @param inative   Status de atividade do local (true para inativo).
     * @param type      Tipo do local (e.g., "indoor", "outdoor").
     * @param eventId   ID do evento associado ao local.
     */
    public Location(int id, String address, String city, int capacity, int yearBuilt, boolean inative, String type, int eventId) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.yearBuilt = yearBuilt;
        this.inative = inative;
        this.type = type;
        this.eventId = eventId;
    }

    /**
     * Construtor alternativo com campos principais.
     *
     * @param id        ID do local.
     * @param address   Morada do local.
     * @param city      Cidade onde o local está situado.
     * @param capacity  Capacidade máxima do local.
     * @param yearBuilt Ano de construção do local.
     * @param inative   Status de atividade do local (true para inativo).
     * @param eventId   ID do evento associado ao local.
     */
    public Location(int id, String address, String city, int capacity, int yearBuilt, boolean inative, int eventId) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.yearBuilt = yearBuilt;
        this.inative = inative;
        this.eventId = eventId;
    }

    /**
     * Construtor padrão (sem parâmetros).
     */
    public Location() {
    }

    // ======================== MÉTODOS GETTERS E SETTERS ========================

    /**
     * Obtém o ID do local.
     *
     * @return ID do local.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do local.
     *
     * @param id o novo ID do local.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém a morada do local.
     *
     * @return morada do local.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Define a morada do local.
     *
     * @param address a nova morada do local.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Obtém a cidade do local.
     *
     * @return cidade do local.
     */
    public String getCity() {
        return city;
    }

    /**
     * Define a cidade do local.
     *
     * @param city a nova cidade do local.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Obtém a capacidade máxima do local.
     *
     * @return capacidade do local.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Define a capacidade máxima do local.
     *
     * @param capacity a nova capacidade do local.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Obtém o ano de construção do local.
     *
     * @return ano de construção do local.
     */
    public int getYearBuilt() {
        return yearBuilt;
    }

    /**
     * Define o ano de construção do local.
     *
     * @param yearBuilt o novo ano de construção do local.
     */
    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    /**
     * Verifica se o local está inativo.
     *
     * @return true se o local está inativo, caso contrário false.
     */
    public boolean isInative() {
        return inative;
    }

    /**
     * Define o status de atividade do local.
     *
     * @param inative true para inativo, false para ativo.
     */
    public void setInative(boolean inative) {
        this.inative = inative;
    }

    /**
     * Obtém o ID do evento associado ao local.
     *
     * @return ID do evento associado.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Define o ID do evento associado ao local.
     *
     * @param eventId o novo ID do evento associado.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Obtém o tipo do local.
     *
     * @return tipo do local.
     */
    public String getType() {
        return type;
    }

    /**
     * Define o tipo do local.
     *
     * @param type o novo tipo do local.
     */
    public void setType(String type) {
        this.type = type;
    }

    // Método equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id == location.id &&
                capacity == location.capacity &&
                yearBuilt == location.yearBuilt &&
                inative == location.inative &&
                eventId == location.eventId &&
                Objects.equals(address, location.address) &&
                Objects.equals(city, location.city) &&
                Objects.equals(type, location.type);
    }

    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, address, city, capacity, yearBuilt, inative, eventId, type);
    }

    // Método toString (opcional)
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                ", yearBuilt=" + yearBuilt +
                ", inative=" + inative +
                ", eventId=" + eventId +
                ", type='" + type + '\'' +
                '}';
    }
}
