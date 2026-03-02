/**
 * Representa um enumerador relacionado com países e interação com a base de dados.
 * <p>
 * Este enumerador inclui um método para obter todos os países disponíveis na base de dados.
 * Atualmente, possui apenas uma constante `EMPTY` que serve como placeholder.
 * </p>
 */
package com.lp3_grupo5.lp3_grupo5.Enums;

import com.lp3_grupo5.lp3_grupo5.DAO.CountryDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Country;

public enum CountryEnum {
    /**
     * Placeholder para representar um estado inicial vazio.
     */
    EMPTY;

    /**
     * Obtém todos os países armazenados na base de dados.
     * <p>
     * Este método utiliza o DAO {@link CountryDAO} para realizar uma consulta à base de dados
     * e retornar uma lista de objetos {@link Country}.
     * </p>
     *
     * @return um array de {@link Country} com todos os países encontrados na base de dados.
     *         Retorna um array vazio se ocorrer algum erro durante a consulta.
     */
    public Country[] getAllCountriesFromDatabase() {
        CountryDAO countryDAO = new CountryDAO();
        try {
            System.out.println("Buscar países na base de dados...");
            // Obtém todos os países da base de dados
            return countryDAO.getAllCountries().toArray(new Country[0]);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return new Country[0]; // Retorna um array vazio em caso de erro
        }
    }
}
