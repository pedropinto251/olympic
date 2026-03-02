package com.lp3_grupo5.lp3_grupo5.DAO;

import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Rule;
import com.lp3_grupo5.lp3_grupo5.Model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela manipulação das regras (Rule) no banco de dados.
 * Fornece métodos para recuperar as regras ativas (onde o campo 'inative' é igual a 0).
 */
public class RuleDAO {

    /**
     * Recupera todas as regras ativas (inative = 0) da tabela 'Rule' no banco de dados.
     *
     * Este método executa uma consulta SQL para obter todas as regras que estão ativas no banco de dados
     * e retorna uma lista de objetos {@link Rule}.
     *
     * @return Uma lista contendo todas as regras ativas encontradas no banco de dados.
     */
    public List<Rule> findAll() {
        List<Rule> rules = new ArrayList<>();
        String query = "SELECT * FROM Rule WHERE inative = 0";

        try (Connection conn = DBConnection.getConnection(); // Estabelece a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(query); // Prepara a consulta SQL
             ResultSet rs = stmt.executeQuery()) { // Executa a consulta e obtém o resultado

            // Itera sobre o resultado da consulta e cria objetos Rule
            while (rs.next()) {
                Rule rule = new Rule(
                        rs.getInt("id"),          // Obtém o id da regra
                        rs.getInt("sport_id"),    // Obtém o id do esporte relacionado
                        rs.getString("rule"),     // Obtém a descrição da regra
                        rs.getBoolean("inative")  // Obtém o status de inatividade da regra
                );
                rules.add(rule);  // Adiciona a regra à lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Caso ocorra um erro, imprime a exceção
        }
        return rules;  // Retorna a lista de regras
    }
}
