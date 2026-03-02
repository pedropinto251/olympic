package com.lp3_grupo5.lp3_grupo5.Model;

/**
 * Classe que representa uma regra de um desporto.
 */
public class Rule {
    private int id; // Identificador da regra
    private int sportId; // ID do desporto relacionado
    private String rule; // Texto da regra
    private boolean inative; // Estado de eliminação da regra

    public Rule(){
    }

    public Rule(int id, int sportId, String rule, boolean inative){
        this.id = id;
        this.sportId = sportId;
        this.rule = rule;
        this.inative = inative;
    }

    // Getters e Setters

    /**
     * Obtém o identificador da regra.
     *
     * @return O identificador da regra
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da regra.
     *
     * @param id O identificador a definir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o ID do desporto relacionado.
     *
     * @return O ID do desporto
     */
    public int getSportId() {
        return sportId;
    }

    /**
     * Define o ID do desporto relacionado.
     *
     * @param sportId O ID a definir
     */
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    /**
     * Obtém o texto da regra.
     *
     * @return O texto da regra
     */
    public String getRule() {
        return rule;
    }

    /**
     * Define o texto da regra.
     *
     * @param rule O texto da regra a definir
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * Verifica se a regra está marcada como eliminada.
     *
     * @return true se a regra estiver eliminada, false caso contrário
     */
    public boolean isInative() {
        return inative;
    }

    /**
     * Define o estado de eliminação da regra.
     *
     * @param inative true se a regra deve ser marcada como eliminada, false caso contrário
     */
    public void setInative(boolean inative) {
        this.inative = inative;
    }
}
