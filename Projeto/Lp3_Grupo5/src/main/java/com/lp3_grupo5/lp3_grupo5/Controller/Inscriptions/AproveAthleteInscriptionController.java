package com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.EventDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.AthleteRegistration;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador responsável pela interface de aprovação e rejeição de inscrições de atletas.
 */
public class AproveAthleteInscriptionController {

    @FXML
    private TableColumn<AthleteRegistration, Integer> InscricaoIdTC;
    @FXML
    private TableColumn<AthleteRegistration, Integer> AtletaTC;
    @FXML
    private TableColumn<AthleteRegistration, String> EstadoTC;
    @FXML
    private TableColumn<AthleteRegistration, String> sportNameTC;
    @FXML
    private TableColumn<AthleteRegistration, String> countryTC;
    @FXML
    private TableColumn<AthleteRegistration, String> applicationDateTC;
    @FXML
    private TableColumn<AthleteRegistration, String> athleteCountryTC;
    @FXML
    private TableColumn<AthleteRegistration, String> athleteGenderTC;
    @FXML
    private TableView<AthleteRegistration> IncriptionTV;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnAprove;
    @FXML
    private Button btnReject;
    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    private AthleteRegistrationDAO athleteRegistrationDAO = new AthleteRegistrationDAO();
    private SportDAO sportDAO = new SportDAO();
    private EventDAO eventDAO = new EventDAO();
    private TeamDAO teamDAO = new TeamDAO();


    @FXML
    private void initialize() {
        // Configuração das colunas da tabela
        InscricaoIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        AtletaTC.setCellValueFactory(new PropertyValueFactory<>("athleteId"));
        EstadoTC.setCellValueFactory(new PropertyValueFactory<>("status"));
        sportNameTC.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        applicationDateTC.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        athleteCountryTC.setCellValueFactory(new PropertyValueFactory<>("athleteCountry"));
        athleteGenderTC.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Carrega os dados na tabela
        loadTableData();
    }

    /**
     * Carrega os dados de inscrições de atletas da base de dados e os exibe na tabela.
     */
    private void loadTableData() {
        try {
            ObservableList<AthleteRegistration> registrations = athleteRegistrationDAO.getAllRegistrations();
            IncriptionTV.setItems(registrations);
        } catch (Exception e) {
            showAlert("Erro ao carregar dados", "Não foi possível carregar as inscrições dos atletas.");
            e.printStackTrace();
        }
    }

    /**
     * Manipula o evento de aprovação de uma inscrição de atleta.
     * Ao clicar no botão "Aprovar", busca automaticamente uma equipa compatível
     * e aprova a inscrição sem precisar de seleção manual.
     */
    @FXML
    void handleBtnAprove(ActionEvent event) {
        AthleteRegistration selectedRegistration = IncriptionTV.getSelectionModel().getSelectedItem();

        if (selectedRegistration != null) {
            int athleteSportId = selectedRegistration.getSportId();
            String athleteCountry = selectedRegistration.getAthleteCountry();
            String athleteGender = selectedRegistration.getGenre();

            // Verificar se já existe uma equipe ativa para este esporte, país e gênero
            Integer teamId = athleteRegistrationDAO.findMatchingTeam(athleteSportId, athleteCountry, athleteGender);

            // Se não houver equipe, criar uma nova
            if (teamId == null) {
                int currentYear = eventDAO.getActiveEventYear(); // Obtém o ano do evento ativo
                String sportName = sportDAO.getSportName(athleteSportId); // Obtém o nome do esporte
                String teamName = sportName + " " + athleteGender + " " + athleteCountry + " " + currentYear;

                teamId = teamDAO.createTeam(athleteSportId, athleteCountry, sportName, athleteGender, teamName);
                System.out.println(athleteGender);
                if (teamId == null) {
                    showAlert("Erro", "Não foi possível criar uma nova equipe.");
                    return;
                }
            }

            // Aprovar a inscrição e associar o atleta à equipe
            if (athleteRegistrationDAO.aproveAthlete(selectedRegistration.getId(), teamId)) {
                selectedRegistration.setStatus("approved");
                IncriptionTV.refresh();
                showAlert("Sucesso", "Inscrição aprovada com sucesso!");

                // Verificar se o mínimo de atletas foi atingido
                int approvedAthletes = athleteRegistrationDAO.countApprovedAthletes(athleteSportId, athleteCountry, athleteGender);
                int minAthletesRequired = athleteRegistrationDAO.getMinAthletesRequired(athleteSportId);

                if (approvedAthletes >= minAthletesRequired) {
                    // Desativar inscrições pendentes
                    athleteRegistrationDAO.deactivatePendingInscriptions(athleteSportId, athleteCountry, athleteGender);
                    showAlert("Aviso", "O número mínimo de atletas foi atingido. Inscrições pendentes foram desativadas.");
                }
            } else {
                showAlert("Erro", "Erro ao aprovar a inscrição.");
            }
        } else {
            showAlert("Atenção", "Por favor, selecione uma inscrição antes de tentar aprová-la.");
        }
    }


    /**
     * Manipula o evento de rejeição de uma inscrição de atleta.
     */
    @FXML
    void handleBtnReject(ActionEvent event) {
        AthleteRegistration selectedRegistration = IncriptionTV.getSelectionModel().getSelectedItem();

        if (selectedRegistration != null) {
            if (athleteRegistrationDAO.rejectAthlete(selectedRegistration.getId())) {
                selectedRegistration.setStatus("rejected");
                IncriptionTV.refresh();

                showAlert("Sucesso", "Inscrição rejeitada com sucesso!");
            } else {
                showAlert("Erro", "Erro ao rejeitar a inscrição.");
            }
        } else {
            showAlert("Atenção", "Por favor, selecione uma inscrição antes de tentar rejeitá-la.");
        }
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Exibe um alerta com uma mensagem informativa para o usuário.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleBtnBack(ActionEvent event) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGestaoMenu();
    }
}