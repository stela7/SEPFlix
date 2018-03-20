package client;

import client.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import server.domain.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    public static ClientConnection clientConnection;
    public static User loggedUser;
    public static LoginController loginC;
    public static Stage stage;
    public static ListMainController listMainC;
    public static MovieInfoController movieInfoC;
    public static FavouritesController facouritesC;
    public static TrailerController trailerC;
    public static SearchTrailerController trailerSearchTrailerController;
    public static String token;

    @Override
    public void start(Stage primaryStage) throws IOException {

        // loading FXML resources
        // note that we don't need PaneTwo in this class

        URL menuBarUrl = getClass().getResource("view/menubarGuest.fxml");
        MenuBar bar = FXMLLoader.load(menuBarUrl);

        URL paneOneUrl = getClass().getResource("view/listOfMovies.fxml");
        AnchorPane paneOne = FXMLLoader.load(paneOneUrl);


        // constructing our scene using the static root

        root.setTop(bar);
        root.setCenter(paneOne);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("view/assets/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("SEPFlix");
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
        stage.setOnCloseRequest(we -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("You are trying to close Sepflix");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    clientConnection.getClientReceiverThread().stop();
                    clientConnection.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.close();
                System.exit(0);
            } else {
                we.consume();
            }
        });
    }


    public static void main(String[] args) {
        createConnection();
        launch(args);
    }

    public static void createConnection() {
        clientConnection = ClientConnection.getClientConnection();
    }
}
