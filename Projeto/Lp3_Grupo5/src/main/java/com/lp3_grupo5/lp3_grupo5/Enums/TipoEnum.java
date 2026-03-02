/**
 * Representa um enumerador para os tipos de ambientes com uma descrição associada.
 * <p>
 * Este enumerador define dois tipos de ambientes: "outdoor" e "interior",
 * com descrições associadas que podem ser obtidas programaticamente.
 * </p>
 */
package com.lp3_grupo5.lp3_grupo5.Enums;

public enum TipoEnum {
    /**
     * Representa o ambiente exterior ("outdoor").
     * A descrição associada é "outdoor".
     */
    Out("outdoor"),

    /**
     * Representa o ambiente interior ("interior").
     * A descrição associada é "interior".
     */
    In("interior");

    /**
     * A descrição textual do tipo de ambiente.
     */
    private final String description;

    /**
     * Construtor para inicializar a descrição do tipo de ambiente.
     *
     * @param description a descrição textual associada ao tipo de ambiente.
     */
    TipoEnum(String description) {
        this.description = description;
    }

    /**
     * Obtém a descrição associada ao tipo de ambiente.
     *
     * @return a descrição textual do tipo de ambiente.
     */
    public String getDescription() {
        return description;
    }
}
