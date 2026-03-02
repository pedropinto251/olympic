package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.TeamOlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Controlador para a visualização das participações olímpicas de uma equipa.
 * <p>
 * Este controlador permite a leitura e exibição das participações olímpicas de uma equipa associada
 * a um atleta logado. Também permite navegar para o menu principal.
 * </p>
 */
public class TeamParticipationReadController {

    @FXML
    private TableView<TeamOlympicParticipation> TeamParticipationTable;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnMenu;

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

    private AthleteDAO teamParticipationDAO = new AthleteDAO();

    /**
     * Método de inicialização do controlador. Este método configura as colunas da tabela
     * de participações olímpicas e carrega as participações do atleta logado.
     */
    @FXML
    public void initialize() {
        // Configurar as colunas da tabela para exibir as informações de cada participação olímpica
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        inativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInactive() ? "Desativado" : "Ativo"));

        // Obter o ID do atleta logado a partir da sessão
        int athleteId = Session.getLoggedUserId();  // Recupera o ID do atleta logado

        // Buscar as participações olímpicas do atleta
        List<TeamOlympicParticipation> participations = AthleteDAO.getTeamParticipationsByAthleteId(athleteId);

        // Preencher a tabela com as participações do atleta
        TeamParticipationTable.getItems().setAll(participations);
    }

    /**
     * Método para tratar o evento de clique no botão de menu.
     * Este método fecha a janela atual e carrega o menu inicial do atleta.
     *
     * @param actionEvent O evento de ação gerado ao clicar no botão.
     */
    public void handleBtnMenu(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta();  // Carrega a vista do menu inicial do atleta
    }

    // Exemplo de evento de botão para desativar participação (a implementar posteriormente)
}
