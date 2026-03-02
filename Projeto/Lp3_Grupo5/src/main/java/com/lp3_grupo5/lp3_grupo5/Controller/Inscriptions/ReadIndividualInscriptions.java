package com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.lp3_grupo5.lp3_grupo5.Model.AthleteRegistration;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;


public class ReadIndividualInscriptions {

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
    private TableColumn<AthleteRegistration, String> individualStatusTC;

    @FXML
    private TableColumn<AthleteRegistration, String> individualCountryTC;

    @FXML
    private TableColumn<AthleteRegistration, String> individualGenderTC;

    @FXML
    private TableColumn<AthleteRegistration, Integer> individualIdTC;

    @FXML
    private TableColumn<AthleteRegistration, String> individualInativeTC;

    @FXML
    private TableColumn<AthleteRegistration, String> individualNameTC;

    @FXML
    private TableColumn<AthleteRegistration, Integer> individualSportIdTC;

    @FXML
    private TableColumn<AthleteRegistration, String> individualSportTC;

    @FXML
    private TableView<AthleteRegistration> individualTV;

    @FXML
    private TextField searchField;

    private AthleteRegistrationDAO athleteRegistrationDAO = new AthleteRegistrationDAO();


    @FXML
    private void initialize() {
        // Configuração das colunas da tabela
        individualIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        individualNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        individualCountryTC.setCellValueFactory(new PropertyValueFactory<>("athleteCountry"));
        individualGenderTC.setCellValueFactory(new PropertyValueFactory<>("genre"));
        individualSportIdTC.setCellValueFactory(new PropertyValueFactory<>("sportId"));
        individualSportTC.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        individualStatusTC.setCellValueFactory(new PropertyValueFactory<>("status"));
        individualInativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));

        // Carregar os dados na tabela
        loadIndividualInscriptions();
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            // Se estiver vazio, exibe todos os registros
            loadIndividualInscriptions();
            return;
        }else {

            ObservableList<AthleteRegistration> filteredList = FXCollections.observableArrayList();

            for (AthleteRegistration registration : individualTV.getItems()) {
                boolean matches = false;

                // Verifica os campos: nome, ID e país
                if (registration.getName().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(registration.getId()).contains(searchText)) {
                    matches = true;
                } else if (registration.getAthleteCountry().toLowerCase().contains(searchText)) {
                    matches = true;
                }

                if (matches) {
                    filteredList.add(registration);
                }
            }

            individualTV.setItems(filteredList);
        }
    }



    private void loadIndividualInscriptions() {
        try {
            ObservableList<AthleteRegistration> individualInscriptions = athleteRegistrationDAO.getIndividualInscriptions();
            individualTV.setItems(individualInscriptions);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao carregar dados", "Não foi possível carregar as inscrições individuais dos atletas.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro ao processar a operação");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBtnDelete(ActionEvent event) {
        AthleteRegistration selectedRegistration = individualTV.getSelectionModel().getSelectedItem();

        if (selectedRegistration != null) {
            try {
                boolean success = athleteRegistrationDAO.deactivateRegistration(selectedRegistration.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Inscrição desativada com sucesso!");
                    alert.setContentText("A inscrição foi marcada como inativa.");
                    alert.showAndWait();

                    // Atualiza a tabela
                    individualTV.getItems().remove(selectedRegistration);
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
        AthleteRegistration selectedRegistration = individualTV.getSelectionModel().getSelectedItem();

        if (selectedRegistration != null) {
            try {
                boolean success = athleteRegistrationDAO.activateRegistration(selectedRegistration.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Inscrição ativada com sucesso!");
                    alert.setContentText("A inscrição foi marcada como ativa.");
                    alert.showAndWait();

                    // Atualiza a tabela
                    individualTV.getItems().remove(selectedRegistration);
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
