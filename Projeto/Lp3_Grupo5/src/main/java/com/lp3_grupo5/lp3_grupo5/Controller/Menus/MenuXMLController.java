package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import com.lp3_grupo5.lp3_grupo5.View.ValidarXmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para o menu de manipulação de arquivos XML (MenuXMLController).
 * Este controlador gerencia as interações no menu que lida com arquivos XML, como a validação de XMLs e o carregamento de listas de atletas, desportos e equipes.
 */
public class MenuXMLController {

    @FXML
    private Button btnSair;  // Botão para sair do menu XML e voltar para o menu principal

    @FXML
    private JFXButton btnValidarFXML;  // Botão para validar arquivos XML

    @FXML
    private JFXButton btn_XMLAtletas;  // Botão para carregar a lista de atletas a partir de um XML

    @FXML
    private JFXButton btn_XMLDesportos;  // Botão para carregar a lista de desportos a partir de um XML

    @FXML
    private JFXButton btn_XMLEquipas;  // Botão para carregar a lista de equipes a partir de um XML

    /**
     * Manipula o evento do botão "Sair".
     * Carrega o menu principal da aplicação, fechando o menu atual.
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
     * Manipula o evento do botão "Validar XML".
     * Carrega a interface de validação de arquivos XML.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnValidarFXML(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadHistoricoXML();
    }

    /**
     * Manipula o evento do botão "Carregar XML Atletas".
     * Carrega a interface que exibe a lista de atletas a partir de um arquivo XML.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnXMLAtletas(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadFXMLAthleteList();  // Carrega a lista de atletas
    }

    /**
     * Manipula o evento do botão "Carregar XML Desportos".
     * Carrega a interface que exibe a lista de desportos a partir de um arquivo XML.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnXMLDesportos(ActionEvent event) {
        //openNewView(CarregarFxmlDesportosView.class);// Abre a tela de carregamento de desportos
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadSportXML();
    }

    /**
     * Manipula o evento do botão "Carregar XML Equipas".
     * Carrega a interface que exibe a lista de equipes a partir de um arquivo XML.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnXMLEquipas(ActionEvent event) {
        //openNewView(CarregarFxmlTeamsView.class);// Abre a tela de carregamento de equipes
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadTeamXML();
    }

    /**
     * Abre uma nova view utilizando reflexão para instanciar e invocar a classe fornecida.
     *
     * @param viewClass A classe da view a ser carregada.
     */
    private void openNewView(Class<?> viewClass) {
        try {
            Stage currentStage = getStage();
            // Usamos a reflexão para criar a instância da classe de view
            Object view = viewClass.getConstructor(Stage.class).newInstance(currentStage);
            // Chama o método start() da view
            view.getClass().getMethod("start").invoke(view);
        } catch (Exception e) {
            e.printStackTrace();  // Em caso de erro, imprime o stack trace
        }
    }

    /**
     * Obtém o estágio (janela) atual a partir de um dos botões da interface.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) btnValidarFXML.getScene().getWindow();
    }
}
