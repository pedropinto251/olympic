package com.lp3_grupo5.lp3_grupo5.Controller.Sports;


import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AthletesListController {
    @FXML
    private TableView<Athlete> athletesTable;
    @FXML
    private TableColumn<Athlete, Integer> athleteId;
    @FXML
    private TableColumn<Athlete, String> athleteName;
    @FXML
    private TableColumn<Athlete, String> athleteCountry;

    public void setAthletes(List<Athlete> athletes) {
        ObservableList<Athlete> observableList = FXCollections.observableArrayList(athletes);
        athleteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        athleteName.setCellValueFactory(new PropertyValueFactory<>("name"));
        athleteCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        athletesTable.setItems(observableList);
    }
}

