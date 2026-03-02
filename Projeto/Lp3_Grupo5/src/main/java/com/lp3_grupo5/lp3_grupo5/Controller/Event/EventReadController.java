package com.lp3_grupo5.lp3_grupo5.Controller.Event;

import com.lp3_grupo5.lp3_grupo5.DAO.EventDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

/**
 * Controlador responsável pela interface de visualização e gestão de eventos.
 * Este controlador permite visualizar os eventos armazenados no banco de dados,
 * navegar de volta ao menu principal, e realizar a exclusão lógica de eventos.
 */
public class EventReadController {

    // Instância do DAO para interagir com o banco de dados
    private final EventDAO eventDAO = new EventDAO();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnDelete; // Botão para excluir evento.

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal.

    @FXML
    private TableView<Event> eventTV; // Tabela para exibir os eventos.

    @FXML
    private TableColumn<Event, Integer> eventIdTC; // Coluna para o ID do evento.

    @FXML
    private TableColumn<Event, Integer> eventAnoTC; // Coluna para o ano do evento.

    @FXML
    private TableColumn<Event, String> countryTC; // Coluna para o país do evento.

    @FXML
    private TableColumn<Event, String> mascotTC; // Coluna para o mascote do evento.

    @FXML
    private TableColumn<Event, String> inativeTC; // Coluna para indicar se o evento está inativo.

    @FXML
    private TextField searchField;

    @FXML
    private Button btnBack;

    /**
     * Manipula o evento de clique no botão de menu.
     * Ao clicar no botão, o usuário é redirecionado de volta ao menu principal.
     *
     * @param event O evento de clique (ActionEvent).
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Inicializa o controlador configurando as colunas da tabela e carregando os dados
     * dos eventos a partir do banco de dados para exibição na interface.
     */
    @FXML
    private void initialize() {
        // Configura as colunas da tabela para exibir as propriedades dos eventos
        eventIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventAnoTC.setCellValueFactory(new PropertyValueFactory<>("year"));
        countryTC.setCellValueFactory(new PropertyValueFactory<>("country"));
        inativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));
        mascotTC.setCellValueFactory(new PropertyValueFactory<>("mascot"));

        mascotTC.setCellFactory(param -> new TableCell<Event, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String mascotPath, boolean empty) {
                super.updateItem(mascotPath, empty);
                if (empty || mascotPath == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Adiciona um timestamp ao caminho para evitar o cache
                        File file = new File("src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/paises/" + mascotPath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            imageView.setFitHeight(50); // Definir tamanho da imagem
                            imageView.setFitWidth(50);  // Definir tamanho da imagem
                            setGraphic(imageView);
                        } else {
                            System.out.println("Imagem não encontrada: " + mascotPath);
                        }

                    } catch (Exception e) {
                        System.out.println("Erro ao carregar a imagem: " + e.getMessage());
                        setGraphic(null);
                    }
                }
            }
        });


        refreshTable(); // Recarrega os dados ao inicializar

        // Carrega os dados da tabela do banco de dados
        try {
            eventTV.setItems(loadDataFromDatabase());
        } catch (SQLException e) {
            showErrorAlert("Erro ao carregar os dados", "Não foi possível carregar os locais.");
        }
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        try {
            if (searchText.isEmpty()) {
                // Se a pesquisa estiver vazia, recarregar todos os eventos da base de dados
                eventTV.setItems(loadDataFromDatabase());
            } else {
                // Caso contrário, aplicar o filtro
                ObservableList<Event> filteredList = FXCollections.observableArrayList();

                for (Event event1 : eventTV.getItems()) {
                    boolean matches = false;

                    if (String.valueOf(event1.getId()).contains(searchText)) {
                        matches = true;
                    } else if (event1.getCountry().toLowerCase().contains(searchText)) {
                        matches = true;
                    } else if (String.valueOf(event1.getYear()).contains(searchText)) {
                        matches = true;
                    }

                    if (matches) {
                        filteredList.add(event1);
                    }
                }

                eventTV.setItems(filteredList);
            }
        } catch (SQLException e) {
            showErrorAlert("Erro ao carregar os dados", "Não foi possível carregar os eventos.");
        }
    }




    /**
     * Manipula a exclusão lógica de um evento selecionado.
     * Marca o evento selecionado como excluído na base de dados,
     * removendo-o da tabela exibida na interface. Caso nenhum evento
     * esteja selecionado, exibe um alerta ao usuário.
     *
     * @param event O evento de clique (ActionEvent) do botão de exclusão.
     */
    @FXML
    void handleBtnDelete(ActionEvent event) {
        // Obtém o evento selecionado na tabela
        Event selectedEvent = eventTV.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                // Verifica se existem desportos ativos associados a qualquer evento
                if (eventDAO.hasActiveSports()) {
                    showAlert("Atenção", "Não é possível desativar o evento porque existem desportos ativos com inscrições associados a ele.");
                } else {
                    // Marca o evento como excluído no banco de dados através do DAO
                    if (eventDAO.markEventAsinative(selectedEvent.getId())) {
                        // Remove o evento da tabela se a exclusão for bem-sucedida
                        eventTV.getItems().remove(selectedEvent);
                    } else {
                        showAlert("Erro", "Erro ao excluir o evento.");
                    }
                }
            } catch (SQLException e) {
                showErrorAlert("Erro ao verificar desportos ativos", "Ocorreu um erro ao verificar se existem desportos ativos associados ao evento.");
                System.out.println(e);
            }
        } else {
            // Exibe um alerta caso nenhum evento tenha sido selecionado
            showAlert("Atenção", "Por favor, selecione um evento antes de tentar excluí-lo.");
        }
    }

    /**
     * Atualiza os dados da tabela.
     */
    @FXML
    private void refreshTable() {
        try {
            ObservableList<Event> updatedData = loadDataFromDatabase();
            eventTV.setItems(updatedData);
            // Força a atualização da coluna de imagens
            mascotTC.setVisible(false);
            mascotTC.setVisible(true);
        } catch (SQLException e) {
            showErrorAlert("Erro ao recarregar os dados", "Não foi possível recarregar os eventos.");
        }
    }

    /**
     * Carrega os dados de todos os eventos a partir do banco de dados.
     *
     * @return Lista observável de eventos.
     * @throws SQLException Caso ocorra um erro ao carregar os dados do banco de dados.
     */
    private ObservableList<Event> loadDataFromDatabase() throws SQLException {
        return FXCollections.observableArrayList(eventDAO.loadAllEvents());
    }

    /**
     * Exibe um alerta na interface com uma mensagem informativa.
     * O alerta é utilizado para exibir mensagens de erro ou aviso ao usuário.
     *
     * @param title   O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLEventMenu();
    }
}
