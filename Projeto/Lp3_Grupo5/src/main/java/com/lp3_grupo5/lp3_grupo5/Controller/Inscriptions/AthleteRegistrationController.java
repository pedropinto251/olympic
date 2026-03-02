package com.lp3_grupo5.lp3_grupo5.Controller.Inscriptions;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.UserDAO;
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
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador responsável pelo registro de atletas em modalidades.
 * <p>
 * Este controlador permite que um atleta se inscreva em uma modalidade específica,
 * associando o atleta a uma modalidade de desporto disponível na plataforma.
 * O processo inclui a captura do ID e nome do atleta, bem como a escolha da modalidade.
 * </p>
 */
public class AthleteRegistrationController {

    @FXML
    private TextField athleteIdTF;

    @FXML
    private TextField athleteNameTF;

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
    private Button btnRegister;

    @FXML
    private Button btnMenu;

    private final UserDAO userDAO = new UserDAO();
    private final SportDAO sportDAO = new SportDAO();
    private final AthleteRegistrationDAO athleteRegistrationDAO = new AthleteRegistrationDAO();

    private Sport selectedSport;

    /**
     * Método de inicialização da interface do controlador.
     * <p>
     * Este método é chamado automaticamente pelo JavaFX ao inicializar o controlador.
     * Ele configura o campo de texto para o ID do atleta, carrega as modalidades
     * disponíveis na base de dados e as apresenta na TableView.
     * </p>
     */
    @FXML
    public void initialize() {
        athleteIdTF.setText(String.valueOf(Session.getLoggedUserId()));

        String athleteName = userDAO.findAthleteNameByUserId(Session.getLoggedUserId());
        athleteNameTF.setText(athleteName != null ? athleteName : "Nome não encontrado");

        athleteNameTF.setEditable(false);

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

        // Carrega as modalidades existentes na base de dados para a TableView
        loadSports();

        // Adiciona um listener para detectar a seleção de um desporto
        sportTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSport = newValue;
        });
    }

    private void loadSports() {
        List<Sport> sportsList = sportDAO.findAllWithSchedule();
        ObservableList<Sport> observableSports = FXCollections.observableArrayList(sportsList);
        sportTV.setItems(observableSports);
    }
    /**
     * Método chamado quando o botão de registro é pressionado.
     * <p>
     * Este método valida os dados inseridos pelo usuário e registra o atleta
     * na modalidade selecionada. Se a inscrição for bem-sucedida, um alerta
     * de sucesso é exibido. Caso contrário, um alerta de erro será mostrado.
     * </p>
     *
     * @param event O evento de ação do botão.
     */
    @FXML
    void handleBtnRegister(ActionEvent event) {
        try {
            String athleteId = athleteIdTF.getText();
            String athleteName = athleteNameTF.getText();

            // Verifica se todos os campos estão preenchidos
            if (selectedSport != null && !athleteId.isEmpty() && !athleteName.isEmpty()) {
                int athleteIdInt = Integer.parseInt(athleteId);
                int sportIdInt = selectedSport.getId();

                // Verifica se há conflito de horários
                boolean hasTimeConflict = athleteRegistrationDAO.hasTimeConflict(athleteIdInt, sportIdInt);

                if (hasTimeConflict) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Horário coincide com uma modalidade já inscrita.");
                } else {
                    // Valida e registra o atleta
                    boolean isSuccess = athleteRegistrationDAO.registerAthlete(athleteIdInt, sportIdInt);

                    // Exibe um alerta com o resultado da operação
                    if (isSuccess) {
                        showAlert(Alert.AlertType.INFORMATION, "Registro", "Inscrição feita com sucesso!");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Aviso",
                                "Inscrição não permitida: o género do atleta não é compatível com o género do desporto.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos corretamente.");
            }
        } catch (NumberFormatException e) {
            // Trata o caso em que o ID do atleta não é um número válido
            showAlert(Alert.AlertType.ERROR, "Erro", "ID do atleta deve ser um número.");
        } catch (Exception e) {
            // Captura outros erros inesperados e exibe um alerta
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao realizar a inscrição.");
            e.printStackTrace();
        }
    }

    /**
     * Exibe um alerta na interface com o tipo e a mensagem especificados.
     *
     * @param type    O tipo do alerta (informação, erro, aviso, etc.).
     * @param title   O título do alerta.
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
     * Método chamado quando o botão "Menu" é pressionado.
     * <p>
     * Este método carrega a tela de menu inicial do atleta.
     * </p>
     *
     * @param event O evento de ação do botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta();
    }
}