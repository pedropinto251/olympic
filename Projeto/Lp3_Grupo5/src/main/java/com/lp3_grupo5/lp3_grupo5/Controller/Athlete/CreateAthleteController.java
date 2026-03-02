package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;
import com.lp3_grupo5.lp3_grupo5.Controller.Login.UserEncryption;
import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Enums.CountryEnum;
import com.lp3_grupo5.lp3_grupo5.Enums.GenreEnum;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Model.Country;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controlador responsável pela funcionalidade de criação de novos atletas.
 * Este controlador gerencia a interface gráfica de adição de atletas, incluindo validação
 * e envio de dados ao banco de dados por meio do `AthleteDAO`.
 */
public class CreateAthleteController {

    @FXML
    private Button btnAdd; // Botão para adicionar atleta

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal

    @FXML
    private Button btnBack;

    @FXML
    private ComboBox<Country> countrySelect; // Campo de seleção de país

    @FXML
    private DatePicker dobPicker; // Campo para seleção de data de nascimento

    @FXML
    private ComboBox<String> genreSelect; // Campo de seleção de gênero

    @FXML
    private TextField heightField; // Campo de entrada para altura

    @FXML
    private TextField nameField; // Campo de entrada para nome

    @FXML
    private TextField weightField; // Campo de entrada para peso

    @FXML
    private ImageView athleteImageView; // Visualização da imagem do mascote.

    private File athleteImageFile; // Arquivo da imagem do mascote.

    private final AthleteDAO athleteDAO = new AthleteDAO(); // Instância do DAO para interagir com o banco de dados

    /**
     * Inicializa o controlador configurando os valores padrão e os componentes gráficos.
     */
    @FXML
    public void initialize() {
        populateCountrySelect();
        populateGenreSelect();
    }

    /**
     * Preenche a ComboBox de países com dados obtidos do banco de dados.
     */
    private void populateCountrySelect() {
        Country[] countriesArray = CountryEnum.EMPTY.getAllCountriesFromDatabase();
        ObservableList<Country> countriesList = observableArrayList(countriesArray);
        countrySelect.setItems(countriesList);
    }

    /**
     * Preenche a ComboBox de gêneros com valores definidos no enum `GenreEnum`.
     */
    private void populateGenreSelect() {
        for (GenreEnum genre : GenreEnum.values()) {
            genreSelect.getItems().add(genre.getDescription());
        }
    }

    /**
     * Método acionado ao clicar no botão "Adicionar".
     * Lê os valores do formulário, valida os campos e insere o atleta no banco de dados.
     *
     * @param event Evento gerado pelo clique no botão.
     */
    @FXML
    void handleBtnAdd(ActionEvent event) {
        try {
            // Obtenção de valores do formulário
            String name = nameField.getText().trim();
            Country selectedCountry = countrySelect.getValue();
            String genre = genreSelect.getValue();
            LocalDate dob = dobPicker.getValue();
            String heightText = heightField.getText().trim();
            String weightText = weightField.getText().trim();

            // Verifica se a imagem do atleta foi selecionada
            if (athleteImageFile == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Você deve selecionar uma foto para o atleta.");
                return;
            }

            // Validação da idade
            if (dob == null || dob.isAfter(LocalDate.now().minusYears(18))) {
                showAlert(Alert.AlertType.WARNING, "Idade Inválida", "O atleta deve ter 18 anos ou mais.");
                return;
            }

            String athelete = athleteImageFile.getPath();
            String caminho = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/";

            // Validação dos campos
            if (name.isEmpty() || selectedCountry == null || genre == null || dob == null || heightText.isEmpty() || weightText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campos Inválidos", "Por favor, preencha todos os campos corretamente.");
                return;
            }

            int height = Integer.parseInt(heightText);
            int weight = Integer.parseInt(weightText);

            // Gerar novo nome para a imagem
            String newImageName = name.replaceAll("\\s+", "") + getExtension(athleteImageFile);
            File newImageFile = new File(caminho + newImageName);

            String extension = getExtension(athleteImageFile);
            if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Somente arquivos JPG, PNG ou JPEG são permitidos.");
                return;
            }

            // Salvar a imagem no diretório especificado
            if (!saveImageToFile(athleteImageFile, newImageFile)) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar a imagem do atleta.");
                return;
            }

            // Criação do objeto Athlete
            Athlete athlete = new Athlete();
            athlete.setName(name);
            athlete.setCountry(selectedCountry.getCode()); // Define o país pelo código
            athlete.setGenre(genre);
            athlete.setDateOfBirth(dob.toString());
            athlete.setHeight(height);
            athlete.setWeight(weight);
            athlete.setPhoto(newImageName);

            // Inserção no banco de dados usando addAthleteTeste
            boolean success = athleteDAO.addAthlete(athlete);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Atleta adicionado com sucesso!");
                clearFields();
                // Chama o método encryptPasswords após inserir o atleta na base de dados
                UserEncryption encryptionService = new UserEncryption();
                encryptionService.encryptPasswords();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao adicionar o atleta.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Altura e peso devem ser números válidos.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao acessar o banco de dados.");
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

    @FXML
    private void handleUploadAthlete() {
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
            athleteImageFile = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            athleteImageView.setImage(image);
        }
    }

    /**
     * Método acionado ao clicar no botão "Menu".
     * Fecha a janela atual e carrega a interface do menu principal.
     *
     * @param event Evento gerado pelo clique no botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Exibe uma mensagem de alerta na interface gráfica.
     *
     * @param alertType Tipo de alerta (e.g., WARNING, INFORMATION, ERROR).
     * @param title     Título do alerta.
     * @param content   Mensagem de conteúdo do alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Limpa os campos do formulário, redefinindo os valores para vazio ou padrão.
     */
    private void clearFields() {
        nameField.clear();
        countrySelect.getSelectionModel().clearSelection();
        genreSelect.getSelectionModel().clearSelection();
        dobPicker.setValue(null);
        heightField.clear();
        weightField.clear();
        athleteImageView.setImage(null);
        athleteImageFile = null;
    }
    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLAthleteMenu();
    }
}
