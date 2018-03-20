package client.controller;

import client.Main;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Marek on 24-May-17.
 */
public class TrailerController implements Initializable {


    public WebView trailerView;
    private String url;
    private Thread controllerThread;

    public TrailerController() {
        Main.trailerC = this;
        controllerThread = Thread.currentThread();
    }

    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {
        Helper.addDataToRequest("Action", "GetTrailer");
        Helper.addDataToRequest("MovieName", Main.movieInfoC.getTitle());
        Helper.sendRequest();
        boolean interrupted = false;
        try {
            wait(15000);
        } catch (InterruptedException e) {
            interrupted = true;
        }
        if (interrupted) {
            trailerView.getEngine().load(url);
            trailerView.setPrefSize(600, 400);
            Main.movieInfoC.secondaryStage.show();
        } else Helper.alertdisplay("Timeout Error", "Server is not responding");
    }

    public void interupt(String url) {
        this.url = url;
        controllerThread.interrupt();
    }
}
