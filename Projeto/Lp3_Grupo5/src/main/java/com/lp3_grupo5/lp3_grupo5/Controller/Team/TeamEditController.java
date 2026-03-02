package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.CountryDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import com.lp3_grupo5.lp3_grupo5.Model.Team;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * Controlador responsável pela edição de equipas.
 *
 * Este controlador permite ao utilizador selecionar uma equipa existente e atualizar os seus dados,
 * como nome, ano de fundação, país e desporto. Após validação, as alterações são salvas no banco de dados.
 */
public class TeamEditController {

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;


    @FXML
    private ChoiceBox<Country> countryCB;

    @FXML
    private TextField foundationYearTF;

    @FXML
    private ChoiceBox<Team> nameCB;

    @FXML
    private ChoiceBox<Sport> sportCB;

    private TeamDAO teamDAO = new TeamDAO();

    private SportDAO sportDAO = new SportDAO();

    private CountryDAO countryDAO = new CountryDAO();

    /**
     * Método de inicialização do controlador.
     * Este método carrega as listas de equipas, desportos e países a partir do banco de dados
     * e preenche os respectivos ChoiceBoxes.
     */
    @FXML
    public void initialize() {

        // Carrega a lista de equipas
        ObservableList<Team> teamList = FXCollections.observableArrayList(teamDAO.findAll());
        nameCB.setItems(teamList);

        // Carrega a lista de desportos
        ObservableList<Sport> sportsList = FXCollections.observableArrayList(sportDAO.findAllColletive());
        sportCB.setItems(sportsList);

        // Carrega a lista de países
        ObservableList<Country> countryList = FXCollections.observableArrayList(countryDAO.getAllCountries());
        countryCB.setItems(countryList);

        // Adiciona listener ao nameCB para carregar informações da equipa selecionada
        nameCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarInformacoesDoTime((Team) newValue);
            }
        });

        // Validação do campo foundationYearTF para aceitar apenas números inteiros
        foundationYearTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Permite apenas dígitos
                foundationYearTF.setText(newValue.replaceAll("[^\\d]", "")); // Remove caracteres inválidos
            }
        });
    }

    private void carregarInformacoesDoTime(Team team) {
        // Preenche o ano de fundação
        foundationYearTF.setText(String.valueOf(team.getFoundationYear()));

        // Define o país no ChoiceBox
        countryCB.getSelectionModel().select(
                countryCB.getItems().stream()
                        .filter(country -> country.getCode().equals(team.getCountry()))
                        .findFirst()
                        .orElse(null)
        );

        // Define o esporte no ChoiceBox
        sportCB.getSelectionModel().select(
                sportCB.getItems().stream()
                        .filter(sport -> sport.getId() == team.getSportsId())
                        .findFirst()
                        .orElse(null)
        );
    }


    /**
     * Método chamado ao clicar no botão "Editar". Valida as alterações e, se forem válidas, atualiza a equipa no banco de dados.
     *
     * @param event O evento de ação do botão.
     */
    @FXML
    void handleBtnEdit(ActionEvent event) {
        Team selectedTeam = (Team) nameCB.getSelectionModel().getSelectedItem();

        if (selectedTeam != null) {

            String foundationYearText = foundationYearTF.getText();
            int foundationYear;

            try {
                foundationYear = Integer.parseInt(foundationYearText);

                // Verifica se o ano de fundação é menor que o ano atual
                int currentYear = java.time.Year.now().getValue();
                if (foundationYear < currentYear) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O ano de fundação só pode se mudado para ano atual ou anos futuros.");
                    return; // Impede a continuação se o ano for inválido
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ano de fundação inválido.");
                return;
            }

            // Atualiza os dados da equipa com os novos valores
            selectedTeam.setCountry(countryCB.getValue().getCode()); // Assume que countryCB fornece os códigos dos países
            selectedTeam.setSport(sportCB.getValue().getName());
            selectedTeam.setSportsId(sportCB.getValue().getId());
            selectedTeam.setFoundationYear(Integer.parseInt(foundationYearTF.getText()));

            // Tenta atualizar a equipa no banco de dados
            try {
                boolean updated = teamDAO.updateTeam(selectedTeam);
                if (updated) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Equipa editada com sucesso!");
                    clearAll();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao editar a equipa.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao acessar a base de dados: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Por favor, selecione uma equipa para editar.");
        }
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
     * Método que limpa os campos.
     * Este método será chamado após uma edição bem-sucedida para resetar os campos.
     */
    private void clearAll() {
        // Limpar o campo de ano de fundação
        foundationYearTF.clear();

        // Limpar as seleções dos ChoiceBoxes
        countryCB.getSelectionModel().clearSelection();
        sportCB.getSelectionModel().clearSelection();
        nameCB.getSelectionModel().clearSelection();
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
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage); // Passa o currentStage ao invés de criar um novo
        loader.loadTeamMenu(); // Método para carregar o menu principal
    }
}
