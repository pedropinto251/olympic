package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.TeamApplicationDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Model.TeamApplication;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador responsável pela gestão das inscrições de atletas nas equipas.
 */
public class TeamApplicationController {

    @FXML
    private TextField txtAthleteId;

    @FXML
    private TableView<Sport> sportTV;

    @FXML
    private TableColumn<Sport, Integer> sportIdTC;

    @FXML
    private TableColumn<Sport, String> sportNameTC;

    @FXML
    private TableColumn<Sport, String> startTimeTC;

    @FXML
    private TableColumn<Sport, String> endTimeTC;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnRegister;

    private TeamApplicationDAO teamApplicationDAO = new TeamApplicationDAO();
    private SportDAO sportDAO = new SportDAO();

    private Sport selectedSport;

    /**
     * Método de inicialização do controlador.
     * Define o ID do atleta logado e carrega os desportos disponíveis na TableView.
     */
    @FXML
    public void initialize() {
        txtAthleteId.setText(String.valueOf(Session.getLoggedUserId()));

        // Configura as colunas da TableView
        sportIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        startTimeTC.setCellValueFactory(cellData -> {
            LocalDateTime startTime = cellData.getValue().getStartTime();
            return new SimpleStringProperty(startTime != null ? startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
        });
        endTimeTC.setCellValueFactory(cellData -> {
            LocalDateTime endTime = cellData.getValue().getEndTime();
            return new SimpleStringProperty(endTime != null ? endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
        });

        // Carrega os desportos existentes na base de dados para a TableView
        loadSports();

        // Adiciona um listener para detectar a seleção de um desporto
        sportTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSport = newValue;
        });

        try (Connection connection = DBConnection.getConnection()) {
            // Carrega as equipas no ChoiceBox

        } catch (SQLException e) {
            showErrorAlert("Erro de Conexão", "Não foi possível conectar ao banco de dados. ");
            System.out.println(e);
        }

    }

    private void loadSports() {
        List<Sport> sportsList = sportDAO.findAllWithScheduleColective();
        ObservableList<Sport> observableSports = FXCollections.observableArrayList(sportsList);
        sportTV.setItems(observableSports);
    }

    @FXML
    public void handleApplication() {
        try (Connection connection = DBConnection.getConnection()) {
            int athleteId = Session.getLoggedUserId();

            if (selectedSport == null) {
                showErrorAlert("Erro", "Por favor, selecione um desporto.");
                return;
            }

            int sportId = selectedSport.getId();
            System.out.println("ID do Desporto Selecionado: " + sportId);

            TeamApplication application = new TeamApplication();
            application.setAthleteId(athleteId);
            application.setStatus("pending");
            application.setSport_id(sportId);

            boolean success = teamApplicationDAO.createApplication(application, connection);

            if (success) {
                showSuccessAlert("Sucesso", "Inscrição criada com sucesso!");
            } else {
                showErrorAlert("Erro", "Não foi possível criar a inscrição.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Erro de Formato", "Os campos de ID devem conter números.");
        } catch (SQLException e) {
            showErrorAlert("Erro no Banco de Dados", "Houve um problema ao acessar o banco de dados.");
            System.out.println(e);
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) btnMenu.getScene().getWindow();
    }

    public void handleBtnMenu(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta();
    }
}