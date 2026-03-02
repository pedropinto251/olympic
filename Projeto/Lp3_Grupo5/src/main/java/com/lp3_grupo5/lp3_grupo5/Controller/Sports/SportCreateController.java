package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ISportDAO;
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

import java.sql.SQLException;

/**
 * Controlador para a tela de criação de modalidades esportivas.
 * Esta classe é responsável por coletar os dados inseridos pelo usuário e interagir com o DAO para inserir o novo esporte no banco de dados.
 */
public class SportCreateController {

    @FXML
    private Button btnCreate;  // Botão para criar a modalidade

    @FXML
    private Button btnMenu;  // Botão para voltar ao menu principal

    @FXML
    private Button btnBack;

    @FXML
    private TextArea descriptionTA;  // Campo de texto para descrição da modalidade

    @FXML
    private ChoiceBox<String> gametypeCB;  // ChoiceBox para tipo de jogo (único ou múltiplo)

    @FXML
    private ChoiceBox<String> typeCB;  // ChoiceBox para tipo de modalidade (individual ou coletiva)

    @FXML
    private ChoiceBox<String> genderCB;  // ChoiceBox para gênero (masculino ou feminino)

    @FXML
    private ChoiceBox<String> measureCB;  // ChoiceBox para medida de pontuação (tempo, pontos, distância)

    @FXML
    private TextField minParticipantsTF;  // Campo de texto para número mínimo de participantes

    @FXML
    private TextField nameTF;  // Campo de texto para o nome da modalidade

    @FXML
    private TextArea ruleTA;  // Campo de texto para as regras da modalidade

    // Instancia o SportDAO para interagir com o banco de dados
    private ISportDAO sportDAO = new SportDAO();

    /**
     * Método de inicialização que configura as opções das ChoiceBoxes.
     * Este método é executado automaticamente quando a tela é carregada.
     */
    public void initialize() {
        // Inicializa as ChoiceBoxes com os valores permitidos
        typeCB.getItems().addAll("Individual", "Collective");
        genderCB.getItems().addAll("Men", "Women");
        measureCB.getItems().addAll("Time", "Points", "Distance");
        gametypeCB.getItems().addAll("One", "Multiple");
    }

    /**
     * Manipula o evento do botão "Criar Modalidade".
     * Valida os campos preenchidos pelo usuário e cria a nova modalidade no banco de dados.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnCreate(ActionEvent event) {
        // Valida os campos de entrada
        if (isInputValid()) {
            String name = nameTF.getText();
            String type = typeCB.getValue();
            String genre = genderCB.getValue();
            String description = descriptionTA.getText();
            int minParticipants = Integer.parseInt(minParticipantsTF.getText());
            String scoringMeasure = measureCB.getValue();
            String oneGame = gametypeCB.getValue();
            String rule = ruleTA.getText();  // Captura das regras do campo TextArea

            // Cria o objeto Sport com os dados coletados
            Sport sport = new Sport(name, type, genre, description, minParticipants, scoringMeasure, oneGame);

            // Chama o método para inserir o esporte no banco de dados, incluindo as regras
            try {
                if (sportDAO.insertSport(sport, rule)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Modalidade criada com sucesso!");
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao criar uma modalidade. Tente novamente!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao acessar o banco de dados: " + e.getMessage());
            }
        }
    }

    /**
     * Valida se todos os campos obrigatórios foram preenchidos corretamente.
     *
     * @return true se todos os campos estiverem preenchidos, caso contrário, false.
     */
    private boolean isInputValid() {
        if (nameTF.getText().isEmpty() || descriptionTA.getText().isEmpty() ||
                typeCB.getValue() == null || genderCB.getValue() == null ||
                measureCB.getValue() == null || gametypeCB.getValue() == null ||
                minParticipantsTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validação de Dados", "Todos os campos devem ser preenchidos.");
            return false;
        }

        // Verifica se o número mínimo de participantes é válido
        try {
            int minParticipants = Integer.parseInt(minParticipantsTF.getText());
            if (minParticipants <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validação de Dados", "O número mínimo de participantes deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validação de Dados", "O número mínimo de participantes deve ser um número válido.");
            return false;
        }

        return true;
    }


    /**
     * Exibe uma mensagem de alerta com base no tipo de alerta, título e mensagem fornecidos.
     *
     * @param type    O tipo do alerta (informação, erro, aviso).
     * @param title   O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Limpa todos os campos do formulário.
     */
    private void clearFields() {
        descriptionTA.clear();
        typeCB.setValue(null);
        gametypeCB.setValue(null);
        genderCB.setValue(null);
        measureCB.setValue(null);
        minParticipantsTF.clear();
        nameTF.clear();
        ruleTA.clear();
    }

    /**
     * Manipula o evento do botão "Voltar ao Menu".
     * Retorna à tela do menu principal.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage); // Passa o currentStage ao invés de criar um novo
        loader.loadMenu(); // método para carregar o menu principal
    }

    @FXML
    public void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLSportsMenu();
    }
}
