package server.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import server.Main;
import server.domain.model.Log;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Created by andreea on 5/24/2017.
 */
public class ControllerServerLog implements Initializable, Observer {
    public TableView table;
    public TableColumn ipAddress;
    public TableColumn logIn;
    public TableColumn action;
    public TableColumn time;
    public Button saveLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.serverLogger.addObserver(this);
        fillTable();
    }

    private void fillTable() {
        ObservableList<Log> logs = FXCollections.observableArrayList(Main.serverLogger.getActionsLog());
        ipAddress.setCellValueFactory(new PropertyValueFactory<Log, String>("ip"));
        action.setCellValueFactory(new PropertyValueFactory<Log, String>("action"));
        logIn.setCellValueFactory(new PropertyValueFactory<Log, Boolean>("loggedIn"));
        time.setCellValueFactory(new PropertyValueFactory<Log, String>("date"));
        table.setItems(logs);
    }

    public void saveLog(ActionEvent actionEvent) {
        Main.serverLogger.saveLog();
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(
                this::fillTable
        );
    }


}
