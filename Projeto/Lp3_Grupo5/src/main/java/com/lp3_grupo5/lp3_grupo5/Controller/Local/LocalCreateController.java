package com.lp3_grupo5.lp3_grupo5.Controller.Local;

import com.lp3_grupo5.lp3_grupo5.DAO.LocalDAO;
import com.lp3_grupo5.lp3_grupo5.Enums.TipoEnum;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Calendar;
import java.util.List;

/**
 * Controlador responsável pela criação de um novo local.
 * Este controlador valida os campos de entrada do formulário e utiliza o DAO para realizar a inserção de um novo local na base de dados.
 */
public class LocalCreateController {

    @FXML
    private TextField AdressTF;  // Campo de texto para o endereço do local

    @FXML
    private Button btnCreate;  // Botão para criar o local

    @FXML
    private Button btnMenu;  // Botão para retornar ao menu principal

    @FXML
    private TextField capacityTF;  // Campo de texto para a capacidade do local

    @FXML
    private TextField cityTF;  // Campo de texto para a cidade onde o local está situado

    @FXML
    private TextField yearBuiltTF;  // Campo de texto para o ano de construção do local

    @FXML
    private ComboBox<Event> EventoCB;  // ComboBox para selecionar um evento relacionado ao local

    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private Button btnBack;

    private final LocalDAO localDAO = new LocalDAO();  // Instância do DAO para realizar operações no modelo Location

