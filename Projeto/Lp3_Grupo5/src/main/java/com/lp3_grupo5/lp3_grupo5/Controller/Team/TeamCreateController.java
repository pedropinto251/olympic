package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.CountryDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Model.Team;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controlador responsável pela criação de equipas.
 *
 * Este controlador permite ao utilizador preencher os dados de uma nova equipa, como nome, ano de fundação, gênero, país e esporte.
 * Após validação, os dados são enviados para o banco de dados para criação da equipa.
 */
public class TeamCreateController {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private ChoiceBox<Country> countryCB;

    @FXML
    private TextField foundationYearTF;

    @FXML
    private ChoiceBox<String> genderCB;

    @FXML
    private TextField nameTF;

    @FXML
    private ChoiceBox<Sport> sportCB;

    private TeamDAO teamDAO = new TeamDAO();

    private SportDAO sportDAO = new SportDAO();

    private CountryDAO countryDAO = new CountryDAO();

    /**
     * Método de inicialização do controlador.
     * Este método configura os ChoiceBoxes e carrega as listas de desportos e países a partir do banco de dados.
     */
    @FXML
    public void initialize() {
        // Define a data atual no campo de texto (apenas o ano)
        foundationYearTF.setText(String.valueOf(LocalDate.now().getYear()));

        // Desabilita a interação do usuário para o campo de ano de fundação
        foundationYearTF.setDisable(true);

        // Adiciona as opções de gênero
        genderCB.getItems().addAll("Men", "Women");

        // Carrega os desportos
        ObservableList<Sport> sportsList = FXCollections.observableArrayList(sportDAO.findAllColletive());
        sportCB.setItems(sportsList);

        // Carrega os países
        ObservableList<Country> countryList = FXCollections.observableArrayList(countryDAO.getAllCountries());
        countryCB.setItems(countryList);

    }

    /**
     * Método chamado ao clicar no botão "Criar". Valida os dados inseridos e, se forem válidos, cria a equipa.
     *
     * @param event O evento de ação do botão.
     */
    @FXML
    void handleBtnCreate(ActionEvent event) {

        String name = nameTF.getText();

        // Valida se o nome tem pelo menos 3 caracteres
        if (name.length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "O nome da equipa deve ter pelo menos 3 caracteres.");
            return; // Retorna sem continuar o processo de criação
        }

        // Valida os campos
        if (isInputValid()) {
            String genre = genderCB.getValue().trim();

            Country country = countryCB.getValue();
            Sport sport = sportCB.getValue();
            int foundationYear = Integer.parseInt(foundationYearTF.getText().trim());

            int sportsId = sport.getId();

            // Cria a equipa com os dados inseridos
            Team teams = new Team(name, country.getCode(), genre, sport.getName(), foundationYear, sportsId);

            // Tenta inserir a equipa no banco de dados
            try {
                if (teamDAO.insertTeam(teams)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Equipa criada com sucesso!");
                    resetFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao criar uma equipa. Tente novamente!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao acessar o banco de dados: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Por favor, preencha todos os campos corretamente.");
        }
    }

    /**
     * Valida os campos inseridos pelo utilizador.
     *
     * @return true se todos os campos estiverem preenchidos corretamente, false caso contrário.
     */
    private boolean isInputValid() {
        return nameTF.getText() != null && !nameTF.getText().isEmpty() &&
                foundationYearTF.getText() != null &&
                sportCB.getValue() != null &&
                countryCB.getValue() != null &&
                genderCB.getValue() != null && (genderCB.getValue().equals("Men") || genderCB.getValue().equals("Women"));
    }

    /**
     * Limpa os campos do formulário.
     */
    private void resetFields() {
        nameTF.clear();
        genderCB.getSelectionModel().clearSelection();
        sportCB.getSelectionModel().clearSelection();
        countryCB.getSelectionModel().clearSelection();
    }

    /**
     * Exibe um alerta na interface gráfica.
     *
     * @param type O tipo de alerta (informação, erro, aviso).
     * @param title O título do alerta.
     * @param message A mensagem do alerta.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Método chamado ao clicar no botão "Menu". Retorna à tela de menu principal.
     *
     * @param event O evento de ação do botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage); // Passa o currentStage ao invés de criar um novo
        loader.loadMenu(); // Método para carregar o menu principal
    }

    public void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage); // Passa o currentStage ao invés de criar um novo
        loader.loadAproveAthleteList(); // Método para carregar o menu principal
    }
}
