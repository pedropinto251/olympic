package com.lp3_grupo5.lp3_grupo5.Controller.Athlete;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Controlador para a interface de visualização e gestão de atletas.
 * Este controlador permite carregar, exibir, visualizar o histórico e excluir atletas.
 */
public class AthleteReadController {

    // FXML elements
    @FXML
    private TableView<Athlete> athleteTableView; // Tabela principal de atletas
    @FXML
    private TableColumn<Athlete, Integer> idColumn; // Coluna para IDs dos atletas
    @FXML
    private TableColumn<Athlete, String> nameColumn; // Coluna para nomes dos atletas
    @FXML
    private TableColumn<Athlete, String> photoColumn; // Coluna para fotos dos atletas
    @FXML
    private TableColumn<Athlete, String> countryColumn; // Coluna para países dos atletas
    @FXML
    private TableColumn<Athlete, String> genreColumn; // Coluna para gêneros dos atletas
    @FXML
    private TableColumn<Athlete, Integer> heightColumn; // Coluna para alturas dos atletas
    @FXML
    private TableColumn<Athlete, Integer> weightColumn; // Coluna para pesos dos atletas
    @FXML
    private TableColumn<Athlete, String> dobColumn; // Coluna para data de nascimento dos atletas
    @FXML
    private TableColumn<Athlete, String> inativeColumn; // Coluna para status de inatividade dos atletas

    @FXML
    private Button btnMenu; // Botão para retornar ao menu principal

    @FXML
    private Button btnBack;

    @FXML
    private TableView<Athlete.Participation> historyAthleteTableView; // Tabela de histórico de participações
    @FXML
    private TableColumn<Athlete, Integer> idColumnH; // Coluna para ID no histórico
    @FXML
    private TableColumn<Athlete, Integer> yearColumnH; // Coluna para o ano de participação no histórico
    @FXML
    private TableColumn<Athlete, Integer> goldColumnH; // Coluna para medalhas de ouro no histórico
    @FXML
    private TableColumn<Athlete, Integer> silverColumnH; // Coluna para medalhas de prata no histórico
    @FXML
    private TableColumn<Athlete, Integer> bronzeColumnH; // Coluna para medalhas de bronze no histórico
    @FXML
    private TableColumn<Athlete, Integer> diplomaColumnH; // Coluna para diplomas no histórico
    @FXML
    private TableColumn<Athlete, Integer> athleteIdColumnH; // Coluna para ID de atleta no histórico
    @FXML
    private TableColumn<Athlete, Integer> eventIdColumnH; // Coluna para ID de evento no histórico
    @FXML
    private TableColumn<Athlete, Boolean> inactiveColumnH; // Coluna para status de inatividade no histórico
    @FXML
    private TextField searchField; // Campo de pesquisa


    private final AthleteDAO athleteDAO = new AthleteDAO(); // Instância do DAO para acesso a dados

    /**
     * Método acionado quando o botão "Menu" é clicado.
     * Fecha a janela atual e carrega o menu principal.
     */
    @FXML
    public void handleBtnMenu() {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }

