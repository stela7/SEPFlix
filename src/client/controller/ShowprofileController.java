package client.controller;

import client.Main;
import client.domain.mediator.Facade;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Marek on 17-May-17.
 */
public class ShowprofileController implements Initializable {

    public TextField nameField;
    public TextField surnameField;
    public TextField emailField;
    public Button editButton;
    public Button saveButton;
    public Label usernameField;
    public Button changepassButton;
    public PasswordField oldPassField;
    public PasswordField newPassField;
    public PasswordField confirmPassField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changepassButton.setVisible(false);
        if (Main.loggedUser != null) {
            usernameField.setText(Facade.getUsername(Main.loggedUser));
            nameField.setText(Facade.getFirstName(Main.loggedUser));
            surnameField.setText(Facade.getLastName(Main.loggedUser));
            emailField.setText(Facade.getEmail(Main.loggedUser));
        }

    }

    public void edit(ActionEvent actionEvent) {
        if (Main.loggedUser != null) {
            changepassButton.setVisible(true);
            nameField.setEditable(true);
            surnameField.setEditable(true);
            emailField.setEditable(true);
        }
    }

    public void save(ActionEvent actionEvent) {
        if (Main.loggedUser != null) {
            if (!Helper.validateEmail(emailField)) {
                Helper.alertdisplay("Wrong Input", "Email format is not correct");
                return;
            }
            if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                Helper.alertdisplay("Wrong Input", "Fields cannot be empty");
                return;
            }
            Helper.addDataToRequest("Action", "editProfile");
            Helper.addDataToRequest("Token", Main.token);
            Helper.addDataToRequest("Username", usernameField.getText());
            Helper.addDataToRequest("FirstName", nameField.getText());
            Main.loggedUser.setFirstName(nameField.getText());
            Helper.addDataToRequest("SecondName", surnameField.getText());
            Main.loggedUser.setLastName(surnameField.getText());
            Helper.addDataToRequest("Email", emailField.getText());
            Main.loggedUser.setEmail(emailField.getText());
            if (!oldPassField.getText().isEmpty()) {
                if (newPassField.getText().equals(confirmPassField.getText())) {
                    Helper.addDataToRequest("OldPassword", Helper.get_SHA_512_SecurePassword(oldPassField.getText()));
                    Helper.addDataToRequest("NewPassword", Helper.get_SHA_512_SecurePassword(newPassField.getText()));
                    Helper.sendRequest();
                } else {
                    Helper.alertdisplay("Wrong password", "new password is not the same as the confirm password");
                    Helper.clearRequest();
                }
            } else {
                Helper.addDataToRequest("OldPassword", null);
                Helper.addDataToRequest("NewPassword", null);
                Helper.sendRequest();
            }
            nameField.setEditable(false);
            surnameField.setEditable(false);
            emailField.setEditable(false);
            oldPassField.setVisible(false);
            oldPassField.setText("");
            newPassField.setVisible(false);
            newPassField.setText("");
            confirmPassField.setVisible(false);
            confirmPassField.setText("");
            changepassButton.setVisible(false);
            Helper.successdisplay("Saved", "Your data has been saved");
        }
    }

    public void revert(ActionEvent actionEvent) {
        if (Main.loggedUser != null) {
            usernameField.setText(Facade.getUsername(Main.loggedUser));
            nameField.setText(Facade.getFirstName(Main.loggedUser));
            surnameField.setText(Facade.getLastName(Main.loggedUser));
            emailField.setText(Facade.getEmail(Main.loggedUser));
        }
        nameField.setEditable(false);
        surnameField.setEditable(false);
        emailField.setEditable(false);

        oldPassField.setVisible(false);
        oldPassField.setText("");
        newPassField.setVisible(false);
        newPassField.setText("");
        confirmPassField.setVisible(false);
        confirmPassField.setText("");
        changepassButton.setVisible(false);

    }


    public void changePass(ActionEvent actionEvent) {
        oldPassField.setVisible(true);
        newPassField.setVisible(true);
        confirmPassField.setVisible(true);
        changepassButton.setVisible(false);

    }
}
