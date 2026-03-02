package com.lp3_grupo5.lp3_grupo5.Controller.Local;

import com.lp3_grupo5.lp3_grupo5.DAO.LocalDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ILocalDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Controlador para a tela de listagem e exclusão de locais.
 * Esta classe gerencia a interação do usuário com a tabela de locais e permite a exclusão de locais do sistema.
 */
public class LocalReadController {

    @FXML
    private TableView<Location> LocalTV;  // Tabela que exibe os locais
    @FXML
    private TableColumn<Location, Integer> LocalIdTC;  // Coluna para o ID do local
    @FXML
    private TableColumn<Location, String> addressTC;  // Coluna para o endereço
    @FXML
    private TableColumn<Location, String> cityTC;  // Coluna para a cidade
    @FXML
    private TableColumn<Location, Integer> CapacityTC;  // Coluna para a capacidade
    @FXML
    private TableColumn<Location, Integer> year_builtTC;  // Coluna para o ano de construção
    @FXML
    private TableColumn<Location, String> inativeTC;  // Coluna para indicar se o local foi excluído
    @FXML
    private TableColumn<Location, Boolean> id_EventTC;  // Coluna para o ID do evento associado
    @FXML
    private TableColumn<Location, Boolean> typeTC;
    @FXML
    private Button btnMenu;  // Botão para voltar ao menu principal
    @FXML
    private Button btnDelete;  // Botão para excluir um local
    @FXML
    private TextField searchField;
    @FXML
    private Button btnBack;

    private ILocalDAO localDAO = new LocalDAO();  // Instância de LocalDAO para acessar dados de locais

    /**
     * Método chamado para voltar ao menu principal da aplicação.
     * Ao ser acionado, carrega o menu principal e fecha a tela atual.
     */
    @FXML
    public void handleBtnMenu() {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);  // Passa o currentStage ao invés de criar um novo
        loader.loadMenu();  // Carrega o menu principal
    }

    /**
     * Inicializa a tabela de locais e configura as colunas da tabela.
     * Este método é automaticamente chamado após a tela ser carregada.
     */
    @FXML
    private void initialize() {
        // Configuração das colunas da tabela
        LocalIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        addressTC.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityTC.setCellValueFactory(new PropertyValueFactory<>("city"));
        CapacityTC.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        year_builtTC.setCellValueFactory(new PropertyValueFactory<>("yearBuilt"));
        id_EventTC.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        inativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Carrega os dados da tabela do banco de dados
        try {
            LocalTV.setItems(loadDataFromDatabase());
        } catch (SQLException e) {
            showErrorAlert("Erro ao carregar os dados", "Não foi possível carregar os locais.");
        }
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            try {
                LocalTV.setItems(loadDataFromDatabase());
            } catch (SQLException e) {
                showErrorAlert("Erro ao carregar os locais.", "Não foi possível carregar os locais.");
            }
            return;
        }

        ObservableList<Location> filteredList = FXCollections.observableArrayList();

        try {
            for (Location location : loadDataFromDatabase()) {
                boolean matches = false;

                if (location.getAddress().toLowerCase().contains(searchText)) {
                    matches = true;
                } else if (String.valueOf(location.getId()).contains(searchText)) {
                    matches = true;
                }

                if (matches) {
                    filteredList.add(location);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        LocalTV.setItems(filteredList);
    }



    /**
     * Carrega os dados de todos os locais a partir do banco de dados.
     *
     * @return Lista observável de locais.
     * @throws SQLException Caso ocorra um erro ao carregar os dados do banco de dados.
     */
    private ObservableList<Location> loadDataFromDatabase() throws SQLException {
        return FXCollections.observableArrayList(localDAO.loadAll());
    }

    /**
     * Método para excluir um local selecionado.
     * A exclusão é realizada marcando o campo "inative" do local como verdadeiro no banco de dados.
     *
     * @param event Evento gerado ao clicar no botão "Excluir".
     */
    @FXML
    void handleBtnDelete(ActionEvent event) {
        Location selectedLocal = LocalTV.getSelectionModel().getSelectedItem();
        if (selectedLocal != null) {
            boolean inative = localDAO.deleteLocation(selectedLocal.getId());

            if (inative) {
                // Exibe um alerta de sucesso após a exclusão
                showSuccessAlert("Sucesso", "Local excluído com sucesso!");
                // Atualiza a tabela removendo o local excluído
                LocalTV.getItems().remove(selectedLocal);
            } else {
                // Exibe um alerta de erro caso a exclusão falhe
                showErrorAlert("Erro", "Houve um problema ao tentar excluir o local.");
            }
        } else {
            // Exibe um alerta caso nenhum local tenha sido selecionado
            showWarningAlert("Atenção", "Nenhum local selecionado", "Selecione um local antes de tentar excluir.");
        }
    }

    /**
     * Exibe um alerta de sucesso com o título e mensagem fornecidos.
     *
     * @param title   O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de erro com o título e mensagem fornecidos.
     *
     * @param title   O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de aviso com o título, cabeçalho e mensagem fornecidos.
     *
     * @param title   O título do alerta.
     * @param header  O cabeçalho do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showWarningAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalMenu();  // Carrega o menu principal
    }
}
