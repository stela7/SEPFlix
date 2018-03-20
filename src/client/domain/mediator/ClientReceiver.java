package client.domain.mediator;

import client.Main;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import server.domain.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by martin on 15/05/2017.
 */
public class ClientReceiver implements Runnable {
    private ObjectInputStream inFromServer;


    public ClientReceiver(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Object object = inFromServer.readObject();
                StringMap<Object> response = new Gson().fromJson((String) object, StringMap.class);
                switch ((String) response.get("Action")) {
                    case "login":
                        if (response.get("Status").equals("success")) {
                            Main.loggedUser = new User(response, false);
                            Main.token = (String) response.get("Token");
                            Main.loginC.interupt(0);
                        } else {
                            Main.loginC.interupt(1);
                        }
                        break;
                    case "LatestMovies":
                        ArrayList<StringMap<Object>> latestMovies = (ArrayList<StringMap<Object>>) response.get("LatestMovies");
                        Main.listMainC.interupt(latestMovies);
                        break;
                    case "MovieDetail":
                        Thread.sleep(500);
                        StringMap<Object> movie = (StringMap<Object>) response.get("MovieDetail");
                        Main.movieInfoC.interupt(response);
                        break;
                    case "SearchMovie":
                        ArrayList<StringMap<Object>> movies = (ArrayList<StringMap<Object>>) response.get("SearchList");
                        Main.listMainC.interupt(movies);
                        break;
                    case "FavouriteMovies":
                        ArrayList<StringMap<Object>> favourites = (ArrayList<StringMap<Object>>) response.get("FavouriteMovies");
                        Main.facouritesC.interupt(favourites);
                        break;
                    case "GetTrailer":
                        String url = (String) response.get("VideoURL");
                        Main.trailerC.interupt(url);
                        break;
                    case "GetTrailerSearched":
                        String url1 = (String) response.get("VideoURL");
                        Main.trailerSearchTrailerController.interupt(url1);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Server down");
            Main.createConnection();
        }
    }
}
