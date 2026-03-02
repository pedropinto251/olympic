package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu de eventos. Esta classe gerencia as ações dos botões no menu de eventos,
 * carregando as telas correspondentes para a criação, edição e listagem de eventos.
 */
public class EventMenuController {

    @FXML
    private Button btnSair;  // Botão para sair do menu de eventos

    @FXML
    private JFXButton btn_AddEventos;  // Botão para adicionar um novo evento

    @FXML
    private JFXButton btn_EditEventos;  // Botão para editar um evento existente

    @FXML
    private JFXButton btnlistEventos;  // Botão para listar os eventos existentes

    /**
     * Método chamado quando o botão "Adicionar Eventos" é clicado.
     * Carrega a tela para criar um novo evento.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnAddEventos(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEventCreate();
    }

    /**
     * Método chamado quando o botão "Editar Eventos" é clicado.
     * Carrega a tela para editar um evento existente.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnEditEventos(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEventEdit();
    }

    /**
     * Método chamado quando o botão "Listar Eventos" é clicado.
     * Carrega a tela para listar os eventos existentes.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnListEventos(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEventsList();
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
        return (Stage) btn_AddEventos.getScene().getWindow();
    }

    /**
     * Método genérico para abrir uma nova tela de acordo com a classe fornecida.
     * Utiliza reflexão para instanciar a classe da view e invocar o método start().
     *
     * @param viewClass A classe da view que será carregada.
     */
    private void openNewView(Class<?> viewClass) {
        try {
            Stage currentStage = getStage();
            // Usamos a reflexão para criar a instância da classe de view
            Object view = viewClass.getConstructor(Stage.class).newInstance(currentStage);
            // Chama o método start() da view
            view.getClass().getMethod("start").invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
