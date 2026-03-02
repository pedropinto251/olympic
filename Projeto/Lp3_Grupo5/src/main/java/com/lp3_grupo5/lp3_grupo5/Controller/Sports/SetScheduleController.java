package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection.ApiService;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class SetScheduleController {
    @FXML
    private ComboBox<Event> eventComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField durationField;
    @FXML
    private ComboBox<Location> locationComboBox; // Substituído o TextField por ComboBox
    @FXML
    private Button saveButton;

    private int sportId;
    private int locationId;
    private final SportDAO sportDAO = new SportDAO();

    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]\\d|2[0-3]):([0-5]\\d)$");

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @FXML
    private void initialize() {
        loadLocations(); // Carrega os locais disponíveis
        configureComboBoxConverters(); // Configura os conversores das ComboBox
    }

    private void loadLocations() {
        List<Location> locations = sportDAO.findAllActiveLocations();
        ObservableList<Location> observableLocations = FXCollections.observableArrayList(locations);
        locationComboBox.setItems(observableLocations);
    }

    private void configureComboBoxConverters() {
        eventComboBox.setConverter(new StringConverter<Event>() {
            @Override
            public String toString(Event event) {
                return event == null ? "" : event.getId() + " - " + event.getYear();
            }

            @Override
            public Event fromString(String string) {
                return null; // Não é necessário implementar
            }
        });

        locationComboBox.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                return location == null ? "" : location.getId() + " - " + location.getAddress();
            }

            @Override
            public Location fromString(String string) {
                return null; // Não é necessário implementar
            }
        });
    }

    public void loadActiveEvents() {
        try {
            List<Event> events = sportDAO.getActiveEvents();
            ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
            eventComboBox.setItems(observableEvents);
        } catch (SQLException e) {
            showErrorAlert("Erro ao carregar eventos ativos.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSave() {
        try {
            Event selectedEvent = eventComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            String startTimeText = startTimeField.getText();
            String durationText = durationField.getText();
            Location selectedLocation = locationComboBox.getValue();

            // Verificação do formato da hora
            if (!TIME_PATTERN.matcher(startTimeText).matches()) {
                showErrorAlert("Hora de início inválida. Use o formato hh:mm.");
                return;
            }

            // Verificação do formato da duração
            if (!durationText.matches("\\d+")) {
                showErrorAlert("Duração inválida. Insira um número inteiro de horas.");
                return;
            }

            LocalTime startTime = LocalTime.parse(startTimeText);
            int durationHours = Integer.parseInt(durationText);

            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = startDateTime.plusHours(durationHours);

            // Verificações adicionais
            if (startDateTime.isBefore(LocalDateTime.now())) {
                showErrorAlert("A data e hora de início não podem ser anteriores ao momento atual.");
                return;
            }

            if (endDateTime.isBefore(startDateTime)) {
                showErrorAlert("A data e hora de término devem ser posteriores à data e hora de início.");
                return;
            }

            // Verificação de horário ativo existente
            if (sportDAO.hasActiveSchedule(sportId)) {
                showErrorAlert("Este desporto já possui um horário ativo definido.");
                return;
            }

            // Verificação de reserva do local
            if (sportDAO.isLocationReserved(selectedLocation.getId(), startDateTime, endDateTime)) {
                showErrorAlert("O local já está reservado para outro desporto no horário especificado.");
                return;
            }

            Sport sport = sportDAO.findSportById(sportId); // Obtém o desporto pelo ID

            // Insere o horário na base de dados e obtém o scheduleId gerado
            int scheduleId = sportDAO.insertSportEvent(sport, selectedEvent.getId(), startDateTime, endDateTime, selectedLocation.getId());

            // Adicionando a requisição POST
            String endpoint = "game/";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String jsonInputString = String.format(
                    "{\"StartDate\":\"%s\",\"EndDate\":\"%s\",\"Location\":\"%s\",\"Sport\":\"%s\",\"Capacity\":%d,\"EventId\":%d}",
                    startDateTime.format(formatter), endDateTime.format(formatter), selectedLocation.getAddress(), sport.getName(), selectedLocation.getCapacity(), scheduleId
            );

            String responseMessage = ApiService.postData(endpoint, jsonInputString);
            if (responseMessage.equals("Success")) {
                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Horário e local definidos com sucesso!\nO evento tem data de fim para: " + endDateTime.format(displayFormatter));
            } else {
                showErrorAlert("Erro ao enviar dados para a API: " + responseMessage);
            }

            closeWindow();
        } catch (Exception e) {
            showErrorAlert("Erro ao salvar horário e local.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        showAlert(Alert.AlertType.ERROR, "Erro", message);
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}