package com.lp3_grupo5.lp3_grupo5.Util;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileDownloadController {

    @FXML
    private TableView<FileItem> fileTableView; // Tabela de arquivos
    @FXML
    private TableColumn<FileItem, String> fileNameColumn; // Coluna para nome do arquivo
    @FXML
    private Button downloadButton; // Botão para baixar arquivo
    @FXML
    private TextArea statusArea; // Área de texto para mensagens de status
    @FXML
    private Button btnMenu;
    @FXML
    private Button btnBack;

    private ObservableList<FileItem> fileList = FXCollections.observableArrayList(); // Lista observável de arquivos

    @FXML
    public void initialize() {
        loadFiles();
        fileTableView.setItems(fileList);

        // Configurar colunas da tabela
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());

        // Adicionar ícone ao botão
        //FontIcon downloadIcon = new FontIcon("fas-download"); // A Usar o prefixo 'fas' para FontAwesome Solid
        //downloadButton.setGraphic(downloadIcon);
    }

    private void loadFiles() {
        String folderPath = "src/main/java/com/lp3_grupo5/lp3_grupo5/Infrastructure/XML-Carregados";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(new FileItem(file.getName()));
                }
            }
        }
    }

    @FXML
    public void handleDownloadFile() {
        FileItem selectedFile = fileTableView.getSelectionModel().getSelectedItem();
        if (selectedFile == null) {
            statusArea.appendText("Por favor, selecione um arquivo para baixar.\n");
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecione o diretório de destino");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            String sourcePath = "src/main/java/com/lp3_grupo5/lp3_grupo5/Infrastructure/XML-Carregados/" + selectedFile.getFileName();
            String destinationPath = selectedDirectory.getAbsolutePath() + "/" + selectedFile.getFileName();

            try {
                Files.copy(Path.of(sourcePath), Path.of(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                statusArea.appendText("Arquivo " + selectedFile.getFileName() + " baixado com sucesso para " + destinationPath + "\n");
            } catch (IOException e) {
                statusArea.appendText("Erro ao baixar o arquivo: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadXMLMenu();  // Carrega o menu principal
    }

    public static class FileItem {
        private final SimpleStringProperty fileName;

        public FileItem(String fileName) {
            this.fileName = new SimpleStringProperty(fileName);
        }

        public String getFileName() {
            return fileName.get();
        }

        public SimpleStringProperty fileNameProperty() {
            return fileName;
        }
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
}