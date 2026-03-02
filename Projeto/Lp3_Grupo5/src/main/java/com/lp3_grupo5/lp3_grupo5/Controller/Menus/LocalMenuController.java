package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu de locais. Esta classe gerencia as ações dos botões no menu de locais,
 * carregando as telas correspondentes para a criação, edição e listagem de locais.
 */
public class LocalMenuController {

    @FXML
    private Button btnSair;  // Botão para sair do menu de locais

    @FXML
    private JFXButton btn_AddLocais;  // Botão para adicionar um novo local

    @FXML
    private JFXButton btn_EditLocais;  // Botão para editar um local existente

    @FXML
    private JFXButton btnlistLocais;  // Botão para listar os locais existentes

    /**
     * Método chamado quando o botão "Adicionar Locais" é clicado.
     * Carrega a tela para criar um novo local.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnAddLocais(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalCreate();
    }

    /**
     * Método chamado quando o botão "Editar Locais" é clicado.
     * Carrega a tela para editar um local existente.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnEditLocais(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalEdit();
    }

    /**
     * Método chamado quando o botão "Listar Locais" é clicado.
     * Carrega a tela para listar os locais existentes.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnListLocais(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalList();
    }

    /**
     * Método chamado quando o botão "Sair" é clicado.
     * Carrega o menu principal da aplicação.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnSair(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Método auxiliar para obter o estágio (janela) atual da aplicação.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btn_AddLocais.getScene().getWindow();
    }
}
