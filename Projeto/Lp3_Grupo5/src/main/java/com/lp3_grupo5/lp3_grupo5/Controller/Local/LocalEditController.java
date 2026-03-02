package com.lp3_grupo5.lp3_grupo5.Controller.Local;

import com.lp3_grupo5.lp3_grupo5.DAO.LocalDAO;
import com.lp3_grupo5.lp3_grupo5.Enums.TipoEnum;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.DBConnection;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador responsável pela edição de um local.
 * Este controlador permite a seleção de um local existente, a modificação dos seus dados e a atualização do local na base de dados.
 */
public class LocalEditController {

    @FXML
    private TextField addressTF;

    @FXML
    private ChoiceBox<Location> idCB;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenu;

    @FXML
    private TextField capacityTF;

    @FXML
    private TextField cityTF;

    @FXML
    private TextField yearBuiltTF;

    @FXML
    private ComboBox<Event> EventoCB;

    @FXML
    private ComboBox<String> typeCB;

    // Variáveis para armazenar os valores originais do local
    private String originalAddress;
    private String originalCity;
    private int originalCapacity;
    private int originalYearBuilt;
    private String originalType;
    private int eventoselected;

    private final LocalDAO localDAO = new LocalDAO();

    /**
     * Este método é chamado para inicializar o controlador. Ele popula o ChoiceBox com locais existentes
     * e o ComboBox com eventos disponíveis.
     */
    public void initialize() {
        // Popula o ComboBox typeCB com os valores do enum LocationType
        ObservableList<String> types = FXCollections.observableArrayList();
        for (TipoEnum type : TipoEnum.values()) {
            types.add(type.getDescription());
        }
        typeCB.setItems(types);

        // Define um conversor opcional se quiser exibir um nome formatado no ComboBox
        typeCB.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object.replace("_", " ").toLowerCase();
            }

