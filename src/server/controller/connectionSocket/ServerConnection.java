package server.controller.connectionSocket;

import server.domain.mediator.ServerCommunication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by martin on 15/05/2017.
 */
public class ServerConnection extends Observable implements Runnable {
    private static final int PORT = 6666;
    private static final ArrayList<Socket> clients = new ArrayList<>();


    public ArrayList<Socket> getClients() {
        for (Socket client : clients) {
            if (!client.isConnected()) {
                clients.remove(client);
            }
        }
        return clients;
    }

    public static void removeClient(Socket client) {
        clients.remove(client);
    }

    @Override
    public void run() {
        int count = 1;
        try {
            System.out.println("Starting Server...");

            ServerSocket welcomeSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Waiting for a client...");
                Socket connectionSocket = welcomeSocket.accept();
                ServerCommunication serverCommunication = new ServerCommunication(connectionSocket);
                new Thread(serverCommunication, "Communication #" + count).start();
                clients.add(connectionSocket);
                super.setChanged();
                super.notifyObservers(clients);
                count++;
            }
        } catch (Exception e) {
            System.out.println("Exception in connection to server: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }
}