    /**
     * Este método é chamado quando o botão "Criar" é pressionado.
     * Ele valida os dados dos campos de texto e insere um novo local na base de dados.
     *
     * @param event O evento do botão "Criar"
     */
    @FXML
    void handleBtnCreate(ActionEvent event) {
        // Verifica se o tipo selecionado é "outdoor" e o campo capacidade está vazio
        String selectedType = typeCB.getSelectionModel().getSelectedItem();
        if (selectedType != null && selectedType.equals(TipoEnum.Out.getDescription()) && capacityTF.getText().trim().isEmpty()) {
            capacityTF.setText("0"); // Define o valor padrão como 0 para locais "outdoor"
        }
        // Valida os campos de entrada
        if (isInputValid()) {
            String address = AdressTF.getText();  // Obtém o endereço
            String city = cityTF.getText();  // Obtém a cidade
            int capacity = Integer.parseInt(capacityTF.getText());  // Obtém a capacidade
            int yearBuilt = Integer.parseInt(yearBuiltTF.getText());  // Obtém o ano de construção
            Event selectedEvent = EventoCB.getSelectionModel().getSelectedItem();// Obtém o evento selecionado no ComboBox


            // Verifica se um evento foi selecionado
            if (selectedEvent == null) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Por favor, selecione um evento.");
                return;
            }

            int eventId = selectedEvent.getId();  // Obtém o ID do evento selecionado



            // Cria um novo objeto Location com os dados fornecidos
            Location newLocation = new Location(0, address, city, capacity, yearBuilt, false, selectedType, eventId);

            // Chama o método do DAO para inserir o local na base de dados
            if (localDAO.insert(newLocation)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Local criado com sucesso!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao criar local. Tente novamente.");
            }
        }
    }

    /**
     * Valida os campos de entrada para garantir que todos os campos estejam preenchidos corretamente.
     *
     * @return true se os campos forem válidos, caso contrário, retorna false
     */
    private boolean isInputValid() {
        String address = AdressTF.getText().trim();
        String city = cityTF.getText().trim();
        String capacityStr = capacityTF.getText().trim();
        String yearBuiltStr = yearBuiltTF.getText().trim();
        String selectedType = typeCB.getSelectionModel().getSelectedItem();

        // Verifica se todos os campos obrigatórios estão preenchidos
        if (address.isEmpty() || city.isEmpty() || yearBuiltStr.isEmpty() || selectedType == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Todos os campos devem ser preenchidos.");
            return false;
        }

        // Valida a cidade
        if (!city.matches("[a-zA-ZÀ-ÿ\\s]+") || city.length() < 2 || city.length() > 50) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "A cidade deve conter apenas letras e espaços, e ter entre 2 e 50 caracteres.");
            return false;
        }

        // Valida a capacidade
        try {
            int capacity = capacityStr.isEmpty() ? 0 : Integer.parseInt(capacityStr);

            // Validação de acordo com o tipo selecionado
            if (selectedType.equals(TipoEnum.Out.getDescription())) {
                // Para "outdoor", aceita capacidade >= 0
                if (capacity < 0) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Para locais 'outdoor', a capacidade deve ser 0 ou maior.");
                    return false;
                }
            } else if (selectedType.equals(TipoEnum.In.getDescription())) {
                // Para "interior", a capacidade deve ser > 0
                if (capacity <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Para locais 'interior', a capacidade deve ser maior que 0.");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "A capacidade deve ser um número válido.");
            return false;
        }

        // Valida o ano de construção
        try {
            int yearBuilt = Integer.parseInt(yearBuiltStr);
            // Verifica se o ano de construção é válido (por exemplo, não pode ser no futuro ou ser zero)
            if (yearBuilt <= 0 || yearBuilt > 2024) { // Supondo que o ano máximo seja o atual
                showAlert(Alert.AlertType.WARNING, "Aviso", "O ano de construção deve ser maior que 0 e não pode ser no futuro.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "O ano de construção deve ser um número válido.");
            return false;
        }


        // Se todas as validações passarem, retorna true
        return true;
    }

    /**
     * Exibe um alerta para o usuário com o tipo, título e mensagem fornecidos.
     *
     * @param alertType Tipo do alerta (INFORMATION, WARNING, ERROR)
     * @param title     Título do alerta
     * @param message   Mensagem do alerta
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Este método é chamado quando o botão "Menu" é pressionado para retornar ao menu principal.
     *
     * @param event O evento do botão "Menu"
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();  // Carrega o menu principal
    }

    /**
     * Limpa todos os campos do formulário.
     */
    private void clearFields() {
        AdressTF.clear();
        capacityTF.clear();
        cityTF.clear();
        yearBuiltTF.clear();
        EventoCB.getSelectionModel().clearSelection();
        typeCB.getSelectionModel().clearSelection();
        capacityTF.setPromptText("");
    }


    /**
     * Este método é chamado durante a inicialização do controlador.
     * Ele popula o ComboBox com a lista de eventos disponíveis.
     */
    public void initialize() {
        // Configura o ComboBox EventoCB com a lista de eventos disponíveis
        List<Event> events = localDAO.getAllEvents();
        ObservableList<Event> eventObservableList = FXCollections.observableArrayList(events);
        EventoCB.setItems(eventObservableList);

        EventoCB.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> param) {
                return new ListCell<Event>() {
                    @Override
                    protected void updateItem(Event item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText("ID: " + item.getId() + " - " + item.getYear() + " - " + item.getCountry());
                        }
                    }
                };
            }
        });

        EventoCB.setButtonCell(new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + " - " + item.getCountry());
                }
            }
        });

        // Configura o ComboBox typeCB com os valores do enum TipoEnum
        ObservableList<String> tipoEnumList = FXCollections.observableArrayList();
        for (TipoEnum tipo : TipoEnum.values()) {
            tipoEnumList.add(tipo.getDescription());
        }
        typeCB.setItems(tipoEnumList);

        // Configura como os itens do ComboBox typeCB são exibidos
        typeCB.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }
        });

        // Popula o ComboBox de tipos com os valores do TipoEnum
        typeCB.setItems(FXCollections.observableArrayList(
                TipoEnum.Out.getDescription(),
                TipoEnum.In.getDescription()
        ));

        // Adiciona um listener ao ComboBox de tipos para ajustar a validação
        typeCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals(TipoEnum.Out.getDescription())) {
                    capacityTF.setPromptText("0");
                } else if (newValue.equals(TipoEnum.In.getDescription())) {
                    capacityTF.setPromptText("Capacidade (maior que 0)");
                }
            }
        });
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalMenu();  // Carrega o menu principal
    }
}
