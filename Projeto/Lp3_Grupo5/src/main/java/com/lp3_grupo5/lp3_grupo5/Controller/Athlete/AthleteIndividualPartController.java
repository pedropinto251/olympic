package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.OlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class AthleteIndividualPartController {

    @FXML
    private TableView<OlympicParticipation> TeamParticipationTable;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<OlympicParticipation, String> athleteNameColumn;

    @FXML
    private TextField athleteNameTextField;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<OlympicParticipation, String> eventColumn;

    @FXML
    private TableColumn<OlympicParticipation, String> eventCountryColumn;

    @FXML
    private TableColumn<OlympicParticipation, Integer> idColumn;

    @FXML
    private TableColumn<OlympicParticipation, String> inativeColumn;

    @FXML
    private TableColumn<OlympicParticipation, String> resultColumn;

    @FXML
    private ComboBox<String> sportComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private ComboBox<Integer> eventComboBox;

    @FXML
    private TableColumn<OlympicParticipation, String> teamSportColumn;

    @FXML
    private TableColumn<OlympicParticipation, Integer> yearColumn;

    private AthleteDAO athleteDAO = new AthleteDAO();
    private SportDAO sportService = new SportDAO();
    private ObservableList<OlympicParticipation> allParticipations;

    /**
     * Inicializa o controlador, configura as colunas da tabela e carrega os dados.
     */
    @FXML
    public void initialize() {
        // Configurar as colunas da tabela
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("athleteId"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        inativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInactive() ? "Desativado" : "Ativo"));
        athleteNameColumn.setCellValueFactory(new PropertyValueFactory<>("athleteName"));
        eventCountryColumn.setCellValueFactory(new PropertyValueFactory<>("eventCountry"));
        teamSportColumn.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        // Obter o ID do atleta logado a partir da sessão
        int athleteId = Session.getLoggedUserId();

        // Buscar todas as participações olímpicas individuais
        List<OlympicParticipation> participations = athleteDAO.getAllOlympicParticipations();
        allParticipations = FXCollections.observableArrayList(participations);

        // Preencher a tabela com os dados
        TeamParticipationTable.setItems(allParticipations);

        // Configurar o ComboBox com os desportos disponíveis
        List<Sport> sports = sportService.findAll();
        ObservableList<String> sportNames = FXCollections.observableArrayList();
        for (Sport sport : sports) {
            sportNames.add(sport.getName());
        }
        sportComboBox.setItems(sportNames);

        // Configurar o ComboBox com os anos disponíveis
        ObservableList<String> years = FXCollections.observableArrayList();
        for (OlympicParticipation participation : allParticipations) {
            if (!years.contains(String.valueOf(participation.getYear()))) {
                years.add(String.valueOf(participation.getYear()));
            }
        }
        yearComboBox.setItems(years);

        // Configurar o ComboBox com os eventos disponíveis
        ObservableList<Integer> eventIds = FXCollections.observableArrayList();
        for (OlympicParticipation participation : allParticipations) {
            if (!eventIds.contains(participation.getEventId())) {
                eventIds.add(participation.getEventId());
            }
        }
        eventComboBox.setItems(eventIds);

    }

    @FXML
    public void handleBtnMenu(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    @FXML
    public void filterByAthleteName() {
        String athleteName = athleteNameTextField.getText();

        if (athleteName != null && !athleteName.trim().isEmpty()) {
            // Filtrar as participações pelo nome do atleta
            List<OlympicParticipation> filteredParticipations = athleteDAO.getIndividualParticipationsByAthleteName(athleteName);
            TeamParticipationTable.getItems().setAll(filteredParticipations);
        } else {
            List<OlympicParticipation> participations = athleteDAO.getAllOlympicParticipations();
            allParticipations = FXCollections.observableArrayList(participations);

            // Preencher a tabela com os dados
            TeamParticipationTable.setItems(allParticipations);
        }
    }

    @FXML
    public void filterByYear(javafx.event.ActionEvent actionEvent) {
        applyFilters();
    }

    @FXML
    public void filterBySport(javafx.event.ActionEvent actionEvent) {
        applyFilters();
    }

    private void applyFilters() {
        String selectedSport = sportComboBox.getValue();
        String selectedYear = yearComboBox.getValue();
        Integer selectedEventId = eventComboBox.getValue();

        ObservableList<OlympicParticipation> filteredData = FXCollections.observableArrayList(allParticipations);

        if (selectedSport != null && !selectedSport.trim().isEmpty()) {
            filteredData = filteredData.filtered(participation -> participation.getSportName().equals(selectedSport));
        }

        if (selectedYear != null && !selectedYear.trim().isEmpty()) {
            int year = Integer.parseInt(selectedYear);
            filteredData = filteredData.filtered(participation -> participation.getYear() == year);
        }

        if (selectedEventId != null) {
            filteredData = filteredData.filtered(participation -> participation.getEventId() == selectedEventId);
        }

        TeamParticipationTable.setItems(filteredData);
    }

    @FXML
    public void filterByEvent(javafx.event.ActionEvent actionEvent) {
        applyFilters();
    }

    @FXML
    public void handleBtnBack(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLAthleteMenu();
    }
}
