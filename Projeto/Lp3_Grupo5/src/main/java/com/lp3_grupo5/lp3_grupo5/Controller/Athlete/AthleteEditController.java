package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IAthleteDAO;
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
 * Controller responsável pela edição de atletas.
 * Gerencia a interação entre a interface gráfica (FXML) e o banco de dados.
 */
public class AthleteEditController {

    /**
     * Instância do DAO utilizada para realizar operações de banco de dados relacionadas ao atleta.
     */
    private IAthleteDAO athleteDAO = new AthleteDAO();

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal.

    @FXML
    private ChoiceBox<Athlete> athleteSelect;// Campo de texto para o ID do atleta.

    @FXML
    private TextField nameField; // Campo de texto para o nome do atleta.

    @FXML
    private ChoiceBox<Country> countrySelect; // ComboBox para selecionar o país do atleta.

    @FXML
    private ChoiceBox<String> genreSelect; // ComboBox para selecionar o gênero do atleta.

    @FXML
    private DatePicker dobPicker; // DatePicker para selecionar a data de nascimento do atleta.

    @FXML
    private TextField heightField; // Campo de texto para a altura do atleta.

    @FXML
    private TextField weightField; // Campo de texto para o peso do atleta.

    @FXML
    private ImageView athleteImageView; // Visualização da imagem do mascote.

    private File athleteImageFile; // Arquivo da imagem do mascote.

    @FXML
    private Button btnEdit; // Botão para confirmar a edição do atleta.

    @FXML
    private Button btnBack;

    /**
     * Método de inicialização do controlador.
     * Configura valores padrão para os componentes da interface.
     */
    @FXML
    public void initialize() {
        populateCountrySelect();
        populateGenreSelect();
        populateAthleteSelect();

        // Adiciona listener para atualizar campos ao selecionar um atleta
        athleteSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillAthleteDetails(newValue);
            }
        });
    }

    /**
     * Preenche a lista de atletas disponíveis no `ChoiceBox`.
     */
    private void populateAthleteSelect() {
        ObservableList<Athlete> athletes = observableArrayList(athleteDAO.loadAll());
        athleteSelect.setItems(athletes);
    }

    /**
     * Preenche os campos de edição com os detalhes do atleta selecionado.
     *
     * @param athlete O atleta selecionado.
     */
    private void fillAthleteDetails(Athlete athlete) {
        nameField.setText(athlete.getName());
        countrySelect.getSelectionModel().select(findCountryByCode(athlete.getCountry()));
        genreSelect.getSelectionModel().select(athlete.getGenre());
        dobPicker.setValue(LocalDate.parse(athlete.getDateOfBirth()));
        heightField.setText(String.valueOf(athlete.getHeight()));
        weightField.setText(String.valueOf(athlete.getWeight()));

        // Carrega a imagem, caso exista
        String imagePath = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/" + athlete.getPhoto();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            athleteImageView.setImage(new Image(imageFile.toURI().toString()));
        } else {
            athleteImageView.setImage(null); // Remove a imagem, se não encontrada
        }
    }

    /**
     * Encontra um país pelo código no `ChoiceBox` de países.
     *
     * @param countryCode O código do país.
     * @return O país correspondente ou `null` se não encontrado.
     */
    private Country findCountryByCode(String countryCode) {
        for (Country country : countrySelect.getItems()) {
            if (country.getCode().equals(countryCode)) {
                return country;
            }
        }
        return null;
    }

    /**
     * Preenche a ComboBox de países com os valores obtidos do banco de dados.
     */
    private void populateCountrySelect() {
        Country[] countriesArray = CountryEnum.EMPTY.getAllCountriesFromDatabase();
        ObservableList<Country> countriesList = observableArrayList(countriesArray);
        countrySelect.setItems(countriesList);
    }

    /**
     * Preenche a ComboBox de gêneros com os valores definidos no enum {@link GenreEnum}.
     */
    private void populateGenreSelect() {
        for (GenreEnum genre : GenreEnum.values()) {
            genreSelect.getItems().add(genre.getDescription());
        }
    }

    /**
     * Método chamado ao pressionar o botão "Editar".
     * Atualiza os dados do atleta no banco de dados com as informações fornecidas.
     */
    @FXML
    private void handleBtnEdit() {
        try {
            Athlete selectedAthlete = athleteSelect.getValue();
            if (selectedAthlete == null) {
                showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione um atleta.");
                return;
            }

            String name = nameField.getText().trim();
            Country selectedCountry = countrySelect.getValue();
            String genre = genreSelect.getValue();
            LocalDate dob = dobPicker.getValue();
            String heightText = heightField.getText().trim();
            String weightText = weightField.getText().trim();

            // Validação dos campos obrigatórios
            if (name.isEmpty() || selectedCountry == null || genre == null || dob == null || heightText.isEmpty() || weightText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campos Inválidos", "Por favor, preencha todos os campos corretamente.");
                return;
            }

            int height = Integer.parseInt(heightText);
            int weight = Integer.parseInt(weightText);

            // Determinar o nome da imagem a ser usada
            String newImageName;
            if (athleteImageFile != null) {
                // Caso uma nova imagem seja selecionada
                String extension = getExtension(athleteImageFile);
                if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Somente arquivos JPG, PNG ou JPEG são permitidos.");
                    return;
                }

                newImageName = name.replaceAll("\\s+", "") + extension;
                File newImageFile = new File("src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/" + newImageName);

                // Salvar a nova imagem
                if (!saveImageToFile(athleteImageFile, newImageFile)) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar a imagem do atleta.");
                    return;
                }
            } else {
                // Usar a imagem existente
                newImageName = selectedAthlete.getPhoto();
                if (newImageName == null || newImageName.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Você deve selecionar uma imagem para o atleta.");
                    return;
                }
            }

            // Atualizar os dados do atleta
            Athlete athlete = new Athlete(selectedAthlete.getId(), name, selectedCountry.getCode(), genre, height, weight, dob.toString(), false, newImageName);

            // Chamar o método updateAthleteTeste
            Athlete updatedAthlete = athleteDAO.updateAthlete(athlete);
            if (updatedAthlete != null) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Atleta atualizado com sucesso!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao atualizar o atleta.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Altura e peso devem ser números válidos.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Desconhecido", "Ocorreu um erro desconhecido: " + e.getMessage());
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

    // Adição de validação de tamanho no upload de imagens
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
     * Método chamado ao pressionar o botão "Menu".
     * Retorna ao menu principal carregando a interface correspondente.
     *
     * @param event Evento de ação gerado pelo botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Exibe uma janela de alerta para o usuário.
     *
     * @param alertType O tipo de alerta ({@link Alert.AlertType}).
     * @param title     O título da janela de alerta.
     * @param content   A mensagem a ser exibida no alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Limpa todos os campos do formulário de edição.
     */
    private void clearFields() {
        nameField.clear(); // Limpa o campo de texto do nome
        countrySelect.getSelectionModel().clearSelection(); // Desseleciona o país
        genreSelect.getSelectionModel().clearSelection(); // Desseleciona o gênero
        dobPicker.setValue(null); // Reseta a data de nascimento
        heightField.clear(); // Limpa o campo de altura
        weightField.clear(); // Limpa o campo de peso
        athleteImageView.setImage(null); // Remove a imagem do atleta
        athleteImageFile = null; // Remove a referência ao arquivo de imagem
        athleteSelect.getSelectionModel().clearSelection(); // Desseleciona o atleta
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLAthleteMenu();
    }
}
