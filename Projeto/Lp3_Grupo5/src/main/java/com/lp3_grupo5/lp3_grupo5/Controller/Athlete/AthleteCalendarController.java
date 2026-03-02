package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Calendar;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class AthleteCalendarController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnMenu; // Botão de menu

    @FXML
    private TableView<Calendar> calendarAthleteTableView; // Tabela de atletas

    @FXML
    private TableColumn<Calendar, String> sportColumn; // Coluna para a modalidade

    @FXML
    private TableColumn<Calendar, String> dateColumn; // Coluna para a data da modalidade

    @FXML
    private TableColumn<Calendar, String> localColumn; // Coluna para a localização

    @FXML
    private TableColumn<Calendar, String> startColumn; // Coluna para a data de início

    @FXML
    private TableColumn<Calendar, String> endColumn; // Coluna para a data de término

    @FXML
    private TableColumn<Calendar, Integer> yearColumn; // Nova coluna para o ano do evento

    @FXML
    private TableColumn<Calendar, String> sportFullNameColumn; // Nova coluna para o nome completo do desporto

    // Lista de atletas
    private ObservableList<Calendar> athleteData = FXCollections.observableArrayList();

    /**
     * Método de inicialização do controlador.
     * Este método é chamado automaticamente após a criação dos elementos FXML.
     */
    @FXML
    public void initialize() throws SQLException {
        // Configuração das colunas da tabela
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year")); // Configuração da nova coluna para o ano
        sportFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("sportFullName")); // Configuração da nova coluna para o nome completo do desporto

        // Recupera o ID do atleta logado
        int athleteId = Session.getLoggedUserId();

        // Obtém os dados do calendário pelo DAO
        List<Calendar> calendarList = AthleteRegistrationDAO.getCalendarByAthleteId(athleteId);

        // Adiciona os dados retornados pelo DAO à ObservableList
        athleteData.addAll(calendarList);

        // Define os itens na tabela
        calendarAthleteTableView.setItems(athleteData);
    }

    /**
     * Manipula o evento do botão "Menu".
     * Carrega a interface para o menu do atleta.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta();
    }

    /**
     * Obtém o estágio (janela) atual da aplicação a partir de tabela da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) calendarAthleteTableView.getScene().getWindow();
    }
}