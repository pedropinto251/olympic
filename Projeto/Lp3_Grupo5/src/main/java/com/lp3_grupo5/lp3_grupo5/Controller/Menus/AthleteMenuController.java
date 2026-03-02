package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controlador para o menu do atleta. Esta classe gerencia as ações dos botões no menu de atleta,
 * carregando as respectivas telas conforme as interações do usuário.
 */
public class AthleteMenuController {

    @FXML
    private JFXButton btn_AddAtleta;  // Botão para adicionar um novo atleta

    /**
     * Método chamado quando o botão "Adicionar Atleta" é clicado.
     * Carrega a tela de adição de um novo atleta.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnAddAtleta(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAddAthlete();
    }



    /**
     * Método chamado quando o botão "Editar Atleta" é clicado.
     * Carrega a tela para editar os dados de um atleta.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnEditAtleta(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAtheleteEdit();
    }

    /**
     * Método chamado quando o botão "Listar Atletas" é clicado.
     * Carrega a tela que exibe a lista de atletas.
     *
     * @param event O evento de clique do botão.
     */
    @FXML
    void handleBtnListAtleta(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAthletesList();
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
        return (Stage) btn_AddAtleta.getScene().getWindow();
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
    @FXML
    void handleBtnIndParticipations(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadIndividualParticipation();
    }
}
