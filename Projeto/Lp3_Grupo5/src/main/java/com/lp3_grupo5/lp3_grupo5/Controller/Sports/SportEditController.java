package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.sql.SQLException;

/**
 * Controlador para a tela de edição de modalidades esportivas.
 * Esta classe é responsável por carregar os dados de uma modalidade existente, permitir a edição desses dados e atualizar as informações no banco de dados.
 */
public class SportEditController {

    // Campos de entrada de dados (TextFields)
    @FXML
    private Button btnEdit;  // Botão para editar a modalidade

    @FXML
    private Button btnMenu;  // Botão para voltar ao menu principal

    @FXML
    private Button btnBack; // Botão para recuar para o menu anterior

    @FXML
    private ChoiceBox<String> gametypeCB;  // ChoiceBox para tipo de jogo (único ou múltiplo)

    @FXML
    private TextField lastWinnerHolderTF;  // Campo de texto para o nome do último vencedor

    @FXML
    private TextField lastWinnerScoreTF;  // Campo de texto para a pontuação do último vencedor

    @FXML
    private ChoiceBox<String> measureCB;  // ChoiceBox para medida de pontuação (tempo, pontos, distância)

    @FXML
    private TextField minParticipantsTF;  // Campo de texto para número mínimo de participantes

    @FXML
    private ChoiceBox<Sport> nameCB;  // ChoiceBox para selecionar o nome da modalidade

    @FXML
    private TextField olympicRecordHolderTF;  // Campo de texto para o detentor do recorde olímpico

    @FXML
    private TextField olympicRecordTF;  // Campo de texto para o recorde olímpico

    @FXML
    private TextField olympicRecordYearTF;  // Campo de texto para o ano do recorde olímpico

    @FXML
    private TextField recentYearTF;  // Campo de texto para o ano do último vencedor

    @FXML
    private ChoiceBox<String> typeCB;  // ChoiceBox para o tipo de modalidade (individual ou coletiva)

    @FXML
    private TextArea descriptionTA;  // Área de texto para a descrição da modalidade

    @FXML
    private TextArea ruleTA;  // Área de texto para as regras da modalidade

    private SportDAO sportDAO = new SportDAO();  // Instância do DAO para interagir com o banco de dados

