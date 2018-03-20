package client.controller;

import client.Main;
import client.domain.mediator.Facade;
import com.google.gson.internal.StringMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Marek on 20-May-17.
 */
public class MovieInfoController implements Initializable {

    public Label titleLabel;
    public ImageView moviePosterImageView;
    public Label yearLabel;
    public Label genreLabel;
    public Label imbdLabel;
    public Label sepflixLabel;
    public Button addToListButton;
    public Button rateButton;
    public Text overviewText;
    public Button trailerButton;
    public ListView comentList;
    public TextField rateField;
    public Button commentButton;
    public TextField commentField;

    private Thread controllerThread;
    private StringMap<Object> movieInfo;
    public Stage secondaryStage;

    public MovieInfoController() {
        Main.movieInfoC = this;
        controllerThread = Thread.currentThread();
    }

    public String getTitle() {
        if (!movieInfo.isEmpty()) return (String) movieInfo.get("title");
        else return null;
    }

    public String getYear() {
        if (!movieInfo.isEmpty()) {
            String year = (String) movieInfo.get("release_date");
            String[] token = year.split("-");
            year = token[0];
            return year;
        } else return null;
    }

    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {
        boolean interupted = false;
        try {
            wait(15000);
        } catch (InterruptedException e) {
            interupted = true;
        }
        if (interupted) {
            loadMovieInfo();
        } else Helper.alertdisplay("Timeout Error", "Server not responding");
    }

    private void loadMovieInfo() {
        String posterpath = (String) movieInfo.get("poster_path");
        String imageURL = "https://image.tmdb.org/t/p/w320" + posterpath;
        moviePosterImageView.setImage(new Image(imageURL));
        titleLabel.setText((String) movieInfo.get("title"));
        String year = (String) movieInfo.get("release_date");
        String[] token = year.split("-");
        year = token[0];
        yearLabel.setText(year);
        genreLabel.setText((String) movieInfo.get("genres"));
        Double voteimbd = (Double) movieInfo.get("vote_average");
        imbdLabel.setText("Imbd: " + voteimbd.toString());
        if (movieInfo.get("vote_sepflix") != null) {
            Double d = (Double) movieInfo.get("vote_sepflix");
            sepflixLabel.setText("SEPFlix: " + d.toString());
        } else sepflixLabel.setText("No sepflix rate");
        overviewText.setText((String) movieInfo.get("overview"));
        if (movieInfo.get("comments") != null) {
            ObservableList<String> data = FXCollections.observableArrayList();
            ArrayList<StringMap<Object>> comments = (ArrayList<StringMap<Object>>) movieInfo.get("comments");
            for (StringMap<Object> comment : comments) {
                String string = comment.get("user_name") + ": " + comment.get("comment");
                data.add(string);
            }
            comentList.setItems(data);
        }
    }

    public void interupt(StringMap<Object> movieInfo) {
        this.movieInfo = movieInfo;
        controllerThread.interrupt();
    }

    public void addToMyList(ActionEvent actionEvent) throws IOException {
        if (Main.loggedUser == null) {
            BorderPane root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/login.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
            Scene scene = new Scene(root);
            Main.stage.setScene(scene);
            Main.stage.show();
        } else {
            Facade.addMovieToFavourites(Main.loggedUser, movieInfo);
            Helper.successdisplay("Added", "Movie was added to you favourites");
            Helper.addDataToRequest("Action", "AddFavouriteMovie");
            Helper.addDataToRequest("Token", Main.token);
            Helper.addDataToRequest("id", movieInfo.get("id"));
            Helper.addDataToRequest("Username", Facade.getUsername(Main.loggedUser));
            Helper.sendRequest();
        }
    }

    public void rate(ActionEvent actionEvent) throws IOException {
        if (Main.loggedUser == null) {
            BorderPane root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/login.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
            Scene scene = new Scene(root);
            Main.stage.setScene(scene);
            Main.stage.show();
        } else {
            if (!Helper.validateRateNumber(rateField)) {
                Helper.alertdisplay("Wrong Input", "You have entered wrong value in rate field");
                return;
            } else {
                Double d = Double.parseDouble(rateField.getText());
                if (d > 10) {
                    Helper.alertdisplay("Wrong value", "Yu can only rate between 0-10");
                    return;
                }
            }
            Double d = Double.parseDouble(rateField.getText());
            Helper.addDataToRequest("Action", "RateMovie");
            Helper.addDataToRequest("id", movieInfo.get("id"));
            Helper.addDataToRequest("Username", Facade.getUsername(Main.loggedUser));
            Helper.addDataToRequest("Token", Main.token);
            Helper.addDataToRequest("Rate", d);
            Helper.sendRequest();
        }
    }


    public void playTrailer(ActionEvent actionEvent) throws IOException {
        Stage secondaryStage = new Stage();
        this.secondaryStage = secondaryStage;
        URL paneOneUrl = getClass().getResource("../view/trailer.fxml");
        WebView webView = FXMLLoader.load(paneOneUrl);
        AnchorPane root = new AnchorPane(webView);
        Scene scene = new Scene(root);
        secondaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../view/assets/icon.png")));
        secondaryStage.setScene(scene);
        secondaryStage.setTitle(titleLabel.getText());
        secondaryStage.show();
        secondaryStage.setOnCloseRequest(we -> {
            webView.getEngine().load(null);
        });
    }

    public void comment(ActionEvent actionEvent) throws IOException {
        if (Main.loggedUser == null) {
            BorderPane root = new BorderPane();
            URL menuBarUrl = getClass().getResource("../view/menubarGuest.fxml");
            MenuBar bar = FXMLLoader.load(menuBarUrl);
            URL paneOneUrl = getClass().getResource("../view/login.fxml");
            AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
            root.setTop(bar);
            root.setCenter(paneOne);
            Scene scene = new Scene(root);
            Main.stage.setScene(scene);
            Main.stage.show();
        } else if (!commentField.getText().isEmpty()) {
            Helper.addDataToRequest("Action", "CommentMovie");
            Helper.addDataToRequest("Token", Main.token);
            Helper.addDataToRequest("id", movieInfo.get("id"));
            Helper.addDataToRequest("Username", Facade.getUsername(Main.loggedUser));
            Helper.addDataToRequest("Comment", commentField.getText());
            Helper.sendRequest();
            String comment = Facade.getUsername(Main.loggedUser) + ": " + commentField.getText();
            comentList.getItems().add(comment);
        }
    }
}
