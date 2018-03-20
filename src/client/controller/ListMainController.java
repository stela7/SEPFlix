package client.controller;

import client.Main;
import com.google.gson.internal.StringMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Marek on 20-May-17.
 */
public class ListMainController implements Initializable {
    public Button searchButton;
    public TextField searchField;
    public TilePane tilepane;
    public Label popularLabel;
    public ScrollPane scrollPane;
    private ArrayList<StringMap<Object>> latestMovies;
    private Thread controllerThread;
    private boolean searched = false;

    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {
        if (!searched) {
            Helper.addDataToRequest("Action", "LatestMovies");
            Helper.sendRequest();
            popularLabel.setText("Popular movies");

            boolean interuppted = false;
            try {
                wait(20000);
            } catch (InterruptedException e) {
                interuppted = true;
            }
            if (interuppted) {
                addMovies(latestMovies);
            } else {
                Helper.alertdisplay("Timeout Error", "Server not responding");
            }
        }
        searched = false;
    }

    public void interupt(ArrayList<StringMap<Object>> latestMovies) {
        this.latestMovies = latestMovies;
        controllerThread.interrupt();
    }

    public ListMainController() {
        controllerThread = Thread.currentThread();
        Main.listMainC = this;
    }

    public void showSearchBar(ActionEvent actionEvent) {
        searchField.setVisible(true);
        searchButton.setVisible(false);
    }

    public synchronized void searchMovie(ActionEvent actionEvent) {
        if (searchField.getText().isEmpty()) {
            Helper.alertdisplay("Wrong input", "Field cannot be empty");
            return;
        }
        if (!searchField.getText().isEmpty()) {
            popularLabel.setText("Search result");
            Helper.addDataToRequest("Action", "SearchMovie");
            Helper.addDataToRequest("SearchField", searchField.getText());
            Helper.sendRequest();
            boolean interuppted = false;
            try {
                wait(20000);
            } catch (InterruptedException e) {
                interuppted = true;
            }
            if (interuppted) {
                searched = true;
                tilepane.getChildren().clear();
                addMovies(latestMovies);
            } else {
                Helper.alertdisplay("Timeout Error", "Server not responding");
            }
            searchField.setVisible(false);
            searchButton.setVisible(true);
        }
    }

    public void addMovies(ArrayList<StringMap<Object>> latestMovies) {
        for (int i = 0; i < latestMovies.size(); i++) {
            String title = (String) latestMovies.get(i).get("title");
            String year = (String) latestMovies.get(i).get("release_date");
            String[] token = year.split("-");
            year = token[0];
            String url = (String) latestMovies.get(i).get("poster_path");
            Double idnum = (Double) latestMovies.get(i).get("id");
            String id = Double.toString(idnum);
            try {
                addTile(title, year, url, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void addTile(String movieTitle, String prodYear, String url, String movieID) throws IOException {
        URL paneOneUrl = getClass().getResource("../view/tile.fxml");
        AnchorPane tile = FXMLLoader.load(paneOneUrl);

        ImageView imageview = (ImageView) tile.getChildren().get(0);
        Label title = (Label) tile.getChildren().get(1);
        Label year = (Label) tile.getChildren().get(2);
        Label id = (Label) tile.getChildren().get(3);

        title.setText(movieTitle);
        year.setText(prodYear);
        String imageURL = "https://image.tmdb.org/t/p/w320" + url;
        imageview.setImage(new Image(imageURL));
        id.setText(movieID);

        tilepane.getChildren().add(tile);
    }

}
