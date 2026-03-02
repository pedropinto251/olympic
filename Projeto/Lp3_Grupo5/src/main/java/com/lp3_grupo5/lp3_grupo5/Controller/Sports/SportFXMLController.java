package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.Controller.XML.XmlValidator;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.ISportDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import com.lp3_grupo5.lp3_grupo5.Model.Rule;
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

public class SportFXMLController {

    @FXML
    private Button xmlButton; // Botão para selecionar arquivo XML
    @FXML
    private Button xsdButton; // Botão para selecionar arquivo XSD
    @FXML
    private Button carregarButton; // Botão para carregar desportos do XML
    @FXML
    private Button menuBtn; // Botão para voltar ao menu principal
    @FXML
    private TableView<Sport> sportTableView; // Tabela para exibir desportos
    @FXML
    private TableColumn<Sport, String> typeColumn; // Coluna para tipo
    @FXML
    private TableColumn<Sport, String> genreColumn; // Coluna para gênero
    @FXML
    private TableColumn<Sport, String> nameColumn; // Coluna para nome
    @FXML
    private TableColumn<Sport, String> descriptionColumn; // Coluna para descrição
    @FXML
    private TableColumn<Sport, Integer> minParticipantsColumn; // Coluna para participantes mínimos
    @FXML
    private TableColumn<Sport, String> scoringMeasureColumn; // Coluna para medida de pontuação
    @FXML
    private TableColumn<Sport, String> oneGameColumn; // Coluna para um jogo
    @FXML
    private TableColumn<Sport, String> olympicRecordColumn; // Coluna para recorde olímpico
    @FXML
    private TableColumn<Sport, String> winnerOlympicColumn; // Coluna para vencedor olímpico
    @FXML
    private TextArea statusArea; // Área de texto para mensagens de status
    @FXML
    private Button btnBack;

    private ObservableList<Sport> sportData = FXCollections.observableArrayList(); // Lista observável para a tabela

    private File selectedXmlFile; // Arquivo XML selecionado
    private File selectedXsdFile; // Arquivo XSD selecionado

    private ISportDAO sportDAO; // Instância do DAO para operações de banco de dados

    public SportFXMLController() {
        this.sportDAO = new SportDAO();
    }

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        minParticipantsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMinParticipants()));
        scoringMeasureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getScoringMeasure()));
        oneGameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOneGame()));
        olympicRecordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOlympicRecordTime() + " (" + cellData.getValue().getOlympicRecordYear() + ") - " + cellData.getValue().getOlympicRecordHolder()));
        winnerOlympicColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWinnerOlympicTime() + " (" + cellData.getValue().getWinnerOlympicYear() + ") - " + cellData.getValue().getWinnerOlympicHolder()));

        sportTableView.setItems(sportData);
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
    public void handleCarregarDesportos() {
        if (selectedXmlFile == null || selectedXsdFile == null) {
            statusArea.appendText("Por favor, selecione arquivos XML e XSD válidos.\n");
            return;
        }

        String xmlPath = selectedXmlFile.getAbsolutePath();
        String xsdPath = selectedXsdFile.getAbsolutePath();

        if (XmlValidator.validateXmlSchema(xmlPath, xsdPath)) {
            statusArea.appendText("XML é válido de acordo com o XSD.\n");
            List<Sport> sports = loadSportsFromXml(xmlPath);
            if (sports != null) {
                sportData.clear();
                sportData.addAll(sports);
                statusArea.appendText("Pré-visualização dos desportos carregada com sucesso.\n");
                sportDAO.insertOrUpdateSports(sports);
                statusArea.appendText("Desportos e regras inseridos/atualizados com sucesso na base de dados.\n");
                saveXmlWithNewName(xmlPath, selectedXmlFile.getName());
            } else {
                statusArea.appendText("Erro ao carregar desportos do XML.\n");
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



    public static List<Sport> loadSportsFromXml(String xmlFilePath) {
        List<Sport> sports = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("sport");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Sport sport = new Sport();
                    sport.setType(getTagValue("type", element));
                    sport.setGenre(getTagValue("genre", element));
                    sport.setName(getTagValue("name", element));
                    sport.setDescription(getTagValue("description", element));
                    sport.setMinParticipants(Integer.parseInt(getTagValue("minParticipants", element)));
                    sport.setScoringMeasure(getTagValue("scoringMeasure", element));
                    sport.setOneGame(getTagValue("oneGame", element));

                    // Ler dados do recorde olímpico
                    Element olympicRecordElement = (Element) element.getElementsByTagName("olympicRecord").item(0);
                    if (olympicRecordElement != null) {
                        sport.setOlympicRecordTime(getTagValue("time", olympicRecordElement));
                        sport.setOlympicRecordYear(Integer.parseInt(getTagValue("year", olympicRecordElement)));
                        sport.setOlympicRecordHolder(getTagValue("holder", olympicRecordElement));
                    }

                    // Ler dados do vencedor olímpico
                    Element winnerOlympicElement = (Element) element.getElementsByTagName("winnerOlympic").item(0);
                    if (winnerOlympicElement != null) {
                        sport.setWinnerOlympicTime(getTagValue("time", winnerOlympicElement));
                        sport.setWinnerOlympicYear(Integer.parseInt(getTagValue("year", winnerOlympicElement)));
                        sport.setWinnerOlympicHolder(getTagValue("holder", winnerOlympicElement));
                    }

                    // Ler regras associadas ao desporto
                    NodeList rulesList = element.getElementsByTagName("rules");
                    if (rulesList.getLength() > 0) {
                        Element rulesElement = (Element) rulesList.item(0);
                        NodeList ruleNodes = rulesElement.getElementsByTagName("rule");
                        List<Rule> rules = new ArrayList<>();
                        for (int j = 0; j < ruleNodes.getLength(); j++) {
                            Rule rule = new Rule();
                            rule.setRule(ruleNodes.item(j).getTextContent());
                            rules.add(rule);
                        }
                        sport.setRules(rules);
                    }

                    // Adiciona o desporto à lista
                    sports.add(sport);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Mostra a stack trace em caso de erro
        }
        return sports; // Retorna a lista de desportos carregados
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null && node.hasChildNodes()) {
                return node.getChildNodes().item(0).getNodeValue(); // Retorna o valor da tag
            }
        }
        return null; // Retorna null se a tag não for encontrada
    }

    @FXML
    public void handleVoltarMenu() {
        Stage currentStage = (Stage) menuBtn.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadXMLMenu();  // Carrega o menu principal
    }
}