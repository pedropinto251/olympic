package com.lp3_grupo5.lp3_grupo5.Controller.Team;

import com.lp3_grupo5.lp3_grupo5.Controller.XML.XmlValidator;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ITeamDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Team;
import com.lp3_grupo5.lp3_grupo5.Model.TeamOlympicParticipation;
import com.lp3_grupo5.lp3_grupo5.Model.User;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TeamFXMLController {

    @FXML
    private Button xmlButton; // Botão para selecionar arquivo XML
    @FXML
    private Button xsdButton; // Botão para selecionar arquivo XSD
    @FXML
    private Button carregarButton; // Botão para carregar equipes do XML
    @FXML
    private Button menuBtn; // Botão para voltar ao menu principal
    @FXML
    private TableView<Team> teamTableView; // Tabela para exibir equipes
    @FXML
    private TableColumn<Team, String> nameColumn; // Coluna para nome
    @FXML
    private TableColumn<Team, String> countryColumn; // Coluna para país
    @FXML
    private TableColumn<Team, String> genreColumn; // Coluna para gênero
    @FXML
    private TableColumn<Team, String> sportColumn; // Coluna para esporte
    @FXML
    private TableColumn<Team, Integer> foundationYearColumn; // Coluna para ano de fundação
    @FXML
    private TextArea statusArea; // Área de texto para mensagens de status
    @FXML
    private Button btnBack;
    private ObservableList<Team> teamData = FXCollections.observableArrayList(); // Lista observável para a tabela

    private File selectedXmlFile; // Arquivo XML selecionado
    private File selectedXsdFile; // Arquivo XSD selecionado

    private ITeamDAO teamDAO; // Instância do DAO para operações de banco de dados

    public TeamFXMLController() {
        this.teamDAO = new TeamDAO();
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        sportColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSport()));
        foundationYearColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoundationYear()));

        teamTableView.setItems(teamData);
    }

    @FXML
    public void handleSelectXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        selectedXmlFile = fileChooser.showOpenDialog(new Stage());
        if (selectedXmlFile != null) {
            statusArea.appendText("Arquivo XML selecionado: " + selectedXmlFile.getName() + "\n");
        }
    }

    @FXML
    public void handleSelectXSD() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSD Files", "*.xml"));
        selectedXsdFile = fileChooser.showOpenDialog(new Stage());
        if (selectedXsdFile != null) {
            statusArea.appendText("Arquivo XSD selecionado: " + selectedXsdFile.getName() + "\n");
        }
    }

    @FXML
    public void handleCarregarEquipes() {
        if (selectedXmlFile == null || selectedXsdFile == null) {
            statusArea.appendText("Por favor, selecione arquivos XML e XSD válidos.\n");
            return;
        }

        String xmlPath = selectedXmlFile.getAbsolutePath();
        String xsdPath = selectedXsdFile.getAbsolutePath();

        if (XmlValidator.validateXmlSchema(xmlPath, xsdPath)) {
            statusArea.appendText("XML é válido de acordo com o XSD.\n");
            List<Team> teams = loadTeamsFromXml(xmlPath);
            if (teams != null) {
                teamData.clear();
                teamData.addAll(teams);
                statusArea.appendText("Pré-visualização das equipes carregada com sucesso.\n");
                teamDAO.insertOrUpdateTeams(teams);
                statusArea.appendText("Equipes e participações inseridas/atualizadas com sucesso na base de dados.\n");
                saveXmlWithNewName(xmlPath, selectedXmlFile.getName());
            } else {
                statusArea.appendText("Erro ao carregar equipes do XML.\n");
            }
        } else {
            statusArea.appendText("XML inválido.\n");
        }
    }

    private void saveXmlWithNewName(String xmlPath, String xmlFileName) {
        User loggedUser = Session.getUser();
        if (loggedUser == null) {
            statusArea.appendText("Erro: Utilizador não está logado.\n");
            return;
        }

        String username = loggedUser.getUsername();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);

        String newFileName = xmlFileName.replace(".xml", "") + "_" + username + "_" + timestamp + ".xml";
        String newFolderPath = "src/main/java/com/lp3_grupo5/lp3_grupo5/Infrastructure/XML-Carregados/";
        File newFolder = new File(newFolderPath);
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        Path sourcePath = Path.of(xmlPath);
        Path destinationPath = Path.of(newFolderPath + newFileName);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            statusArea.appendText("Arquivo XML salvo com o novo nome: " + newFileName + "\n");
        } catch (IOException e) {
            statusArea.appendText("Erro ao salvar o arquivo XML: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleVoltarMenu() {
        Stage currentStage = (Stage) menuBtn.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    public static List<Team> loadTeamsFromXml(String xmlFilePath) {
        List<Team> teams = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("team");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Team team = new Team();
                    team.setName(getTagValue("name", element));
                    team.setCountry(getTagValue("country", element));
                    team.setGenre(getTagValue("genre", element));
                    team.setSport(getTagValue("sport", element));
                    team.setFoundationYear(Integer.parseInt(getTagValue("foundationYear", element)));

                    // Carregar participações olímpicas
                    List<TeamOlympicParticipation> participations = new ArrayList<>();
                    NodeList participationList = element.getElementsByTagName("participation");
                    for (int j = 0; j < participationList.getLength(); j++) {
                        Element participationElement = (Element) participationList.item(j);
                        TeamOlympicParticipation participation = new TeamOlympicParticipation();
                        participation.setYear(Integer.parseInt(getTagValue("year", participationElement)));
                        participation.setResult(getTagValue("result", participationElement)); // Lê o resultado
                        participations.add(participation);
                    }
                    team.setOlympicParticipations(participations);
                    teams.add(team);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Mostra a stack trace em caso de erro
        }
        return teams; // Retorna a lista de equipes carregadas
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0).getChildNodes().item(0);
            return node != null ? node.getNodeValue() : null;
        }
        return null; // Retorna null se a tag não for encontrada
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadXMLMenu();  // Carrega o menu principal
    }
}