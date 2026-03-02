package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ICountryDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Classe DAO responsável pela interação com a base de dados para operações relacionadas aos países.
 * Implementa a interface {@link ICountryDAO} e fornece o método para recuperar todos os países disponíveis.
 */
public class CountryDAO implements ICountryDAO {

    /**
     * Recupera todos os países armazenados na base de dados.
     * Realiza uma consulta SQL para obter os códigos e nomes de todos os países presentes na tabela {@code Countries}.
     *
     * @return Uma lista observável de {@link Country} contendo todos os países.
     */
    @Override
    public ObservableList<Country> getAllCountries() {

        ObservableList<Country> countries = observableArrayList();

        // Consulta SQL para buscar todos os países na base de dados
        String sql = "SELECT CountryCode, CountryName FROM Countries";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre os resultados e adiciona cada país à lista
            while (rs.next()) {
                Country country = new Country(
                        rs.getString("CountryCode"),  // Código do país
                        rs.getString("CountryName")   // Nome do país
                );
                countries.add(country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retorna a lista de países
        return countries;
    }
}
