package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Controlador para o menu principal da aplicação (MenuController).
 * Este controlador gerencia as interações do menu principal, permitindo ao usuário acessar diferentes menus
 * de funcionalidades como equipes, atletas, eventos, locais, modalidades, e sair da aplicação.
 */
public class MenuController {

    @FXML
    private JFXButton btnMenuFXML;  // Botão para carregar o menu principal

    @FXML
    private Button btnSair;  // Botão para sair da aplicação

    @FXML
    private JFXButton btn_MenuEquipas;  // Botão para acessar o menu de equipes

    @FXML
    private JFXButton btn_MenuAtletas;  // Botão para acessar o menu de atletas

    @FXML
    private JFXButton btn_MenuEventos;  // Botão para acessar o menu de eventos

    @FXML
    private JFXButton btn_MenuLocais;  // Botão para acessar o menu de locais

    @FXML
    private JFXButton btn_MenuModalidades;  // Botão para acessar o menu de modalidades
    @FXML
    private JFXButton btn_MenuGestaoEvento;
    @FXML
    private Button btnCalendario;

    @FXML
    private JFXButton btn_MenuAPI;
    @FXML
    private JFXButton btn_JogosAPI1;


    /**
     * Manipula o evento do botão "Menu Atletas".
     * Carrega a interface do menu de atletas.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuAtletas(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLAthleteMenu();
    }

    /**
     * Manipula o evento do botão "Menu Eventos".
     * Carrega a interface do menu de eventos.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuEventos(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLEventMenu();
    }

    /**
     * Manipula o evento do botão "Menu Principal".
     * Carrega a interface do menu principal de edição de eventos.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuFXML(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        currentStage.centerOnScreen();
        loader.loadXMLMenu();  // Carrega e exibe a tela de edição de evento
    }

    /**
     * Manipula o evento do botão "Menu Locais".
     * Carrega a interface do menu de locais.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuLocais(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalMenu();
    }

    /**
     * Manipula o evento do botão "Menu Modalidades".
     * Carrega a interface do menu de modalidades.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuModalidades(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLSportsMenu();
    }

    /**
     * Manipula o evento do botão "Sair".
     * Exibe uma janela de confirmação antes de fechar a aplicação.
     *
     * Se o usuário confirmar a ação, a aplicação será fechada e a tela de login será carregada.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnSair(ActionEvent event) {
        // Criar uma janela de confirmação para o fechamento
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setContentText("Você tem certeza que deseja sair?");

        // Exibir a janela de confirmação
        Optional<ButtonType> result = alert.showAndWait();

        // Se o usuário confirmar, fecha a janela e carrega a tela de login
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage currentStage = getStage();
            LoaderFXML loader = new LoaderFXML(currentStage);
            loader.loadLogin();
        }
    }

    /**
     * Manipula o evento do botão "Alterar Senha".
     * Carrega a interface de alteração de senha para o usuário.
     * Também imprime os detalhes do usuário logado no console para depuração.
     *
     * @param actionEvent O evento de clique no botão.
     */
    @FXML
    public void handleBtnPassword(ActionEvent actionEvent) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadPasswordView();
        System.out.println(Session.getLoggedUserId());
        System.out.println(Session.getUser());
    }

    /**
     * Manipula o evento do botão "Menu Equipas".
     * Carrega a interface do menu de equipes.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenuEquipas(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamMenu();
    }

    @FXML
    void handleBtnMenuGestaoEvento(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGestaoMenu();
    }

    @FXML
    void handleBtnCalendar(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCalendarAllView();
    }
    @FXML
    void handleBtnMenuAPI(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAltClientAPI();
    }


    @FXML
    void handleBtnJogosAPI(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAllGamesAPI();
    }
    /**
     * Obtém o estágio (janela) atual da aplicação a partir de um dos botões da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btnMenuFXML.getScene().getWindow();
    }
}
