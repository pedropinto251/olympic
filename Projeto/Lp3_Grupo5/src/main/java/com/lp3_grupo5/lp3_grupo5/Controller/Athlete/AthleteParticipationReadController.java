package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.OlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para exibição e manipulação das participações olímpicas de um atleta.
 * Gerencia a visualização, atualização e inativação de participações olímpicas por meio de uma interface gráfica (FXML).
 */
public class AthleteParticipationReadController {

    // FXML elements
    @FXML
    private TableView<OlympicParticipation> participationTable; // Tabela para exibir participações olímpicas

    @FXML
    private TableColumn<OlympicParticipation, Integer> yearColumn; // Coluna para o ano da participação

    @FXML
    private TableColumn<OlympicParticipation, String> resultColumn; // Coluna para o resultado geral

    @FXML
    private TableColumn<OlympicParticipation, Integer> goldColumn; // Coluna para medalhas de ouro

    @FXML
    private TableColumn<OlympicParticipation, Integer> silverColumn; // Coluna para medalhas de prata

    @FXML
    private TableColumn<OlympicParticipation, Integer> bronzeColumn; // Coluna para medalhas de bronze

    @FXML
    private TableColumn<OlympicParticipation, Integer> diplomaColumn; // Coluna para diplomas

    @FXML
    private TableColumn<OlympicParticipation, Boolean> inativeColumn; // Coluna para status de inatividade

    @FXML
    private TableColumn<OlympicParticipation, String> sportColumn; // Nova coluna para Desporto

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal

    @FXML
    private ComboBox<String> sportComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    private AthleteDAO athleteParticipationDAO = new AthleteDAO(); // DAO para acessar participações no banco de dados

    private ObservableList<OlympicParticipation> allParticipations;

    /**
     * Inicializa o controlador, configurando a tabela e carregando as participações do atleta logado.
     */
    @FXML
    public void initialize() {
        // Configurar colunas da tabela com os campos do modelo OlympicParticipation
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("gold"));
        silverColumn.setCellValueFactory(new PropertyValueFactory<>("silver"));
        bronzeColumn.setCellValueFactory(new PropertyValueFactory<>("bronze"));
        diplomaColumn.setCellValueFactory(new PropertyValueFactory<>("diploma"));
        inativeColumn.setCellValueFactory(new PropertyValueFactory<>("inactive"));
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sportName"));

        // Obter o ID do atleta logado da sessão
        int athleteId = Session.getLoggedUserId();

        // Buscar as participações do atleta logado usando o DAO
        List<OlympicParticipation> participations = athleteParticipationDAO.getParticipationsByAthleteId(athleteId);

        // Inicializar allParticipations com as participações obtidas
        allParticipations = FXCollections.observableArrayList(participations);

        // Adicionar as participações à tabela
        participationTable.getItems().setAll(allParticipations);

        // Configurar o ComboBox com os desportos disponíveis para o atleta logado
        List<String> sports = athleteParticipationDAO.getSportsByAthleteId(athleteId);
        ObservableList<String> sportNames = FXCollections.observableArrayList(sports);
        sportComboBox.setItems(sportNames);

        // Configurar o ComboBox com os anos disponíveis para o atleta logado
        List<String> years = athleteParticipationDAO.getYearsByAthleteId(athleteId);
        ObservableList<String> yearList = FXCollections.observableArrayList(years);
        yearComboBox.setItems(yearList);
    }

    /**
     * Manipula o evento de clique para inativar uma participação selecionada.
     *
     * @param event evento de clique do mouse na tabela.
     */
    @FXML
    public void handleBtnDelete(MouseEvent event) {
        // Obter a participação selecionada na tabela
        OlympicParticipation selectedParticipation = participationTable.getSelectionModel().getSelectedItem();
        if (selectedParticipation != null) {
            // Alterar o estado da participação para inativa
            selectedParticipation.setInactive(true);
            athleteParticipationDAO.updateParticipationStatus(selectedParticipation);
            // Atualizar a tabela para refletir as alterações
            participationTable.refresh();
        }
    }

    /**
     * Manipula o evento do botão para retornar ao menu principal.
     *
     * @param actionEvent evento de clique do botão.
     */
    public void handleBtnMenu(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta(); // Carrega a visão do menu inicial do atleta
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
        int athleteId = Session.getLoggedUserId();

        List<OlympicParticipation> filteredParticipations = allParticipations;

        if (selectedSport != null && !selectedSport.trim().isEmpty()) {
            filteredParticipations = athleteParticipationDAO.getOlympicParticipationsByAthleteAndSport(athleteId, selectedSport);
        }

        if (selectedYear != null && !selectedYear.trim().isEmpty()) {
            int year = Integer.parseInt(selectedYear);
            filteredParticipations = athleteParticipationDAO.getOlympicParticipationsByAthleteYearAndSport(athleteId, year, selectedSport);
        }

        participationTable.getItems().setAll(filteredParticipations);
    }
}
