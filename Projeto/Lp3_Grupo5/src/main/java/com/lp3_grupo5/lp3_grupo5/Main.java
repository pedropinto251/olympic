package com.lp3_grupo5.lp3_grupo5;

import com.lp3_grupo5.lp3_grupo5.Controller.Login.UserEncryption;
import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.OlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Model.TeamOlympicParticipation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class Main extends Application {

    private static Stage mainStage; // Para reutilizar o Stage em qualquer parte da aplicação

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        loadScene("/com/lp3_grupo5/lp3_grupo5/View/LoginFXML/Login.fxml");
    }

    /**
     * Método para carregar uma nova cena no `Stage` principal.
     *
     * @param fxmlPath Caminho do arquivo FXML para carregar.
     */
    public static void loadScene(String fxmlPath) {
        try {
            // Carrega o arquivo FXML
            URL resource = Main.class.getResource(fxmlPath);
            if (resource == null) {
                System.out.println("Arquivo FXML não encontrado: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            // Define nova cena
            Scene scene = new Scene(root);
            mainStage.setScene(scene);

            // Exibe o Stage
            mainStage.setTitle("OPorto Olympics");

            // Centraliza o Stage na tela (tanto em X quanto em Y)
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            mainStage.setX((screenBounds.getWidth() - mainStage.getWidth()) / 2); // Centraliza no eixo X
            mainStage.setY((screenBounds.getHeight() - mainStage.getHeight()) / 2); // Centraliza no eixo Y

            // Exibe a janela
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Cria uma instância de UserEncryptionService e chama o método encryptPasswords
        UserEncryption encryptionService = new UserEncryption();
        encryptionService.encryptPasswords();

        launch(args);
        AthleteDAO dao = new AthleteDAO();
        List<TeamOlympicParticipation> participations = dao.getAllTeamParticipations();
        List<OlympicParticipation> individualParticipations = dao.getAllOlympicParticipations();
    }
}
