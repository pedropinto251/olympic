package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu do atleta (MenuAtletaController).
 * Este controlador gerencia as interações no menu do atleta, permitindo ao usuário fazer inscrição,
 * visualizar equipes e resultados, gerenciar a senha, e sair do menu.
 */
public class MenuAtletaController {

    @FXML
    private Button btnInscricao;  // Botão para realizar inscrição em uma equipe

    @FXML
    private Button btnVisualizarEquipasResultados;  // Botão para visualizar equipes e resultados gerais

    @FXML
    private Button btnEquipasInscrito;  // Botão para visualizar as equipes em que o atleta está inscrito

    @FXML
    private Button btnResultadosEquipasInscrito;  // Botão para visualizar os resultados das equipes nas quais o atleta está inscrito

    @FXML
    private Button btnSair;  // Botão para sair do menu do atleta

    @FXML
    private Button btnPassword;  // Botão para alterar a senha do atleta

    /**
     * Método de inicialização do controlador.
     * Este método é chamado automaticamente após a criação dos elementos FXML.
     * Pode ser usado para qualquer configuração inicial necessária.
     */
    @FXML
    public void initialize() {
        // Inicialização adicional, se necessário
    }

    /**
     * Manipula o evento do botão "Fazer Inscrição".
     * Carrega a interface para o atleta fazer uma nova inscrição em uma equipe.
     */
    @FXML
    public void handleBtnInscricao() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamInscription();
    }

    /**
     * Manipula o evento do botão "Visualizar Equipas e Resultados".
     * Carrega a interface para visualização das equipes e dos resultados gerais dos eventos.
     */
    @FXML
    public void handleBtnVisualizarEquipasResultados() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewParticipationIdAtleta();
    }

    /**
     * Manipula o evento do botão "Minhas Equipas".
     * Carrega a interface para o atleta visualizar as equipes nas quais ele está inscrito.
     */
    @FXML
    public void handleBtnEquipasInscrito() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAthleteRegistration();
    }

    /**
     * Manipula o evento do botão "Resultados Minhas Equipas".
     * Carrega a interface para o atleta visualizar os resultados das equipes nas quais ele está inscrito.
     */
    @FXML
    public void handleBtnResultadosEquipasInscrito() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewParticipationTeamIdAtleta();
    }

    /**
     * Manipula o evento do botão "Sair".
     * Fecha o menu do atleta e retorna à tela de login.
     */
    @FXML
    public void handleBtnSair() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }

    /**
     * Manipula o evento do botão "Alterar Senha".
     * Carrega a interface para o atleta alterar a sua senha.
     * Também imprime os detalhes do usuário logado no console.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnPassword(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadPasswordView();
        System.out.println(Session.getLoggedUserId());
        System.out.println(Session.getUser());
    }

    /**
     * Manipula o evento do botão "Calendário".
     * Carrega a interface para o calendário do atleta.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnCalendar(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCalendarView();
    }


    @FXML
    void handleBtnPhotoEdit(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditPhoto();
    }

    /**
     * Obtém o estágio (janela) atual da aplicação a partir de um dos botões da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btnInscricao.getScene().getWindow();
    }
}
