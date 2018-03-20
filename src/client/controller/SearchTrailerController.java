package client.controller;

import client.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

import java.io.IOException;

/**
 * Created by Marek on 23-May-17.
 */
public class SearchTrailerController {
    public WebView ytView;
    public TextField searchField;
    private String url;
    private Thread controllerThread;
    private boolean play = false;

    public SearchTrailerController() {
        Main.trailerSearchTrailerController = this;
        controllerThread = Thread.currentThread();
    }

    public synchronized void Play(ActionEvent actionEvent) throws IOException {
        this.play = true;
        if (searchField.getText().isEmpty()) {
            Helper.alertdisplay("Wrong input", "Field cannot be empty");
            return;
        }
        Helper.addDataToRequest("Action", "GetTrailerSearched");
        Helper.addDataToRequest("FieldText", searchField.getText());
        Helper.sendRequest();
        boolean interrupted = false;
        try {
            wait(15000);
        } catch (InterruptedException e) {
            interrupted = true;
        }
        if (interrupted) {
            ytView.getEngine().load(url);
            ytView.setPrefSize(600, 400);
            Main.stage.show();
        } else Helper.alertdisplay("Timeout Error", "Server is not responding");

    }

    public void interupt(String url) {
        this.url = url;
        controllerThread.interrupt();
    }

    public void stopPlay() {
        if (play) {
            ytView.getEngine().load(null);
        }
    }
}