    /**
     * Método de inicialização que configura as opções das ChoiceBoxes e carrega os esportes existentes.
     * Este método é executado automaticamente quando a tela é carregada.
     */
    public void initialize() {
        // Inicializa as ChoiceBoxes com os valores permitidos
        typeCB.getItems().addAll("Individual", "Collective");
        measureCB.getItems().addAll("Time", "Points", "Distance");
        gametypeCB.getItems().addAll("One", "Multiple");

        // Carrega as modalidades existentes na ChoiceBox de nome
        ObservableList<Sport> sportsList = FXCollections.observableArrayList(sportDAO.findAll());
        nameCB.setItems(sportsList);

        // Configura o conversor de objetos para String na ChoiceBox
        nameCB.setConverter(new StringConverter<Sport>() {
            @Override
            public String toString(Sport sport) {
                return sport == null ? "" : sport.getName();
            }

            @Override
            public Sport fromString(String string) {
                return null;
            }
        });

        // Listener para carregar os detalhes da modalidade selecionada
        nameCB.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadSportDetails(newVal);
            }
        });

        applyNumericValidation(minParticipantsTF);
        applyNumericValidation(olympicRecordYearTF);
        applyNumericValidation(recentYearTF);
    }

    // Função para aplicar a validação de números em um TextField
    private void applyNumericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue); // Se o texto não for numérico, reverte para o valor anterior
            }
        });
    }

    /**
     * Carrega os detalhes da modalidade selecionada na interface.
     * Preenche os campos de entrada com as informações da modalidade escolhida.
     *
     * @param sport A modalidade selecionada
     */
    private void loadSportDetails(Sport sport) {
        nameCB.setValue(sport);
        typeCB.setValue(sport.getType());
        descriptionTA.setText(sport.getDescription());
        minParticipantsTF.setText(String.valueOf(sport.getMinParticipants()));
        measureCB.setValue(sport.getScoringMeasure());
        gametypeCB.setValue(sport.getOneGame());
        olympicRecordTF.setText(sport.getOlympicRecordTime());
        olympicRecordYearTF.setText(String.valueOf(sport.getOlympicRecordYear()));
        olympicRecordHolderTF.setText(sport.getOlympicRecordHolder());
        recentYearTF.setText(String.valueOf(sport.getWinnerOlympicYear()));
        lastWinnerScoreTF.setText(sport.getWinnerOlympicTime());
        lastWinnerHolderTF.setText(sport.getWinnerOlympicHolder());
    }

    /**
     * Manipula o evento de clique no botão "Editar Modalidade".
     * Atualiza as informações da modalidade selecionada com os novos dados inseridos pelo usuário.
     *
     * @param event O evento de clique no botão
     */
    @FXML
    public void handleBtnEdit(ActionEvent event) {
        // Verificar se todos os campos obrigatórios estão preenchidos
        if (nameCB.getSelectionModel().isEmpty() ||
                typeCB.getSelectionModel().isEmpty() ||
                measureCB.getSelectionModel().isEmpty() ||
                gametypeCB.getSelectionModel().isEmpty() ||
                isNullOrEmpty(minParticipantsTF.getText()) ||
                isNullOrEmpty(recentYearTF.getText()) ||
                isNullOrEmpty(descriptionTA.getText())) {

            showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Por favor, preencha todos os campos obrigatórios.");
            return;  // Não prosseguir se algum campo estiver vazio
        }
        Sport sport = nameCB.getSelectionModel().getSelectedItem();
        if (sport != null) {
            // Atualiza os dados da modalidade com as novas informações
            sport.setType(typeCB.getValue());
            sport.setDescription(descriptionTA.getText());
            sport.setMinParticipants(Integer.parseInt(minParticipantsTF.getText()));
            sport.setScoringMeasure(measureCB.getValue());
            sport.setOneGame(gametypeCB.getValue());
            sport.setOlympicRecordTime(olympicRecordTF.getText());
            sport.setOlympicRecordYear(Integer.parseInt(olympicRecordYearTF.getText()));
            sport.setOlympicRecordHolder(olympicRecordHolderTF.getText());
            sport.setWinnerOlympicYear(Integer.parseInt(recentYearTF.getText()));
            sport.setWinnerOlympicTime(lastWinnerScoreTF.getText());
            sport.setWinnerOlympicHolder(lastWinnerHolderTF.getText());

            String rule = ruleTA.getText();  // A nova regra editada pelo usuário

            try {
                boolean updated = sportDAO.updateSport(sport, rule);
                if (updated) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Modalidade atualizada com sucesso!");
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao atualizar a modalidade.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao acessar a base de dados: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica se a string é null ou está vazia.
     */
    private boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Limpa os campos de entrada de dados após uma atualização bem-sucedida.
     */
    private void clearFields() {
        nameCB.getSelectionModel().clearSelection();
        descriptionTA.setText("");
        minParticipantsTF.clear();
        olympicRecordTF.clear();
        olympicRecordHolderTF.clear();
        olympicRecordYearTF.clear();
        recentYearTF.clear();
        lastWinnerScoreTF.clear();
        lastWinnerHolderTF.clear();
        typeCB.getSelectionModel().clearSelection();
        measureCB.getSelectionModel().clearSelection();
        gametypeCB.getSelectionModel().clearSelection();
    }

    /**
     * Exibe uma mensagem de alerta com base no tipo de alerta, título e mensagem fornecidos.
     *
     * @param type    O tipo do alerta (informação, erro, aviso)
     * @param title   O título do alerta
     * @param message A mensagem a ser exibida no alerta
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Manipula o evento de clique no botão "Voltar ao Menu".
     * Retorna à tela do menu principal.
     *
     * @param event O evento de clique no botão
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        // Pega o Stage atual
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();

        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu(); // Carrega o método para o menu principal
    }

    @FXML
    void handleBtnBack(ActionEvent event) {
        // Pega o Stage atual
        Stage currentStage = (Stage) btnBack.getScene().getWindow();

        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLSportsMenu(); // Carrega o método para o menu principal
    }

}
