package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu de modalidades desportivas (SportsMenuController).
 * Este controlador gerencia as interações no menu de modalidades, incluindo adicionar, editar, listar modalidades e gerar resultados.
 */
public class SportsMenuController {

    @FXML
    private Button btnSair;  // Botão para sair do menu de modalidades e voltar ao menu principal

    @FXML
    private JFXButton btn_AddEventos;  // Botão para adicionar uma nova modalidade

    @FXML
    private JFXButton btn_EditEventos;  // Botão para editar uma modalidade existente

    @FXML
    private JFXButton btnlistModalidades;  // Botão para listar todas as modalidades



    /**
     * Manipula o evento do botão "Adicionar Modalidade".
     * Carrega a interface para adicionar uma nova modalidade desportiva.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnAddModalidades(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadSportCreate();  // Carrega a interface de criação de modalidade
    }

    /**
     * Manipula o evento do botão "Editar Modalidade".
     * Carrega a interface para editar uma modalidade desportiva existente.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnEditModalidades(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadSportEdit();  // Carrega a interface de edição de modalidade
    }

    /**
     * Manipula o evento do botão "Listar Modalidades".
     * Carrega a interface para listar todas as modalidades desportivas.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnListModalidades(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadSportList();// Carrega a lista de modalidades
    }







    /**
     * Manipula o evento do botão "Sair".
     * Fecha o menu de modalidades e carrega o menu principal.
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
     * Manipula o evento do botão "Gerar Resultados".
     * Carrega a interface para gerar resultados das modalidades.
     *
     * @param event O evento de clique no botão.
     */


    /**
     * Obtém o estágio (janela) atual a partir de um dos botões da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btn_AddEventos.getScene().getWindow();
    }

}
