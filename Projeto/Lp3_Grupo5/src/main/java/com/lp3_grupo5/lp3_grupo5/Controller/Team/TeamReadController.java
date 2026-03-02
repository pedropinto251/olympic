package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Model.Team;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;



import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador responsável pela leitura e manipulação das equipas.
 * <p>
 * Este controlador permite a visualização de equipas numa tabela. Também possibilita a exclusão
 * de uma equipa da base de dados, atualizando a tabela após a operação, e a navegação para o menu principal.
 * </p>
 */
public class TeamReadController {

    @FXML
    private TableView<Team> TeamTV;

    @FXML
    private TableView<Athlete> TeamAthleteTV;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<Team, String> countryTC;

    @FXML
    private TableColumn<Team, Integer> foundationyearTC;

    @FXML
    private TableColumn<Team, String> genderTC;

    @FXML
    private TableColumn<Team, Integer> idTC;

    @FXML
    private TableColumn<Team, String> inativeTC;

    @FXML
    private TableColumn<Team, String> nameTC;

    @FXML
    private TableColumn<Team, String> sportTC;

    @FXML
    private TableColumn<Team, Integer> sportidTC;

    @FXML
    private TableColumn<Athlete, Integer> idAthleteTC;

    @FXML
    private TableColumn<Athlete, String> nameAthleteTC;

    @FXML
    private TextField searchField;

    private TeamDAO teamDAO = new TeamDAO();

    /**
     * Método de inicialização do controlador. Configura as colunas da tabela e carrega os dados
     * das equipas da base de dados.
     */
    @FXML
    private void initialize() {
        // Configuração das colunas da tabela de equipas
        idTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        sportTC.setCellValueFactory(new PropertyValueFactory<>("sport"));
        genderTC.setCellValueFactory(new PropertyValueFactory<>("genre"));
        countryTC.setCellValueFactory(new PropertyValueFactory<>("country"));
        foundationyearTC.setCellValueFactory(new PropertyValueFactory<>("foundationYear"));
        sportidTC.setCellValueFactory(new PropertyValueFactory<>("sportsId"));
        inativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));

        // Configuração das colunas da tabela de atletas
        idAthleteTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameAthleteTC.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Carregar os dados da tabela usando a DAO
        loadDataFromDatabase();

        // Adicionar listener para a seleção da equipe
        TeamTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleTeamSelection());
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            loadDataFromDatabase();
        } else {
            ObservableList<Team> filteredList = FXCollections.observableArrayList();

            for (Team team : TeamTV.getItems()) {
                boolean matches = false;

                // Verifica os campos: nome, país, gênero, ano de fundação, etc.
                if (team.getName().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (team.getCountry().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (team.getGenre().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(team.getId()).contains(searchText)) {
                    matches = true;
                }

                if (matches) {
                    filteredList.add(team);
                }
            }

            TeamTV.setItems(filteredList);
        }
    }




    /**
     * Carrega os dados das equipas a partir da base de dados e os exibe na tabela.
     */
    private void loadDataFromDatabase() {
        List<Team> teams = teamDAO.findAll(); // Obtém a lista de equipas
        ObservableList<Team> observableTeams = FXCollections.observableArrayList(teams);
        TeamTV.setItems(observableTeams); // Atualiza a tabela com as equipas
    }

    /**
     * Método de tratamento da seleção de uma equipe na tabela.
     * Recupera os atletas da equipe selecionada e os exibe na tabela de atletas.
     */
    private void handleTeamSelection() {
        // Obtém a equipe selecionada na tabela
        Team selectedTeam = TeamTV.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            // Obtém o ID da equipe selecionada
            int teamId = selectedTeam.getId();

            // Recupera os atletas dessa equipe usando o DAO
            List<Athlete> athletes = teamDAO.getAthletesByTeamId(teamId);

            // Converte a lista de atletas para ObservableList
            ObservableList<Athlete> observableAthletes = FXCollections.observableArrayList(athletes);

            // Preenche a tabela de atletas com a lista de atletas
            TeamAthleteTV.setItems(observableAthletes);
        }
    }

    /**
     * Método de tratamento do evento de clique no botão de exclusão de equipa.
     * Exclui a equipa selecionada da base de dados e atualiza a tabela.
     *
     * @param event O evento gerado ao clicar no botão de exclusão.
     */
    @FXML
    void handleBtnDelete(ActionEvent event) {
        Team selectedTeam = TeamTV.getSelectionModel().getSelectedItem(); // Obtém a equipa selecionada

        if (selectedTeam != null) {
            try {
                boolean success = teamDAO.deleteTeam(selectedTeam.getId()); // Tenta excluir a equipa
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Equipa excluída com sucesso!");
                    alert.setContentText("A equipa foi marcada como excluída.");
                    alert.showAndWait(); // Exibe um alerta de sucesso

                    // Atualiza a tabela após a exclusão
                    TeamTV.getItems().remove(selectedTeam);
                } else {
                    showErrorAlert("Erro ao excluir a equipa."); // Exibe um alerta de erro se a exclusão falhar
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao tentar excluir a equipa.");
                e.printStackTrace(); // Exibe a stack trace em caso de erro de SQL
            }
        } else {
            showErrorAlert("Nenhuma equipa selecionada."); // Exibe um alerta se nenhuma equipa for selecionada
        }
    }

    /**
     * Exibe um alerta de erro com uma mensagem personalizada.
     *
     * @param message A mensagem de erro a ser exibida no alerta.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro ao processar a operação");
        alert.setContentText(message);
        alert.showAndWait(); // Exibe o alerta de erro
    }

    /**
     * Método de tratamento do evento de clique no botão de menu.
     * Fecha a janela atual e carrega o menu principal.
     *
     * @param event O evento gerado ao clicar no botão de menu.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow(); // Obtém a janela atual
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu(); // Carrega o menu principal
    }

    @FXML
    void handleBtnBack(ActionEvent event){
        Stage currentStage = (Stage) btnBack.getScene().getWindow(); // Obtém a janela atual
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamMenu(); // Carrega o menu principal
    }
}