    /**
     * Método de inicialização do controlador.
     * Configura as colunas das tabelas e carrega os atletas a partir do DAO.
     */
    @FXML
    private void initialize() {
        // Configura colunas da tabela principal de atletas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        inativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));;

        photoColumn.setCellFactory(param -> new TableCell<Athlete, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String athletePath, boolean empty) {
                super.updateItem(athletePath, empty);
                if (empty || athletePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Adiciona um timestamp ao caminho para evitar o cache
                        File file = new File("src/main/resources/com/lp3_grupo5/lp3_grupo5/View/images/fotosAtletas/" + athletePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            imageView.setFitHeight(50); // Definir tamanho da imagem
                            imageView.setFitWidth(50);  // Definir tamanho da imagem
                            setGraphic(imageView);
                        } else {
                            System.out.println("Imagem não encontrada: " + athletePath);
                        }

                    } catch (Exception e) {
                        System.out.println("Erro ao carregar a imagem: " + e.getMessage());
                        setGraphic(null);
                    }
                }
            }
        });

        // Configura colunas da tabela de histórico de participações
        yearColumnH.setCellValueFactory(new PropertyValueFactory<>("year"));
        goldColumnH.setCellValueFactory(new PropertyValueFactory<>("gold"));
        silverColumnH.setCellValueFactory(new PropertyValueFactory<>("silver"));
        bronzeColumnH.setCellValueFactory(new PropertyValueFactory<>("bronze"));

        // Carrega os atletas na tabela principal
        athleteTableView.setItems(loadAthletes());
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase(); // Texto da pesquisa em minúsculas

        // Filtra a lista de atletas com base no nome, ID ou país
        ObservableList<Athlete> filteredList = FXCollections.observableArrayList();

        for (Athlete athlete : loadAthletes()) {  // "loadAthletes" carrega todos os atletas
            boolean matches = false;

            // Verifica se algum dos critérios corresponde ao texto de pesquisa
            if (athlete.getName().toLowerCase().contains(searchText)) {
                matches = true;
            } else if (String.valueOf(athlete.getId()).contains(searchText)) {
                matches = true;
            } else if (athlete.getCountry().toLowerCase().contains(searchText)) {
                matches = true;
            }

            // Se o atleta corresponder a qualquer critério, adiciona à lista filtrada
            if (matches) {
                filteredList.add(athlete);
            }
        }

        athleteTableView.setItems(filteredList);  // Atualiza a tabela com os atletas filtrados
    }

    /**
     * Carrega a lista de atletas do banco de dados.
     *
     * @return Uma lista observável de atletas carregada do DAO.
     */
    private ObservableList<Athlete> loadAthletes() {
        return athleteDAO.loadAll(); // Retorna os atletas usando o DAO
    }

    /**
     * Método acionado quando o botão "Excluir" é clicado.
     * Exclui o atleta selecionado da tabela e marca-o como excluído no banco de dados.
     *
     * @param event Evento gerado pelo clique no botão "Excluir".
     */
    @FXML
    void handleBtnDelete(ActionEvent event) {
        Athlete selectedAthlete = athleteTableView.getSelectionModel().getSelectedItem();

        if (selectedAthlete != null) {
            // Tenta excluir o atleta selecionado
            boolean success = athleteDAO.deleteAthlete(selectedAthlete.getId());
            if (success) {
                System.out.println("Atleta marcado como excluído com sucesso.");
                athleteTableView.getItems().remove(selectedAthlete); // Remove o atleta da tabela
            } else {
                // Exibe um alerta em caso de falha
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao excluir atleta");
                alert.setContentText("Ocorreu um erro ao tentar excluir o atleta.");
                alert.showAndWait();
            }
        } else {
            // Exibe um alerta se nenhum atleta for selecionado
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum atleta selecionado");
            alert.setContentText("Por favor, selecione um atleta antes de tentar excluí-lo.");
            alert.showAndWait();
        }
    }

    /**
     * Método acionado quando o botão "Histórico" é clicado.
     * Carrega e exibe o histórico de participações olímpicas do atleta selecionado.
     *
     * @param event Evento gerado pelo clique no botão "Histórico".
     */
    @FXML
    void handleBtnHistory(ActionEvent event) {
        Athlete selectedAthlete = athleteTableView.getSelectionModel().getSelectedItem();

        if (selectedAthlete != null) {
            // Busca as participações olímpicas do atleta
            List<Athlete.Participation> participations = athleteDAO.getOlympicParticipations(selectedAthlete.getId());

            // Preenche a tabela de histórico
            ObservableList<Athlete.Participation> participationObservableList = FXCollections.observableArrayList(participations);
            historyAthleteTableView.setItems(participationObservableList);
        } else {
            // Exibe um alerta se nenhum atleta for selecionado
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum atleta selecionado");
            alert.setContentText("Por favor, selecione um atleta antes de visualizar o histórico.");
            alert.showAndWait();
        }
    }
    @FXML
    public void handleBtnBack() {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLAthleteMenu();
    }
}
