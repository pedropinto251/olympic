package com.lp3_grupo5.lp3_grupo5.Controller.Event;

import com.lp3_grupo5.lp3_grupo5.DAO.EventDAO;
import com.lp3_grupo5.lp3_grupo5.Enums.CountryEnum;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.image.Image;
import java.io.File;
import java.time.LocalDate;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controlador para a interface de criação de eventos.
 * Responsável por gerenciar a lógica de criação de novos eventos, incluindo validação de dados,
 * comunicação com o banco de dados e navegação na interface.
 */
public class CreateEventController {

    @FXML
    private TextField yearField; // Campo de entrada para o ano do evento.

    @FXML
    private ChoiceBox<Country> countryCB; // Campo de seleção para o país anfitrião do evento.

    @FXML
    private ImageView mascotImageView; // Visualização da imagem do mascote.

    private File mascotImageFile; // Arquivo da imagem do mascote.

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenu; // Botão para voltar ao menu principal.

    @FXML
    private Button btnAddEvent; // Botão para adicionar um novo evento.

    @FXML
    private ChoiceBox<Event> idEventCB; // (não utilizado no método) Campo para seleção de eventos.

    /**
     * Método chamado ao clicar no botão "Adicionar Evento".
     * Lê os valores do formulário, valida os dados e tenta adicionar um novo evento no banco de dados.
     */
    @FXML
    private void handleAddEvent() {
        String yearText = yearField.getText();
        Country selectedCountry = countryCB.getValue();

        // Verifica se a imagem do mascote foi selecionada
        if (mascotImageFile == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Você deve selecionar uma imagem para o mascote.");
            return;
        }

        String mascot = mascotImageFile.getPath();
        String caminho = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/paises/";

        // Validação de campos obrigatórios
        if (yearText == null || yearText.isEmpty() ||
                selectedCountry == null ||
                mascot == null || mascot.isEmpty()) {
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

            // Verifica se já existe um evento no mesmo ano
            if (eventDAO.eventExistsByYear(year)) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Já existe um evento para o ano " + year + ".");
                return;
            }

            // Gerar novo nome para a imagem
            String newImageName = selectedCountry.getCode() + "EVT" + year + getExtension(mascotImageFile);
            File newImageFile = new File(caminho + newImageName);

            String extension = getExtension(mascotImageFile);
            if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Somente arquivos JPG, PNG ou JPEG são permitidos.");
                return;
            }

            // Salvar a imagem no diretório especificado
            if (!saveImageToFile(mascotImageFile, newImageFile)) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar a imagem do mascote.");
                return;
            }

            // Criação e salvamento do evento
            Event newEvent = new Event(0, year, selectedCountry.getCode(), newImageName, false);
            boolean success = eventDAO.addEvent(newEvent);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Evento adicionado com sucesso!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar evento.");
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
     * Exibe uma mensagem de alerta na interface.
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
     * Método chamado ao clicar no botão "Voltar".
     * Navega de volta para a tela do menu principal.
     */
    @FXML
    private void handleBackToMenu() {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    // Ajuste no método clearFields para limpar a imagem do mascote
    private void clearFields() {
        yearField.clear();
        mascotImageView.setImage(null); // Limpa a visualização da imagem
        countryCB.getSelectionModel().clearSelection();
    }

    // Adição de validação de tamanho no upload de imagens
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
     * Inicializa os elementos do controlador.
     * Configura a lista de países no `ChoiceBox` e define o conversor para exibir nomes de países.
     */
    public void initialize() {
        populateChoiceBoxWithCountries();

        // Define o conversor para exibir nomes de países no ChoiceBox
        countryCB.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country == null ? "" : country.getName();
            }

            @Override
            public Country fromString(String string) {
                return null; // Conversão reversa não necessária
            }
        });
    }

    /**
     * Preenche o `ChoiceBox` de países com dados obtidos do banco de dados.
     */
    private void populateChoiceBoxWithCountries() {
        Country[] countriesArray = CountryEnum.EMPTY.getAllCountriesFromDatabase();
        ObservableList<Country> countriesList = observableArrayList(countriesArray);
        countryCB.setItems(countriesList);
    }

    @FXML
    public void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLEventMenu();
    }
}
