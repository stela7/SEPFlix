package client.controller;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Marek on 17-May-17.
 */
public class LoginController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    private int errorCode = -1;
    private Thread controlllerThread;

    public LoginController() {
        Main.loginC = this;
        this.controlllerThread = Thread.currentThread();
    }

    public synchronized void logIn(ActionEvent actionEvent) throws IOException {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Helper.alertdisplay("Wrong input", "Fields cannot be empty");
            return;
        }
        boolean interupted = false;
        Helper.addDataToRequest("Action", "login");
        Helper.addDataToRequest("Username", usernameField.getText());
        Helper.addDataToRequest("Password", Helper.get_SHA_512_SecurePassword(passwordField.getText()));
        Helper.sendRequest();
        try {
            wait(25000);
        } catch (InterruptedException e) {
            interupted = true;
        }
        if (interupted) {
            if (errorCode == 0) loginView();
            else if (errorCode == 1) loginError();
        } else Helper.alertdisplay("Timeout Error", "Server is not responding");
    }

    public void interupt(int errorCode) {
        this.errorCode = errorCode;
        controlllerThread.interrupt();

    }

    private void loginView() throws IOException {
        BorderPane root = new BorderPane();
        URL menuBarUrl = getClass().getResource("../view/menubarLogged.fxml");
        MenuBar bar = FXMLLoader.load(menuBarUrl);
        URL paneOneUrl = getClass().getResource("../view/listOfMovies.fxml");
        AnchorPane paneOne = FXMLLoader.load(paneOneUrl);
        root.setTop(bar);
        root.setCenter(paneOne);
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    private void loginError() {
        Helper.alertdisplay("Error", "Wrong username or password.");
    }
}
