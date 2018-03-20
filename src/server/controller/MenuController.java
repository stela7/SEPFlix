package server.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import server.Main;

import java.io.IOException;
import java.net.URL;

/**
 * Created by andreea on 5/24/2017.
 */
public class MenuController {
    public MenuItem home;
    public MenuItem homescreen;
    public MenuItem serverLog;
    public MenuItem usersManag;

    public void changeServerMenu(ActionEvent actionEvent) throws IOException {
        BorderPane root = new BorderPane();

        if ((actionEvent.getSource() == homescreen)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menuServer.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/ServerHomeScreen.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == serverLog)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menuServer.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/ServerLog.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == usersManag)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menuServer.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/UsersManagement.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        }
        if (root != null) {
            Scene scene = new Scene(root);
            Main.stage.setScene(scene);
            Main.stage.show();
        }
    }
}
