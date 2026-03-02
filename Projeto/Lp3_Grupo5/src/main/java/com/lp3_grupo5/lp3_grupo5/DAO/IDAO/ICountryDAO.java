package com.lp3_grupo5.lp3_grupo5.DAO.IDAO;

import com.lp3_grupo5.lp3_grupo5.Model.Country;
import javafx.collections.ObservableList;

public interface ICountryDAO {

    /**
     * Busca todos os países do banco de dados.
     *
     * @return Lista observável contendo os países.
     */
    ObservableList<Country> getAllCountries();
}