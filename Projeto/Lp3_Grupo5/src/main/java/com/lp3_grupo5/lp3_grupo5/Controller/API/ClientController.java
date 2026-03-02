package com.lp3_grupo5.lp3_grupo5.Controller.API;

import com.lp3_grupo5.lp3_grupo5.Model.User;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.lp3_grupo5.lp3_grupo5.Model.Client;
import com.lp3_grupo5.lp3_grupo5.Model.ApiResponse;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.ApiService;
import com.google.gson.Gson;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.List;

public class ClientController {

    @FXML
    private TableView<Client> clientTableView;
    @FXML
    private TableColumn<Client, String> idColumn;
    @FXML
    private TableColumn<Client, String> groupIdColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TableColumn<Client, String> emailColumn;
    @FXML
    private TableColumn<Client, Boolean> activeColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Client> clientObservableList;
    private static final Dotenv dotenv = Dotenv.load();
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        groupIdColumn.setCellValueFactory(new PropertyValueFactory<>("GroupId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("Active"));

        // Custom cell factory for activeColumn
        activeColumn.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Boolean>() {
            @Override
            public String toString(Boolean object) {
                return object ? "Ativo" : "Desativado";
            }

            @Override
            public Boolean fromString(String string) {
                return "Ativo".equals(string);
            }
        }));

        loadClientData("client");
    }

    private void loadClientData(String endpoint) {
        try {
            String jsonData = ApiService.getData(endpoint);
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(jsonData, ApiResponse.class);
            List<Client> clients = apiResponse.getClients();

            clientObservableList = FXCollections.observableArrayList(clients);
            clientTableView.setItems(clientObservableList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Client> filteredList = FXCollections.observableArrayList();

        for (Client client : clientObservableList) {
            boolean matches = false;

            if (client.getName().toLowerCase().contains(searchText)) {
                matches = true;
            } else if (client.getId().toLowerCase().contains(searchText)) {
                matches = true;
            } else if (client.getEmail().toLowerCase().contains(searchText)) {
                matches = true;
            }

            if (matches) {
                filteredList.add(client);
            }
        }

        clientTableView.setItems(filteredList);
    }

    @FXML
    void handleDeactivateClient(ActionEvent event) {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                String endpoint = "client/" + selectedClient.getId();
                String jsonInputString = "{\"Active\": false}";
                String responseMessage = ApiService.updateData(endpoint, jsonInputString);

                if (responseMessage.equals("Success")) {
                    selectedClient.setActive(false);
                    clientTableView.refresh();
                    showAlert("Sucesso", "Cliente desativado com sucesso.", Alert.AlertType.INFORMATION);

                    // Enviar email de desativação
                    sendDeactivationEmail(selectedClient.getEmail(), selectedClient.getName());
                } else {
                    showAlert("Erro", "Falha ao desativar o cliente. Mensagem: " + responseMessage, Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erro", "Ocorreu um erro ao desativar o cliente.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aviso", "Nenhum cliente selecionado.", Alert.AlertType.WARNING);
        }
    }

    private void sendDeactivationEmail(String recipientEmail, String recipientName) {
        String apiKey = dotenv.get("MAILJET_API_KEY");
        String apiSecret = dotenv.get("MAILJET_API_SECRET");

        // Obter o número de identificação do gestor da sessão
        User loggedUser = Session.getUser();
        String managerIdNumber = loggedUser != null ? String.valueOf(loggedUser.getId()) : "ID não disponível";

        MailjetClient client = new MailjetClient(apiKey, apiSecret);
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "lp3sendmail@gmail.com")
                                        .put("Name", "Lp3 Oporto Olympics"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipientEmail)
                                                .put("Name", recipientName)))
                                .put(Emailv31.Message.SUBJECT, "Conta Desativada")
                                .put(Emailv31.Message.TEXTPART, "Olá " + recipientName + ",\n\nA sua conta foi desativada pelo gestor: " + managerIdNumber + ".\n\nPara mais informações, contacte lp3sendmail@gmail.com")
                                .put(Emailv31.Message.HTMLPART,
                                        "<div style='font-family: Arial, sans-serif; color: #333;'>"
                                                + "<h3>Olá " + recipientName + ",</h3>"
                                                + "<p>A sua conta foi desativada pelo gestor: <strong>" + managerIdNumber + "</strong>.</p>"
                                                + "<p>Para mais informações, contacte <a href='mailto:lp3sendmail@gmail.com'>lp3sendmail@gmail.com</a>.</p>"
                                                + "<br>"
                                                + "<p>Atenciosamente,</p>"
                                                + "<p><strong>Lp3 Oporto Olympics</strong></p>"
                                                + "</div>")));

        try {
            MailjetResponse response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
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
        return (Stage) clientTableView.getScene().getWindow();
    }
}