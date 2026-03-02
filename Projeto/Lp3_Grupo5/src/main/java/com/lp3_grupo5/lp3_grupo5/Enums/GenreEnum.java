/**
 * Representa um enumerador para géneros com uma descrição associada.
 * <p>
 * Este enumerador define duas constantes que representam os géneros masculino e feminino,
 * cada uma delas com uma descrição textual associada em inglês.
 * </p>
 */
package com.lp3_grupo5.lp3_grupo5.Enums;

public enum GenreEnum {

    /**
     * Representa o género masculino.
     * A descrição associada é "Men".
     */
    MASCULINO("Men"),

    /**
     * Representa o género feminino.
     * A descrição associada é "Women".
     */
    FEMININO("Women");

    /**
     * A descrição textual do género.
     */
    private final String description;

    /**
     * Construtor para inicializar a descrição do género.
     *
     * @param description a descrição textual associada ao género.
     */
    GenreEnum(String description) {
        this.description = description;
    }

    /**
     * Obtém a descrição associada ao género.
     *
     * @return a descrição textual do género.
     */
    public String getDescription() {
        return description;
    }
}
