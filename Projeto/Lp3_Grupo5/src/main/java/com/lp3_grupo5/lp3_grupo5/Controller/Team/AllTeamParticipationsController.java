package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Model.TeamOlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class AllTeamParticipationsController {
    @FXML
    private TableView<TeamOlympicParticipation> TeamParticipationTable;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<TeamOlympicParticipation, Integer> eventColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, Integer> idColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, String> inativeColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, Integer> teamColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, Integer> yearColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, String> resultColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, String> eventCountryColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, String> teamSportColumn;

    @FXML
    private TableColumn<TeamOlympicParticipation, String> athleteNameColumn;

    @FXML
    private ComboBox<String> sportComboBox;

    @FXML
    private TextField athleteNameTextField;

    @FXML
    private ComboBox<Integer> eventComboBox;

    private AthleteDAO teamParticipationService = new AthleteDAO();
    private ObservableList<TeamOlympicParticipation> allParticipations;
    private SportDAO sportService = new SportDAO();

    @FXML
    public void initialize() {
        // Configurar as colunas da tabela
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        inativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInactive() ? "Desativado" : "Ativo"));
        eventCountryColumn.setCellValueFactory(new PropertyValueFactory<>("event_country"));
        teamSportColumn.setCellValueFactory(new PropertyValueFactory<>("team_sport"));
        athleteNameColumn.setCellValueFactory(new PropertyValueFactory<>("athleteName"));

        // Obter o ID do atleta logado a partir da sessão
        int athleteId = Session.getLoggedUserId();

        // Buscar todas as participações olímpicas
        List<TeamOlympicParticipation> participations = teamParticipationService.getAllTeamParticipations();
        allParticipations = FXCollections.observableArrayList(participations);

        // Preencher a tabela com as participações
        TeamParticipationTable.getItems().setAll(allParticipations);

        // Configurar o ComboBox com os desportos coletivos disponíveis
        List<Sport> sports = sportService.findAllColletive();
        ObservableList<String> sportNames = FXCollections.observableArrayList();
        for (Sport sport : sports) {
            sportNames.add(sport.getName());
        }
        sportComboBox.setItems(sportNames);

        // Configurar o ComboBox com os eventos disponíveis
        ObservableList<Integer> eventIds = FXCollections.observableArrayList();
        for (TeamOlympicParticipation participation : allParticipations) {
            if (!eventIds.contains(participation.getEventId())) {
                eventIds.add(participation.getEventId());
            }
        }
        eventComboBox.setItems(eventIds);
    }

    @FXML
    public void filterBySport() {
        applyFilters();
    }

    @FXML
    public void filterByAthleteName() {
        applyFilters();
    }

    @FXML
    public void filterByEvent() {
        applyFilters();
    }

    private void applyFilters() {
        String selectedSport = sportComboBox.getValue();
        String athleteName = athleteNameTextField.getText();
        Integer selectedEventId = eventComboBox.getValue();

        ObservableList<TeamOlympicParticipation> filteredData = FXCollections.observableArrayList(allParticipations);

        if (selectedSport != null && !selectedSport.trim().isEmpty()) {
            filteredData = filteredData.filtered(participation -> participation.getTeam_sport().equals(selectedSport));
        }

        if (athleteName != null && !athleteName.trim().isEmpty()) {
            filteredData = filteredData.filtered(participation -> participation.getAthleteName().toLowerCase().contains(athleteName.toLowerCase()));
        }

        if (selectedEventId != null) {
            filteredData = filteredData.filtered(participation -> participation.getEventId() == selectedEventId);
        }

        TeamParticipationTable.setItems(filteredData);
    }

    public void handleBtnMenu(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    public void handleBtnBack(javafx.event.ActionEvent actionEvent){
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamMenu();
    }
}