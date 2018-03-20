package server.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import server.Main;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Controller implements Initializable, Observer {
    public Label labelStartingServer;
    public Label labelYesNo;
    public Label labelClients;
    public Label labelNoClients;
    public Label labelIpAddress;
    public ListView listView;

    public Controller() {
        Main.serverConnection.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Main.serverConnection != null) {
            labelYesNo.setText("Yes");
        } else labelYesNo.setText("No");
        updateTable();
    }

    private void updateTable() {
        ArrayList<Socket> clients = Main.serverConnection.getClients();
        if (clients != null) {
            labelNoClients.setText(clients.size() + "");
            ArrayList<String> clientsIp = new ArrayList<>();
            for (Socket socket : clients) {
                clientsIp.add(socket.getRemoteSocketAddress().toString());
            }

            ObservableList<String> IPAddressList = FXCollections.observableArrayList(clientsIp);
            listView.setItems(IPAddressList);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(
                this::updateTable
        );
    }
}