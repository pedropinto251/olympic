package com.lp3_grupo5.lp3_grupo5.Controller.Event;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Calendar;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventCalendarController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Calendar> calendarTableView;

    @FXML
    private TableColumn<Calendar, String> sportColumn;



    @FXML
    private TableColumn<Calendar, String> localColumn;

    @FXML
    private TableColumn<Calendar, String> startColumn;

    @FXML
    private TableColumn<Calendar, String> endColumn;

    @FXML
    private TableColumn<Calendar, Integer> yearColumn;

    @FXML
    private TableColumn<Calendar, String> sportFullNameColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeTextField;


    private ObservableList<Calendar> calendarData = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        sportFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("sportFullName"));

        List<Calendar> calendarList = AthleteRegistrationDAO.getAllCalendars();
        calendarData.addAll(calendarList);
        calendarTableView.setItems(calendarData);

        // Adiciona os desportos ao ComboBox
        ObservableList<String> sports = FXCollections.observableArrayList();
        for (Calendar calendar : calendarList) {
            if (!sports.contains(calendar.getSportName())) {
                sports.add(calendar.getSportName());
            }
        }
        sportFilterComboBox.setItems(sports);
        datePicker.setOnAction(this::filterByDate);
        timeTextField.setOnAction(this::filterByTime);
    }


    @FXML
    private ComboBox<String> sportFilterComboBox;

    @FXML
    public void filterBySport(ActionEvent event) {
        String selectedSport = sportFilterComboBox.getValue();
        if (selectedSport != null) {
            ObservableList<Calendar> filteredData = FXCollections.observableArrayList();
            for (Calendar calendar : calendarData) {
                if (calendar.getSportName().equals(selectedSport)) {
                    filteredData.add(calendar);
                }
            }
            calendarTableView.setItems(filteredData);
        } else {
            calendarTableView.setItems(calendarData);
        }
    }

    @FXML
    public void filterByDate(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            ObservableList<Calendar> filteredData = FXCollections.observableArrayList();
            for (Calendar calendar : calendarData) {
                LocalDateTime startTime = LocalDateTime.parse(calendar.getStartTime(), formatter);
                if (startTime.toLocalDate().equals(selectedDate)) {
                    filteredData.add(calendar);
                }
            }
            calendarTableView.setItems(filteredData);
        } else {
            calendarTableView.setItems(calendarData);
        }
    }

    @FXML
    public void filterByTime(ActionEvent event) {
        String selectedTime = timeTextField.getText();
        if (selectedTime != null && !selectedTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            ObservableList<Calendar> filteredData = FXCollections.observableArrayList();
            for (Calendar calendar : calendarData) {
                LocalDateTime startTime = LocalDateTime.parse(calendar.getStartTime(), formatter);
                if (startTime.toLocalTime().toString().startsWith(selectedTime)) {
                    filteredData.add(calendar);
                }
            }
            calendarTableView.setItems(filteredData);
        } else {
            calendarTableView.setItems(calendarData);
        }
    }
    @FXML
    void handleBtnMenu(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMenu();
    }
    private Stage getStage() {
        return (Stage) calendarTableView.getScene().getWindow();
    }
}