            @Override
            public String fromString(String string) {
                return string.toUpperCase().replace(" ", "_");
            }
        });

        // Popula o ChoiceBox com locais existentes
        ObservableList<Location> locationsList = localDAO.getLocationsFromDatabase();
                /*logetLocationsFromDatabase();*/
        idCB.setItems(locationsList);

        // Define a conversão do objeto Location para String para exibição no ChoiceBox
        idCB.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                return location == null ? "" : location.getId() + " - " + location.getAddress();
            }

            @Override
            public Location fromString(String string) {
                return null; // Não é necessário implementar isso aqui
            }
        });

        // Define o que acontece quando um local é selecionado
        idCB.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadLocationDetails(newVal);
            }
        });

        // Carrega todos os eventos disponíveis
        List<Event> events = localDAO.getAllEvents();
        ObservableList<Event> eventObservableList = FXCollections.observableArrayList(events);
        EventoCB.setItems(eventObservableList);

        // Configura como o ComboBox mostra os itens
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

        // Configura o item do botão do ComboBox
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
    }



    /**
     * Este método carrega os detalhes de um local selecionado nos campos de texto.
     *
     * @param location O local cujos detalhes devem ser carregados.
     */
    private void loadLocationDetails(Location location) {
        // Preenche os campos de texto com os valores do local
        addressTF.setText(location.getAddress());
        cityTF.setText(location.getCity());
        capacityTF.setText(String.valueOf(location.getCapacity()));
        yearBuiltTF.setText(String.valueOf(location.getYearBuilt()));

        // Preenche o ComboBox de tipo
        typeCB.setValue(location.getType()); // Assume que o valor do tipo é compatível com o ComboBox

        // Seleciona o evento no ComboBox EventoCB
        for (Event event : EventoCB.getItems()) {
            if (event.getId() == location.getEventId()) {
                EventoCB.setValue(event);
                break;
            }
        }

        // Armazena os valores originais para verificar se houve alteração
        originalAddress = location.getAddress();
        originalCity = location.getCity();
        originalCapacity = location.getCapacity();
        originalYearBuilt = location.getYearBuilt();
        originalType = location.getType();  // Aqui, o valor do tipo é armazenado apenas uma vez
        eventoselected = location.getEventId();
    }

    /**
     * Este método é chamado quando o botão "Editar" é pressionado.
     * Ele valida os campos e, se houver alterações, atualiza o local na base de dados.
     *
     * @param event O evento do botão "Editar".
     */
    @FXML
    void handleBtnEdit(ActionEvent event) {
        Location selectedLocation = idCB.getValue();
        if (selectedLocation == null) {
            showAlert(AlertType.INFORMATION, "Nenhum local selecionado", "Selecione um local");
            return;
        }

        // Obtém o tipo selecionado no ComboBox typeCB
        String selectedType = typeCB.getValue();
        if (selectedType == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um tipo.");
            return;
        }

       // Verifica se houve alteração nos campos
        if (!isAnyFieldChanged()) {
            showAlert(AlertType.INFORMATION, "Nenhuma alteração", "Nenhum campo foi alterado.");
            return;
        }

        // Valida os campos antes de atualizar
        if (!isInputValid()) {
            return; // Se a validação falhar, retorna e não executa a atualização
        }

        // Recupera o evento selecionado
        Event selectedEvent = EventoCB.getValue();
        if (selectedEvent == null) {
            showAlert(AlertType.WARNING, "Aviso", "Selecione um evento.");
            return;
        }

        // Cria o objeto Location com os dados atualizados
        Location updatedLocation = new Location(
                selectedLocation.getId(),
                addressTF.getText(),
                cityTF.getText(),
                Integer.parseInt(capacityTF.getText()),
                Integer.parseInt(yearBuiltTF.getText()),
                false, // Supondo que a flag "inativo" seja falsa para locais ativos
                selectedType, // Passando o tipo selecionado do ComboBox
                selectedEvent.getId() // Define o ID do evento
        );

        try {
            // Chama o método de atualização da DAO
            boolean isUpdated = localDAO.updateLocation(updatedLocation);

            if (isUpdated) {
                showAlert(AlertType.INFORMATION, "Sucesso", "Local atualizado com sucesso!");
                clearFields();
                atualizarLocal();
            } else {
                showAlert(AlertType.ERROR, "Erro", "Falha ao atualizar o local.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    /**
     * Exibe um alerta ao usuário com o tipo, título e mensagem fornecidos.
     *
     * @param alertType Tipo de alerta (INFORMATION, WARNING, ERROR).
     * @param title     Título do alerta.
     * @param message   Mensagem do alerta.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Este método é chamado quando o botão "Menu" é pressionado para retornar ao menu principal.
     *
     * @param event O evento do botão "Menu".
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu(); // Método para carregar o menu principal
    }

    /**
     * Atualiza o ChoiceBox com os locais mais recentes da base de dados.
     */
    private void atualizarLocal() {
        ObservableList<Location> updateLocalList = localDAO.getLocationsFromDatabase();
        idCB.setItems(updateLocalList);

    }

    /**
     * Limpa todos os campos de entrada.
     */
    private void clearFields() {
        addressTF.clear();
        cityTF.clear();
        capacityTF.clear();
        yearBuiltTF.clear();
        idCB.getSelectionModel().clearSelection(); // Limpa a Combo
        EventoCB.getSelectionModel().clearSelection(); // Limpa a choice
        typeCB.getSelectionModel().clearSelection();// limpa a Combo
    }

    /**
     * Valida os campos de entrada antes de permitir a atualização do local.
     *
     * @return true se todos os campos forem válidos, caso contrário false.
     */
    private boolean isInputValid() {
        String address = addressTF.getText().trim();
        String city = cityTF.getText().trim();
        String capacityStr = capacityTF.getText().trim();
        String yearBuiltStr = yearBuiltTF.getText().trim();

        // Verifica se todos os campos estão preenchidos
        if (address.isEmpty() || city.isEmpty() || capacityStr.isEmpty() || yearBuiltStr.isEmpty()) {
            showAlert(AlertType.WARNING, "Aviso", "Todos os campos devem ser preenchidos.");
            return false;
        }

        // Valida a cidade
        if (!city.matches("[a-zA-ZÀ-ÿ\\s]+") || city.length() < 2 || city.length() > 50) {
            showAlert(AlertType.WARNING, "Aviso", "A cidade deve conter apenas letras e espaços, e ter entre 2 e 50 caracteres.");
            return false;
        }

        // Valida a capacidade
        try {
            int capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                showAlert(AlertType.WARNING, "Aviso", "A capacidade deve ser um 0 ou um número maior que 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Aviso", "A capacidade deve ser um número válido.");
            return false;
        }

        // Valida o ano de construção
        try {
            int yearBuilt = Integer.parseInt(yearBuiltStr);

            // Obtém o ano atual
            int currentYear = LocalDate.now().getYear(); // Obtém o ano atual (ex: 2024)

            // Verifica se o ano de construção é válido (não pode ser no futuro ou ser zero)
            if ( yearBuilt > currentYear) {
                showAlert(AlertType.WARNING, "Aviso", "O ano de construção não pode ser no futuro.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Aviso", "O ano de construção deve ser um número válido.");
            return false;
        }


        return true;
    }

    /**
     * Verifica se algum campo foi alterado em relação aos valores originais.
     *
     * @return true se algum campo foi alterado, caso contrário false.
     */
    private boolean isAnyFieldChanged() {
        // Comparações detalhadas para verificar se algum campo foi alterado
        boolean isAddressChanged = !trimToEmpty(addressTF.getText()).equals(trimToEmpty(originalAddress));
        boolean isCityChanged = !trimToEmpty(cityTF.getText()).equals(trimToEmpty(originalCity));
        boolean isCapacityChanged = !trimToEmpty(capacityTF.getText()).equals(trimToEmpty(String.valueOf(originalCapacity)));
        boolean isYearBuiltChanged = !trimToEmpty(yearBuiltTF.getText()).equals(trimToEmpty(String.valueOf(originalYearBuilt)));
        boolean isTypeChanged = isComboBoxChanged(typeCB, originalType);
        boolean isEventChanged = isComboBoxChanged(EventoCB, eventoselected);

        // Se algum campo foi alterado, retorna true, caso contrário false
        return isAddressChanged || isCityChanged || isCapacityChanged || isYearBuiltChanged || isTypeChanged || isEventChanged;
    }

    // Método que verifica se o valor de um ComboBox foi alterado em relação ao valor original
    private boolean isComboBoxChanged(ComboBox<?> comboBox, Object originalValue) {
        // Verifica se o valor do ComboBox foi alterado em relação ao valor original
        if (comboBox.getValue() == null) {
            return originalValue != null; // Retorna true se o valor original não for nulo
        }

        // Para o ComboBox de Evento, verificamos o ID do evento
        if (comboBox.getValue() instanceof Event && originalValue instanceof Integer) {
            // Compara o ID do evento (um valor simples)
            Event selectedEvent = (Event) comboBox.getValue();
            return selectedEvent.getId() != (int) originalValue;  // Compara o ID
        }

        // Para os outros ComboBoxes, comparamos diretamente o valor
        return !comboBox.getValue().equals(originalValue);
    }

    // Método auxiliar para remover espaços extras e tratar valores nulos
    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLocalMenu();  // Carrega o menu principal
    }
}
