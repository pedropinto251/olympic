package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IAthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AthleteEditPhotoController {

    private IAthleteDAO athleteDAO = new AthleteDAO();

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal.

    @FXML
    private Button btnEdt; // Botão para confirmar a edição.

    @FXML
    private ImageView athleteImageView; // Visualização da imagem do atleta.

    private File athleteImageFile; // Arquivo da nova imagem do atleta.

    private Athlete currentAthlete; // Atleta da sessão atual.

    /**
     * Método de inicialização. Carrega os dados do atleta atual.
     */
    @FXML
    public void initialize() {
        int loggedUserId = Session.getLoggedUserId();

        try {
            currentAthlete = athleteDAO.getAthleteById(loggedUserId);
            if (currentAthlete != null) {
                loadAthleteImage(currentAthlete.getPhoto());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Atleta não encontrado.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar os dados do atleta: " + e.getMessage());
        }
    }

    /**
     * Carrega a imagem do atleta no ImageView.
     *
     * @param imageName O nome do arquivo da imagem.
     */
    private void loadAthleteImage(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            String imagePath = "src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/" + imageName;
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                athleteImageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                athleteImageView.setImage(null);
            }
        }
    }

    /**
     * Ação para carregar uma nova foto.
     */
    @FXML
    private void handleUploadAthlete() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Atleta");
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
     * Ação para salvar a nova foto no banco de dados.
     */
    @FXML
    private void handleBtnEdit() {
        if (athleteImageFile == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma imagem foi selecionada.");
            return;
        }

        try {
            String extension = getExtension(athleteImageFile);
            if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Somente arquivos JPG, PNG ou JPEG são permitidos.");
                return;
            }

            String newImageName = currentAthlete.getName().replaceAll("\\s+", "") + extension;
            File newImageFile = new File("src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/" + newImageName);

            Files.copy(athleteImageFile.toPath(), newImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            currentAthlete.setPhoto(newImageName);
            athleteDAO.updateAthlete(currentAthlete);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Foto do atleta atualizada com sucesso!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar a foto: " + e.getMessage());
        }
    }

    /**
     * Obtém a extensão do arquivo.
     *
     * @param file O arquivo a ser verificado.
     * @return A extensão do arquivo.
     */
    private String getExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf(".");
        return lastDot == -1 ? "" : name.substring(lastDot);
    }

    /**
     * Exibe uma janela de alerta.
     *
     * @param alertType O tipo de alerta.
     * @param title     O título da mensagem.
     * @param content   O conteúdo da mensagem.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Retorna ao menu principal.
     *
     * @param event Evento do botão.
     */
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadViewMenuInicialAtleta();
    }
}
