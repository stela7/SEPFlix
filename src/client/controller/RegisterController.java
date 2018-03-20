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
 * Created by Marek on 15-May-17.
 */
public class RegisterController {
    public Button registerButton;
    public TextField firstNameField;
    public TextField secondNameField;
    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;

    public void registerUser(ActionEvent actionEvent) throws IOException {
        if (!Helper.validateEmail(emailField)) {
            Helper.alertdisplay("Wrong Input", "Email format is not correct");
            return;
        }
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || firstNameField.getText().isEmpty()
                || secondNameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Helper.alertdisplay("Wrong Input", "Fields cannot be empty");
            return;
        }
        Helper.addDataToRequest("Action", "register");
        Helper.addDataToRequest("Username", usernameField.getText());
        Helper.addDataToRequest("Password", Helper.get_SHA_512_SecurePassword(passwordField.getText()));
        Helper.addDataToRequest("FirstName", firstNameField.getText());
        Helper.addDataToRequest("SecondName", secondNameField.getText());
        Helper.addDataToRequest("Email", emailField.getText());
        Helper.sendRequest();
        Helper.successdisplay("Registered", "You are now user of SEPFlix");

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
    }
}
