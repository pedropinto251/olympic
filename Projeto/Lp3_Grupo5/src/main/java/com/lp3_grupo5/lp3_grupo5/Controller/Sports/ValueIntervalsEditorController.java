package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class ValueIntervalsEditorController {

    @FXML
    private CheckBox timeCheckBox;
    @FXML
    private CheckBox distanceCheckBox;
    @FXML
    private CheckBox pointsCheckBox;
    @FXML
    private TextField minValueField;
    @FXML
    private TextField maxValueField;
    @FXML
    private Label statusLabel;

    private SportDAO sportDAO = new SportDAO();

    @FXML
    private void initialize() {
        // Adicionar listeners para as checkboxes
        timeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> handleCheckBoxSelection("Time", newValue));
        distanceCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> handleCheckBoxSelection("Distance", newValue));
        pointsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> handleCheckBoxSelection("Points", newValue));
    }

    private void handleCheckBoxSelection(String type, boolean isSelected) {
        if (isSelected) {
            // Desmarcar as outras checkboxes
            if (type.equals("Time")) {
                distanceCheckBox.setSelected(false);
                pointsCheckBox.setSelected(false);
            } else if (type.equals("Distance")) {
                timeCheckBox.setSelected(false);
                pointsCheckBox.setSelected(false);
            } else if (type.equals("Points")) {
                timeCheckBox.setSelected(false);
                distanceCheckBox.setSelected(false);
            }

            // Carregar os valores atuais do intervalo selecionado
            try {
                double[] interval = sportDAO.getInterval(type);
                minValueField.setText(String.valueOf(interval[0]));
                maxValueField.setText(String.valueOf(interval[1]));
            } catch (SQLException e) {
                statusLabel.setText("Erro ao carregar intervalo: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave() {
        String type = null;
        if (timeCheckBox.isSelected()) {
            type = "Time";
        } else if (distanceCheckBox.isSelected()) {
            type = "Distance";
        } else if (pointsCheckBox.isSelected()) {
            type = "Points";
        }

        if (type == null) {
            statusLabel.setText("Por favor, selecione um tipo de intervalo.");
            return;
        }

        try {
            double minValue = Double.parseDouble(minValueField.getText());
            double maxValue = Double.parseDouble(maxValueField.getText());

            // Verificações específicas para cada tipo
            if (type.equals("Time") && (minValue < 0 || maxValue > 86400)) { // 86400 segundos = 24 horas
                statusLabel.setText("Intervalo de tempo inválido. Deve estar entre 0 e 86400 segundos.");
                return;
            } else if (type.equals("Distance") && (minValue < 0 || maxValue > 1000)) { // Exemplo de limite para distância
                statusLabel.setText("Intervalo de distância inválido. Deve estar entre 0 e 1000 metros.");
                return;
            } else if (type.equals("Points") && (minValue < 0 || maxValue > 1000)) { // Exemplo de limite para pontos
                statusLabel.setText("Intervalo de pontos inválido. Deve estar entre 0 e 1000 pontos.");
                return;
            }

            sportDAO.updateInterval(type, minValue, maxValue);
            statusLabel.setText("Intervalo atualizado com sucesso.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Por favor, insira valores válidos.");
        } catch (SQLException e) {
            statusLabel.setText("Erro ao atualizar intervalo: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToMenu() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();  // Carrega o menu principal
    }
    private Stage getStage() {
        return (Stage) statusLabel.getScene().getWindow();
    }

    @FXML
    private void handleBtnBack() {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGestaoMenu();
    }
}