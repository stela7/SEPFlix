package client.controller;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Marek on 15-May-17.
 */
public class MenuController {

    public MenuItem list;
    public MenuItem logIn;
    public MenuItem register;
    public MenuItem homescreen;
    public MenuItem trailer;

    public void changeViewMenu(ActionEvent actionEvent) throws IOException {

        BorderPane root = null;
        if (Main.trailerSearchTrailerController != null) {
            Main.trailerSearchTrailerController.stopPlay();
        }

        if ((actionEvent.getSource() == homescreen)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/listOfMovies.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == list)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/login.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == logIn)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/login.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == register)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/registerUser.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
        } else if ((actionEvent.getSource() == trailer)) {
            root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/player.fxml");
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
