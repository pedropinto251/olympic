package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Rule;

import java.sql.SQLException;
import java.util.List;

public interface IRuleDAO {

    /**
     * Obtém todas as regras armazenadas na base de dados.
     *
     * @return Uma lista contendo todas as {@link Rule} encontradas.
     * @throws SQLException Se ocorrer um erro durante a consulta à base de dados.
     */
    List<Rule> findAll() throws SQLException;
}
