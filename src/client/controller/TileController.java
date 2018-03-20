package client.controller;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Marek on 21-May-17.
 */
public class TileController {

    public Label idLabel;

    private void movieInfo() throws IOException {
        Helper.addDataToRequest("Action", "MovieDetail");
        Double id = Double.parseDouble(idLabel.getText());
        Helper.addDataToRequest("id", id);
        Helper.sendRequest();

        BorderPane root = new BorderPane();
        if (Main.loggedUser != null) {
            URL menuBarUrl = getClass().getResource("../view/menubarLogged.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            root.setTop(bar);
        } else {
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            root.setTop(bar);
        }
        URL paneOneUrl = getClass().getResource("../view/movieInfo.fxml");
        AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
        root.setCenter(paneOne);
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    public void showMovieInfo(ActionEvent actionEvent) throws IOException {
        movieInfo();
    }

    public void showMovieInfoMouse(MouseEvent mouseEvent) throws IOException {
        movieInfo();
    }
}
