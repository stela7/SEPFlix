package server;

import com.google.gson.Gson;
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
import server.controller.connectionSocket.ServerConnection;
import server.domain.mediator.ConnectionREST;
import server.domain.mediator.DatabaseConnection;
import server.domain.mediator.ServerLogger;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {

    public static DatabaseConnection databaseConnection;
    public static ServerConnection serverConnection;
    public static ConnectionREST connectionREST;
    public static ServerLogger serverLogger;
    public static Gson gson;
    private static final BorderPane root = new BorderPane();
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL menuBarUrl = getClass().getResource("view/menuServer.fxml");
        MenuBar bar = FXMLLoader.load(menuBarUrl);

        URL paneOneUrl = getClass().getResource("view/ServerHomeScreen.fxml");
        AnchorPane paneOne = FXMLLoader.load(paneOneUrl);

        root.setTop(bar);
        root.setCenter(paneOne);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("view/assets/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("SEPFlix Server");
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
        stage.setOnCloseRequest(we -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("You are trying to close Sepflix Server");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                for (Socket client : serverConnection.getClients()) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stage.close();
                System.exit(0);
            } else {
                we.consume();
            }
        });
    }


    public static void main(String[] args) {
        gson = new Gson();
        serverLogger = new ServerLogger();
        serverConnection = new ServerConnection();
        new Thread(serverConnection).start();
        databaseConnection = DatabaseConnection.getDatabaseConnection();
        connectionREST = new ConnectionREST();

        launch(args);
    }
}
