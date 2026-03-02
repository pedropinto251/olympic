package com.lp3_grupo5.lp3_grupo5.Controller.API;

import com.google.gson.Gson;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.ApiService;
import com.lp3_grupo5.lp3_grupo5.Model.ApiResponse;
import com.lp3_grupo5.lp3_grupo5.Model.Game;
import com.lp3_grupo5.lp3_grupo5.Model.Ticket;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class GameController {

    @FXML
    private TableView<Game> gameTableView;
    @FXML
    private TableColumn<Game, String> gameIdColumn;
    @FXML
    private TableColumn<Game, String> gameGroupIdColumn;
    @FXML
    private TableColumn<Game, String> gameStartDateColumn;
    @FXML
    private TableColumn<Game, String> gameEndDateColumn;
    @FXML
    private TableColumn<Game, String> gameLocationColumn;
    @FXML
    private TableColumn<Game, Integer> gameCapacityColumn;
    @FXML
    private TableColumn<Game, String> gameSportColumn;
    @FXML
    private TableColumn<Game, Boolean> gameActiveColumn;
    @FXML
    private TableColumn<Game, Integer> gameEventIdColumn;

    @FXML
    private TableView<Ticket> ticketTableView;
    @FXML
    private TableColumn<Ticket, String> ticketIdColumn;
    @FXML
    private TableColumn<Ticket, String> ticketStartDateColumn;
    @FXML
    private TableColumn<Ticket, String> ticketEndDateColumn;
    @FXML
    private TableColumn<Ticket, String> ticketLocationColumn;
    @FXML
    private TableColumn<Ticket, String> ticketSeatColumn;
    @FXML
    private TableColumn<Ticket, ImageView> ticketQRColumn;

    @FXML
    private Button btnMenu;

    private ObservableList<Game> gameObservableList;
    private ObservableList<Ticket> ticketObservableList;
    private static final Dotenv dotenv = Dotenv.load();

    @FXML
    public void initialize() {
        gameIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        gameGroupIdColumn.setCellValueFactory(new PropertyValueFactory<>("GroupId"));
        gameStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        gameEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        gameCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        gameSportColumn.setCellValueFactory(new PropertyValueFactory<>("Sport"));
        gameActiveColumn.setCellValueFactory(new PropertyValueFactory<>("Active"));
        gameEventIdColumn.setCellValueFactory(new PropertyValueFactory<>("EventId"));

        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        ticketStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        ticketEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        ticketLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        ticketSeatColumn.setCellValueFactory(new PropertyValueFactory<>("Seat"));
        ticketQRColumn.setCellValueFactory(new PropertyValueFactory<>("TicketQRImage"));

        gameTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadTicketData(newValue.getId());
            }
        });

        loadGameData("game");
    }

    private void loadGameData(String endpoint) {
        try {
            String jsonData = ApiService.getData(endpoint);
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(jsonData, ApiResponse.class);
            List<Game> games = apiResponse.getGames();

            gameObservableList = FXCollections.observableArrayList(games);
            gameTableView.setItems(gameObservableList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTicketData(String gameId) {
        try {
            String endpoint = "ticket/game/" + gameId;
            String jsonData = ApiService.getData(endpoint);
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(jsonData, ApiResponse.class);
            List<Ticket> tickets = apiResponse.getTicketInfo();

            for (Ticket ticket : tickets) {
                String base64Image = ticket.getTicketQR();
                System.out.println("Ticket ID: " + ticket.getId() + ", QR Base64: " + base64Image); // Verificação
                if (base64Image != null && base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    Image image = new Image(bis);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    ticket.setTicketQRImage(imageView);
                }
            }

            ticketObservableList = FXCollections.observableArrayList(tickets);
            ticketTableView.setItems(ticketObservableList);

        } catch (IOException e) {
            if (e.getMessage().contains("Server returned HTTP response code: 406")) {
                showAlert("Aviso", "Nenhum bilhete encontrado para este jogo.", Alert.AlertType.WARNING);
            } else {
                e.printStackTrace();
                showAlert("Erro", "Ocorreu um erro ao buscar os bilhetes.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao buscar os bilhetes.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }
    private Stage getStage() {
        return (Stage) ticketTableView.getScene().getWindow();
    }
}