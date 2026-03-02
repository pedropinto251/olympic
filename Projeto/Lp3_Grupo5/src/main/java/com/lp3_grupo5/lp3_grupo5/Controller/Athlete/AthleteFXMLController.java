package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.Controller.Login.UserEncryption;
import com.lp3_grupo5.lp3_grupo5.Controller.XML.XmlValidator;
import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IAthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
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

/**
 * Controller para manipulação de atletas via interface gráfica (FXML).
 * Permite carregar atletas de arquivos XML, validar com XSD e inserir no banco de dados.
 */
public class AthleteFXMLController {

    // FXML elements
    @FXML
    private Button xmlButton; // Botão para selecionar arquivo XML
    @FXML
    private Button xsdButton; // Botão para selecionar arquivo XSD
    @FXML
    private Button carregarButton; // Botão para carregar atletas do XML
    @FXML
    private Button menuBtn; // Botão para voltar ao menu principal
    @FXML
    private Button btnBack;
    @FXML
    private TableView<Athlete> athleteTableView; // Tabela para exibir atletas
    @FXML
    private TableColumn<Athlete, Integer> idColumn; // Coluna para ID
    @FXML
    private TableColumn<Athlete, String> nameColumn; // Coluna para nome
    @FXML
    private TableColumn<Athlete, String> countryColumn; // Coluna para país
    @FXML
    private TableColumn<Athlete, String> genreColumn; // Coluna para gênero
    @FXML
    private TableColumn<Athlete, Integer> heightColumn; // Coluna para altura
    @FXML
    private TableColumn<Athlete, Integer> weightColumn; // Coluna para peso
    @FXML
    private TableColumn<Athlete, String> dateOfBirthColumn; // Coluna para data de nascimento
    @FXML
    private TableColumn<Athlete, Boolean> inativeColumn; // Coluna para status de inatividade
    @FXML
    private TextArea statusArea; // Área de texto para mensagens de status

    private ObservableList<Athlete> athleteData = FXCollections.observableArrayList(); // Lista observável para a tabela

    private IAthleteDAO athleteDAO; // Instância do DAO para operações de banco de dados

    private File selectedXmlFile; // Arquivo XML selecionado
    private File selectedXsdFile; // Arquivo XSD selecionado

    /**
     * Construtor padrão.
     * Inicializa o DAO de atletas.
     */
    public AthleteFXMLController() {
        this.athleteDAO = new AthleteDAO();
    }

    /**
     * Inicializa o controlador e configura as colunas da tabela.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        heightColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHeight()));
        weightColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getWeight()));
        dateOfBirthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfBirth()));
        inativeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isInative()));

        athleteTableView.setItems(athleteData);
    }

    /**
     * Abre o explorador de arquivos para selecionar um arquivo XML.
     */
    @FXML
    public void handleSelectXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        selectedXmlFile = fileChooser.showOpenDialog(new Stage());
        if (selectedXmlFile != null) {
            statusArea.appendText("Arquivo XML selecionado: " + selectedXmlFile.getName() + "\n");
        }
    }

    /**
     * Abre o explorador de arquivos para selecionar um arquivo XSD.
     */
    @FXML
    public void handleSelectXSD() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSD Files", "*.xml"));
        selectedXsdFile = fileChooser.showOpenDialog(new Stage());
        if (selectedXsdFile != null) {
            statusArea.appendText("Arquivo XSD selecionado: " + selectedXsdFile.getName() + "\n");
        }
    }

    /**
     * Carrega atletas de um arquivo XML e os insere no banco de dados.
     */
    @FXML
    public void handleCarregarAtletas() {
        if (selectedXmlFile == null || selectedXsdFile == null) {
            statusArea.appendText("Por favor, selecione arquivos XML e XSD válidos.\n");
            return;
        }

        String xmlPath = selectedXmlFile.getAbsolutePath();
        String xsdPath = selectedXsdFile.getAbsolutePath();

        if (XmlValidator.validateXmlSchema(xmlPath, xsdPath)) {
            statusArea.appendText("XML é válido de acordo com o XSD.\n");
            List<Athlete> athletes = loadAthletesFromXml(xmlPath);
            if (athletes != null) {
                athleteData.clear();
                athleteData.addAll(athletes);
                statusArea.appendText("Pré-visualização dos atletas carregada com sucesso.\n");
                athleteDAO.insertOrUpdateAthletes(athletes);
                statusArea.appendText("Atletas e participações inseridos/atualizados com sucesso na base de dados.\n");

                // Chama o método encryptPasswords após inserir os atletas na base de dados
                UserEncryption encryptionService = new UserEncryption();
                encryptionService.encryptPasswords();
                // Salvar o arquivo XML carregado em outra pasta com o novo nome
                saveXmlWithNewName(xmlPath, selectedXmlFile.getName());
            } else {
                statusArea.appendText("Erro ao carregar atletas do XML.\n");
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

    /**
     * Retorna ao menu principal.
     */
    @FXML
    public void handleVoltarMenu() {
        Stage currentStage = (Stage) menuBtn.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Carrega atletas a partir de um arquivo XML.
     *
     * @param xmlFilePath Caminho do arquivo XML.
     * @return Lista de atletas carregados.
     */
    public static List<Athlete> loadAthletesFromXml(String xmlFilePath) {
        List<Athlete> athletes = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("athlete");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Athlete athlete = new Athlete();
                    athlete.setName(getTagValue("name", element));
                    athlete.setCountry(getTagValue("country", element));
                    athlete.setGenre(getTagValue("genre", element));
                    athlete.setHeight(Integer.parseInt(getTagValue("height", element)));
                    athlete.setWeight(Integer.parseInt(getTagValue("weight", element)));
                    athlete.setDateOfBirth(getTagValue("dateOfBirth", element));

                    NodeList participationList = element.getElementsByTagName("participation");
                    List<Athlete.Participation> participations = new ArrayList<>();
                    for (int j = 0; j < participationList.getLength(); j++) {
                        Element partElement = (Element) participationList.item(j);
                        Athlete.Participation participation = new Athlete.Participation();
                        participation.setYear(Integer.parseInt(getTagValue("year", partElement)));
                        participation.setGold(Integer.parseInt(getTagValue("gold", partElement)));
                        participation.setSilver(Integer.parseInt(getTagValue("silver", partElement)));
                        participation.setBronze(Integer.parseInt(getTagValue("bronze", partElement)));
                        participations.add(participation);
                    }
                    athlete.setOlympicParticipations(participations);
                    athletes.add(athlete);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return athletes;
    }

    /**
     * Obtém o valor de uma tag XML.
     *
     * @param tagName Nome da tag.
     * @param element Elemento XML.
     * @return Valor da tag.
     */
    private static String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        Node node = nodeList.item(0);
        return node != null ? node.getTextContent() : "";
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadXMLMenu();  // Carrega o menu principal
    }
}