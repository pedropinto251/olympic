package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Rule;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class SportReadController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnActivate;
    @FXML
    private Button btnMenu;

    @FXML
    private Button btnBack;

    @FXML
    private TableView<Sport> sportTV;

    @FXML
    private TableColumn<Sport, String> inativeTC;

    @FXML
    private TableColumn<Sport, String> sportDescriptionTC;

    @FXML
    private TableColumn<Sport, String> sportGameTC;

    @FXML
    private TableColumn<Sport, String> sportGenderTC;

    @FXML
    private TableColumn<Sport, Integer> sportIdTC;

    @FXML
    private TableColumn<Sport, String> sportMeasureTC;

    @FXML
    private TableColumn<Sport, Integer> sportMinParticipantsTC;

    @FXML
    private TableColumn<Sport, String> sportNameTC;

    @FXML
    private TableColumn<Sport, String> sportOlympicRecordTC;

    @FXML
    private TableColumn<Sport, Integer> sportOlympicRecordYearTC;

    @FXML
    private TableColumn<Sport, String> sportOlympicRecordHolderTC;

    @FXML
    private TableColumn<Sport, String> sportTypeTC;

    @FXML
    private TableColumn<Sport, String> sportWinnerOlympicTimeTC;

    @FXML
    private TableColumn<Sport, Integer> sportWinnerOlympicYearTC;

    @FXML
    private TableColumn<Sport, String> sportWinnerOlympicHolderTC;

    @FXML
    private TableColumn<Sport, String> sportRuleTC;

    @FXML
    private TextField searchField; // Campo de pesquisa


    private SportDAO sportDAO = new SportDAO();

    /**
     * Método que lida com o evento de clicar no botão de menu.
     * <p>
     * Quando o botão do menu é clicado, o método fecha a janela atual e carrega a tela principal do menu.
     * </p>
     *
     * @param event O evento de ação que foi disparado pelo botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Método de inicialização que configura as colunas da tabela com os valores da classe {@link Sport}.
     * <p>
     * Este método é chamado automaticamente quando a interface gráfica é carregada.
     * </p>
     */
    @FXML
    private void initialize() {
        sportIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        sportTypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        sportGenderTC.setCellValueFactory(new PropertyValueFactory<>("genre"));
        sportDescriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        sportMinParticipantsTC.setCellValueFactory(new PropertyValueFactory<>("minParticipants"));
        sportMeasureTC.setCellValueFactory(new PropertyValueFactory<>("scoringMeasure"));
        sportGameTC.setCellValueFactory(new PropertyValueFactory<>("oneGame"));
        sportOlympicRecordTC.setCellValueFactory(new PropertyValueFactory<>("olympicRecordTime"));
        sportOlympicRecordYearTC.setCellValueFactory(new PropertyValueFactory<>("olympicRecordYear"));
        sportOlympicRecordHolderTC.setCellValueFactory(new PropertyValueFactory<>("olympicRecordHolder"));
        sportWinnerOlympicYearTC.setCellValueFactory(new PropertyValueFactory<>("winnerOlympicYear"));
        sportWinnerOlympicTimeTC.setCellValueFactory(new PropertyValueFactory<>("winnerOlympicTime"));
        sportWinnerOlympicHolderTC.setCellValueFactory(new PropertyValueFactory<>("winnerOlympicHolder"));
        inativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));
        sportRuleTC.setCellValueFactory(new PropertyValueFactory<>("rule"));

        // Carrega os dados da tabela usando a DAO
        loadDataFromDatabase();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            loadDataFromDatabase();
        } else {
            ObservableList<Sport> filteredList = FXCollections.observableArrayList();

            for (Sport sport : sportTV.getItems()) {
                boolean matches = false;

                // Verifica os campos: nome, ID, tipo, gênero e outros campos relevantes
                if (sport.getName().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(sport.getId()).contains(searchText)) {
                    matches = true;
                } else if (sport.getType().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (sport.getGenre().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (sport.getDescription().toLowerCase().contains(searchText)) {
                    matches = true;
                }

                if (matches) {
                    filteredList.add(sport);
                }
            }

            sportTV.setItems(filteredList);
        }
    }

    /**
     * Método que carrega os dados dos desportos a partir da base de dados.
     * <p>
     * Este método utiliza o {@link SportDAO} para recuperar a lista de desportos e, em seguida, exibe esses dados na tabela.
     * </p>
     */
    private void loadDataFromDatabase() {
        List<Sport> sports = sportDAO.findAll1();
        ObservableList<Sport> observableSports = FXCollections.observableArrayList(sports);
        sportTV.setItems(observableSports);
    }

    /**
     * Método que lida com o evento de clicar no botão de excluir.
     * <p>
     * Quando o botão de excluir é clicado, o método tenta excluir o desporto selecionado. Se a exclusão for bem-sucedida,
     * uma mensagem de sucesso é exibida. Caso contrário, uma mensagem de erro será mostrada.
     * </p>
     *
     * @param event O evento de ação que foi disparado pelo botão.
     */
    @FXML
    void handleBtnDelete(ActionEvent event) {
        Sport selectedSport = sportTV.getSelectionModel().getSelectedItem();

        if (selectedSport != null) {
            try {
                boolean success = sportDAO.deleteSport(selectedSport.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Modalidade excluída com sucesso!");
                    alert.setContentText("A modalidade foi marcada como excluída.");
                    alert.showAndWait();

                    // Atualiza a tabela
                    sportTV.getItems().remove(selectedSport);
                } else {
                    showErrorAlert("Erro ao excluir a modalidade.");
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao tentar excluir a modalidade.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Nenhuma modalidade selecionada.");
        }
    }
    @FXML
    void handleBtnActivate(ActionEvent event) {
        Sport selectedSport = sportTV.getSelectionModel().getSelectedItem();

        if (selectedSport != null) {
            try {
                boolean success = sportDAO.activateSport(selectedSport.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Modalidade Ativada com sucesso!");
                    alert.setContentText("A modalidade foi marcada como Ativa.");
                    alert.showAndWait();

                    // Atualiza a tabela
                    sportTV.getItems().remove(selectedSport);
                } else {
                    showErrorAlert("Erro ao ativar a modalidade.");
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao tentar ativar a modalidade.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Nenhuma modalidade selecionada.");
        }
    }
    /**
     * Exibe uma mensagem de erro em um alerta.
     * <p>
     * Este método é utilizado para exibir mensagens de erro quando uma operação falha, como a tentativa de exclusão de
     * um desporto que não pode ser removido.
     * </p>
     *
     * @param message A mensagem de erro que será exibida no alerta.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro ao processar a operação");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBtnBack(ActionEvent event){
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLSportsMenu();
    }
}
