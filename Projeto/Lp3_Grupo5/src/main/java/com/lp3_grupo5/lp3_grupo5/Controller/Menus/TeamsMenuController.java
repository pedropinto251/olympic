package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu de equipes (TeamsMenuController).
 * Este controlador gerencia as interações no menu de equipes, incluindo adicionar, editar, listar equipes e sair do menu.
 */
public class TeamsMenuController {



    @FXML
    private JFXButton btnEditTeams;  // Botão para editar uma equipe existente

    @FXML
    private JFXButton btnListTeams;  // Botão para listar todas as equipes
    @FXML
    private JFXButton btnTeamResult;

    @FXML
    private Button btnSair;  // Botão para sair do menu de equipes e voltar ao menu principal

    /**
     * Manipula o evento do botão "Adicionar Equipe".
     * Carrega a interface para adicionar uma nova equipe.
     *
     * @param event O evento de clique no botão.
     */


    /**
     * Manipula o evento do botão "Editar Equipe".
     * Carrega a interface para editar uma equipe existente.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnEditTeams(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamEdit();  // Carrega a interface para edição de uma equipe
    }

    /**
     * Manipula o evento do botão "Listar Equipes".
     * Carrega a interface para listar todas as equipes disponíveis.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnListTeams(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamList();  // Carrega a lista de equipes
    }

    /**
     * Manipula o evento do botão "Sair".
     * Fecha o menu de equipes e carrega o menu principal.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnSair(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();  // Carrega o menu principal
    }

    /**
     * Obtém o estágio (janela) atual a partir de um dos botões da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btnSair.getScene().getWindow();
    }

    public void handleBtnTeamResult(ActionEvent actionEvent) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAllTeamParticipations();
    }
}
