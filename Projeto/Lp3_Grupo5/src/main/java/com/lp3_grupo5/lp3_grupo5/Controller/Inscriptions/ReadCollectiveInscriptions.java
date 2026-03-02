package com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions;

import com.lp3_grupo5.lp3_grupo5.DAO.TeamApplicationDAO;
import com.lp3_grupo5.lp3_grupo5.Model.TeamApplication;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ReadCollectiveInscriptions {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnActivate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<TeamApplication, String> collectiveStatusTC;

    @FXML
    private TableColumn<TeamApplication, String> collectiveCountryTC;

    @FXML
    private TableColumn<TeamApplication, String> collectiveGenderTC;

    @FXML
    private TableColumn<TeamApplication, Integer> collectiveIdTC;

    @FXML
    private TableColumn<TeamApplication, String> collectiveInativeTC;

    @FXML
    private TableColumn<TeamApplication, String> collectiveNameTC;

    @FXML
    private TableColumn<TeamApplication, String> collectiveSportTC;
    @FXML
    private TableColumn<TeamApplication, Integer> collectiveSportIdTC;
    @FXML
    private TableColumn<TeamApplication, Integer> collectiveTeamIdTC;

    @FXML
    private TableView<TeamApplication> collectiveTV;

    @FXML
    private TextField searchField;

    private final TeamApplicationDAO teamApplicationDAO = new TeamApplicationDAO();

    @FXML
    public void initialize() {
        // Configuração das colunas
        collectiveIdTC.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        collectiveNameTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        collectiveCountryTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry()));
        collectiveGenderTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));
        collectiveSportTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSportName()));
        collectiveTeamIdTC.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTeamId()));
        collectiveStatusTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        collectiveInativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));
        collectiveSportIdTC.setCellValueFactory( data -> new SimpleObjectProperty<>(data.getValue().getSport_id()));
        // Carrega os dados na tabela
        loadCollectiveInscriptions();
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            loadCollectiveInscriptions();
        } else {
            ObservableList<TeamApplication> filteredList = FXCollections.observableArrayList();

            for (TeamApplication application : collectiveTV.getItems()) {
                boolean matches = false;

                if (application.getName().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (application.getCountry().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (application.getGenre().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(application.getSport_id()).contains(searchText)) {
                    matches = true;
                } else if (application.getSportName().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(application.getTeamId()).contains(searchText)) {
                    matches = true;
                }

                if (matches) {
                    filteredList.add(application);
                }
            }

            collectiveTV.setItems(filteredList);
        }
    }


    private void loadCollectiveInscriptions() {
        try {
            ObservableList<TeamApplication> inscriptions = teamApplicationDAO.getCollectiveInscriptions();
            collectiveTV.setItems(inscriptions);
        } catch (SQLException e) {
            showErrorAlert("Erro ao carregar inscrições coletivas.");
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBtnDelete(ActionEvent event) {
        TeamApplication selectedApplication = collectiveTV.getSelectionModel().getSelectedItem();

        if (selectedApplication != null) {
            try {
                // Vamos chamar o método que vai "desativar" a inscrição da equipe
                boolean success = teamApplicationDAO.deactivateApplication(selectedApplication.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Inscrição desativada com sucesso!");
                    alert.setContentText("A inscrição foi marcada como inativa.");
                    alert.showAndWait();

                    // Atualiza a tabela removendo a inscrição desativada
                    collectiveTV.getItems().remove(selectedApplication);
                } else {
                    showErrorAlert("Erro ao desativar a inscrição.");
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao tentar desativar a inscrição.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Nenhuma inscrição selecionada.");
        }
    }

    @FXML
    void handleBtnActivate(ActionEvent event) {
        TeamApplication selectedApplication = collectiveTV.getSelectionModel().getSelectedItem();

        if (selectedApplication != null) {
            try {
                // Vamos chamar o método que vai "ativar" a inscrição da equipe
                boolean success = teamApplicationDAO.activateApplication(selectedApplication.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Inscrição ativada com sucesso!");
                    alert.setContentText("A inscrição foi marcada como ativa.");
                    alert.showAndWait();

                    // Atualiza a tabela removendo a inscrição ativada
                    collectiveTV.getItems().remove(selectedApplication);
                } else {
                    showErrorAlert("Erro ao ativar a inscrição.");
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao tentar ativar a inscrição.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Nenhuma inscrição selecionada.");
        }
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    @FXML
    void handleBtnBack(ActionEvent event) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGestaoMenu();
    }
}