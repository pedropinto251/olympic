package com.lp3_grupo5.lp3_grupo5.Controller.Menus;

import com.jfoenix.controls.JFXButton;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GestaoEventoController {

    @FXML
    private JFXButton btnAproveInscription;

    @FXML
    private JFXButton btnInitSport;

    @FXML
    private Button btnSair;

    @FXML
    private JFXButton btnReadCollective;

    @FXML
    private JFXButton btnReadIndividual;

    @FXML
    private JFXButton btnIntResult;

    @FXML
    private JFXButton btnSportEvent;

    @FXML
    void handleBtnAproveInscription(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAproveAthleteList();
    }

    @FXML
    void handleBtnInitSport(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGenerateResult();
    }

    @FXML
    void handleBtnSair(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();  // Carrega o menu principal
    }
    private Stage getStage() {
        return (Stage) btnInitSport.getScene().getWindow();
    }

    @FXML
    void handlebtnReadCollective(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadReadIndividual();
    }

    @FXML
    void handlebtnReadIndividual(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadReadCollective();
    }

    @FXML
    void handleBtnIntResult(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAltIntervalo();
    }

    @FXML
    void handleBtnSportEvent(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        // Carrega a lista de modalidades
        loader.loadSportReadEvent();
    }

}
