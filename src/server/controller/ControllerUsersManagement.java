package server.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import server.Main;
import server.domain.model.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by martin on 25/05/2017.
 */
public class ControllerUsersManagement implements Initializable {
    public Button buttonRemove;
    public TableView table;
    public TableColumn username;
    public TableColumn firstName;
    public TableColumn lastName;
    public TableColumn credit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<User> users = FXCollections.observableArrayList(Main.databaseConnection.getUsers());
        username.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        firstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        table.setItems(users);
    }

    public void removeUser(ActionEvent actionEvent) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            User user = (User) table.getSelectionModel().getSelectedItem();
            Main.databaseConnection.removeUserByUserName(user.getUserName());
            HelperServer.successdisplay("Success", "User removed!");
        } else {
            HelperServer.alertdisplay("Alert", "Select a user!");
        }
    }
}
