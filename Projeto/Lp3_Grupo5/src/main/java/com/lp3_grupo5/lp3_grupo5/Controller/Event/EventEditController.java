package com.lp3_grupo5.lp3_grupo5.Controller.Event;

import com.lp3_grupo5.lp3_grupo5.DAO.EventDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.LocalDAO;
import com.lp3_grupo5.lp3_grupo5.Enums.CountryEnum;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controlador para a interface de edição de eventos.
 * Permite editar informações de eventos existentes, incluindo ano, país e mascote.
 */
public class EventEditController {

    @FXML
    private TextField yearTF; // Campo de texto para entrada do ano do evento.

    @FXML
    private ChoiceBox<Country> idCountryCB; // Campo de seleção para o país do evento.

    @FXML
    private ImageView mascotImageView; // Visualização da imagem do mascote.

    private File mascotImageFile; // Arquivo da imagem do mascote.

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal.

    @FXML
    private Button btnBack;

    @FXML
    private Button btnEdit; // Botão para confirmar a edição do evento.

    @FXML
    private ChoiceBox<Event> idEventCB; // Campo de seleção para o evento que será editado.

    /**
     * Método chamado ao clicar no botão "Editar".
     * Atualiza as informações do evento selecionado com os dados fornecidos no formulário.
     */
    @FXML
    private void handleBtnEdit() {
        String yearText = yearTF.getText();
        Country selectedCountry = idCountryCB.getValue();
        Event selectedEvent = idEventCB.getValue();

        // Validações de campos obrigatórios
        if (selectedEvent == null || yearText == null || yearText.isEmpty() || selectedCountry == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Todos os campos devem ser preenchidos.");
            return;
        }

        try {
            int year = Integer.parseInt(yearText);
            int currentYear = LocalDate.now().getYear();

            // Validação do ano
            if (year < currentYear) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O ano não pode ser anterior ao atual.");
                return;
            }

            EventDAO eventDAO = new EventDAO();

            // Verifica se já existe um evento para o mesmo ano (diferente do selecionado)
            if (eventDAO.eventExistsByYear(year) && year != selectedEvent.getYear()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Já existe um evento para o ano " + year + ".");
                return;
            }

            // Determinar qual imagem usar
            String mascot;
            String caminho = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/paises/";
            if (mascotImageFile != null) {
                // Nova imagem foi carregada
                String extension = getExtension(mascotImageFile);
                if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Somente arquivos JPG, PNG ou JPEG são permitidos.");
                    return;
                }

                // Gerar novo nome para a imagem
                String newImageName = selectedCountry.getCode() + "EVT" + year + extension;
                File newImageFile = new File(caminho + newImageName);

                // Salvar a nova imagem
                if (!saveImageToFile(mascotImageFile, newImageFile)) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar a imagem do mascote.");
                    return;
                }
                mascot = newImageName;
            } else if (selectedEvent.getMascot() != null && !selectedEvent.getMascot().isEmpty()) {
                // Usar a imagem já existente no evento
                mascot = selectedEvent.getMascot();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Você deve selecionar uma imagem para o mascote.");
                return;
            }

            // Atualiza os dados do evento
            selectedEvent.setYear(year);
            selectedEvent.setCountry(selectedCountry.getCode());
            selectedEvent.setMascot(mascot);

            // Salva as alterações no banco de dados
            boolean success = eventDAO.updateEvent(selectedEvent);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Evento atualizado com sucesso!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar evento.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "O ano deve ser um número válido.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao processar o evento: " + e.getMessage());
        }
    }


    /**
     * Obtém a extensão do arquivo a partir do nome.
     */
    private String getExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf(".");
        return lastDot == -1 ? "" : name.substring(lastDot);
    }

    /**
     * Copia o arquivo de imagem para o novo local.
     */
    private boolean saveImageToFile(File source, File destination) {
        try {
            java.nio.file.Files.copy(source.toPath(), destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Exibe uma mensagem de alerta ao usuário.
     *
     * @param alertType Tipo do alerta (e.g., ERROR, INFORMATION).
     * @param title     Título do alerta.
     * @param message   Mensagem de conteúdo do alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Método chamado ao clicar no botão "Menu".
     * Navega de volta para a tela do menu principal.
     */
    @FXML
    private void handleBtnMenu() {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Limpa os campos do formulário de edição.
     * Restaura os valores dos campos para o estado inicial.
     */
    private void clearFields() {
        yearTF.clear();
        mascotImageView.setImage(null); // Limpa a visualização da imagem
        idCountryCB.getSelectionModel().clearSelection();
        idEventCB.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUploadMascot() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Mascote");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) { // Limite de 2 MB
                showAlert(Alert.AlertType.ERROR, "Erro", "O arquivo é muito grande. Limite de 2 MB.");
                return;
            }
            mascotImageFile = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            mascotImageView.setImage(image);
        }
    }

    /**
     * Método de inicialização chamado ao carregar a interface.
     * Preenche os `ChoiceBox` de países e eventos e configura os conversores para exibição.
     */
    public void initialize() {
        populateChoiceBoxWithCountries();
        populateChoiceBoxWithEvents();

        // Configura o conversor para exibir o nome do país no ChoiceBox
        idCountryCB.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country == null ? "" : country.getName();
            }

            @Override
            public Country fromString(String string) {
                return null; // Conversão reversa não necessária
            }
        });

        // Configura o conversor para exibir detalhes do evento no ChoiceBox
        idEventCB.setConverter(new StringConverter<Event>() {
            @Override
            public String toString(Event event) {
                return event == null ? "" : "Evento " + event.getYear();
            }

            @Override
            public Event fromString(String string) {
                return null; // Conversão reversa não necessária
            }
        });

        // Adiciona listener para atualizar os campos ao selecionar um evento
        idEventCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedEvent) -> {
            if (selectedEvent != null) {
                updateFieldsWithEvent(selectedEvent);
            }
        });
    }

    /**
     * Atualiza os campos do formulário com os dados do evento selecionado.
     *
     * @param event Evento selecionado.
     */
    private void updateFieldsWithEvent(Event event) {
        yearTF.setText(String.valueOf(event.getYear())); // Atualiza o ano
        Country selectedCountry = getCountryByCode(event.getCountry()); // Obtém o objeto Country pelo código
        idCountryCB.setValue(selectedCountry); // Atualiza o ChoiceBox de país

        // Atualiza a imagem do mascote
        if (event.getMascot() != null && !event.getMascot().isEmpty()) {
            String imagePath = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/paises/" + event.getMascot();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                mascotImageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                mascotImageView.setImage(null); // Limpa a imagem caso o arquivo não seja encontrado
            }
        } else {
            mascotImageView.setImage(null);
        }
    }

    /**
     * Obtém um objeto Country pelo código do país.
     *
     * @param code Código do país.
     * @return Objeto Country correspondente ou null se não encontrado.
     */
    private Country getCountryByCode(String code) {
        for (Country country : idCountryCB.getItems()) {
            if (country.getCode().equals(code)) {
                return country;
            }
        }
        return null; // Retorna null se o país não for encontrado
    }

    /**
     * Preenche o `ChoiceBox` de países com dados obtidos do banco de dados.
     */
    private void populateChoiceBoxWithCountries() {
        Country[] countriesArray = CountryEnum.EMPTY.getAllCountriesFromDatabase();
        ObservableList<Country> countriesList = FXCollections.observableArrayList(countriesArray);
        idCountryCB.setItems(countriesList);
    }

    /**
     * Preenche o `ChoiceBox` de eventos com dados obtidos do banco de dados.
     */
    private void populateChoiceBoxWithEvents() {
        EventDAO eventDAO = new EventDAO();
        ObservableList<Event> eventsList = eventDAO.loadAllEvents();
        idEventCB.setItems(eventsList);
    }

    @FXML
    private void handleBtnBack() {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLEventMenu();
    }
}
