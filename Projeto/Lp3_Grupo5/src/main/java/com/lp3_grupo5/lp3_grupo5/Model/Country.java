package com.lp3_grupo5.lp3_grupo5.Model;

/**
 * Classe que representa um país.
 * <p>
 * Contém informações básicas sobre um país, incluindo o seu código e nome.
 * </p>
 */
public class Country {

    /**
     * Código do país (geralmente em formato ISO 3166-1 alpha-2, e.g., "BR" para Brasil).
     */
    private String code;

    /**
     * Nome do país (e.g., "Brasil", "Portugal").
     */
    private String name;

    /**
     * Construtor para inicializar um país com código e nome.
     *
     * @param code o código do país.
     * @param name o nome do país.
     */
    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // ======================== MÉTODOS GETTERS E SETTERS ========================

    /**
     * Obtém o código do país.
     *
     * @return o código do país.
     */
    public String getCode() {
        return code;
    }

    /**
     * Define o código do país.
     *
     * @param code o novo código do país.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtém o nome do país.
     *
     * @return o nome do país.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do país.
     *
     * @param name o novo nome do país.
     */
    public void setName(String name) {
        this.name = name;
    }

    // ======================== MÉTODOS UTILITÁRIOS ========================

    /**
     * Retorna a representação textual do objeto.
     * Neste caso, retorna apenas o nome do país.
     *
     * @return o nome do país.
     */
    @Override
    public String toString() {
        return name;
    }
}
