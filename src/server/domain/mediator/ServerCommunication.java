package server.domain.mediator;

import com.google.gson.internal.StringMap;
import server.Main;
import server.controller.connectionSocket.ServerConnection;
import server.domain.model.Log;
import server.domain.model.Movie;
import server.domain.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by martin on 15/05/2017.
 */
public class ServerCommunication implements Runnable {
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private String authToken;
    private final Socket clientSocket;
    private StringMap<Object> data;
    private StringMap<Object> returnData;

    public ServerCommunication(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        this.data = new StringMap<>();
        this.returnData = new StringMap<>();
    }

    public void run() {
        try {
            while (true) {
                String json = (String) inFromClient.readObject();
                data = Main.gson.fromJson(json, StringMap.class);
                returnData = new StringMap<>();

                switch ((String) data.get("Action")) {
                    case "register":
                        if (authenticate((String) data.get("Token"))) {
                            sendAlert("You are already authenticated!", "register");
                            break;
                        }
                        User userRegister = new User(data, false);
                        Main.databaseConnection.registerUser(userRegister);
                        break;
                    case "login":
                        if (authenticate((String) data.get("Token"))) {
                            sendAlert("You are already authenticated!", "login");
                            break;
                        }
                        returnData.put("Action", "login");
                        User userLogin = Main.databaseConnection.getUserByUserName((String) data.get("Username"));
                        if (userLogin != null) {
                            if (userLogin.getPassword().equals(data.get("Password"))) {
                                returnData.putAll(userLogin.toMap(false));
                                returnData.put("Status", "success");
                                this.authToken = UUID.randomUUID().toString();
                                returnData.put("Token", this.authToken);
                            } else {
                                returnData.put("Status", "error");
                            }
                        } else {
                            returnData.put("Status", "error");
                        }
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "editProfile":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "editProfile");
                            break;
                        }
                        User userEdit = new User(data, false);
                        if (data.get("NewPassword") != null) {
                            if (!(Main.databaseConnection.getUserByUserName((String) data.get("Username")).getPassword().equals(data.get("OldPassword")))) {
                                sendAlert("Wrong Password!", "editProfile");
                                break;
                            } else {
                                Main.databaseConnection.changePassword(userEdit);
                            }
                        }
                        Main.databaseConnection.updateUserInformation(userEdit);
                        break;
                    case "LatestMovies":
                        returnData.put("Action", "LatestMovies");
                        returnData.put("LatestMovies", Main.connectionREST.getLatestMovies());
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "MovieDetail":
                        returnData.put("Action", "MovieDetail");
                        Double idDetail = (double) data.get("id");
                        Movie movie;
                        movie = Main.databaseConnection.getMovieById(idDetail.intValue());
                        if (movie == null) {
                            movie = Main.connectionREST.getMovie(idDetail.intValue());
                        }
                        returnData.putAll(movie.toStringMap());
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "FavouriteMovies":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "FavouriteMovies");
                            break;
                        }
                        returnData.put("Action", "FavouriteMovies");
                        ArrayList<Movie> moviesObjects = Main.databaseConnection.getListOfFavourites((String) data.get("Username"));
                        ArrayList<Object> favouriteMovies = new ArrayList<>();
                        for (Movie favouriteMovie : moviesObjects) {
                            favouriteMovies.add(favouriteMovie.toStringMap());
                        }
                        returnData.put("FavouriteMovies", favouriteMovies);
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "AddFavouriteMovie":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "AddFavouriteMovie");
                            break;
                        }
                        Double idAddFavourite = (double) data.get("id");
                        Main.databaseConnection.addFavouriteMovie((String) data.get("Username"), idAddFavourite.intValue());
                        break;
                    case "RemoveFavouriteMovie":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "RemoveFavouriteMovie");
                            break;
                        }
                        Double idRemoveFavourite = (double) data.get("id");
                        Main.databaseConnection.removeFavouriteMovie((String) data.get("Username"), idRemoveFavourite.intValue());
                        break;
                    case "RateMovie":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "RateMovie");
                            break;
                        }
                        Double idRate = (double) data.get("id");
                        Main.databaseConnection.rateMovie((String) data.get("Username"), idRate.intValue(), (double) data.get("Rate"));
                        break;
                    case "CommentMovie":
                        if (!authenticate((String) data.get("Token"))) {
                            sendAlert("Wrong authentication!", "CommentMovie");
                            break;
                        }
                        Double idCommentMovie = (double) data.get("id");
                        Main.databaseConnection.commentMovie((String) data.get("Username"), idCommentMovie.intValue(), (String) data.get("Comment"));
                        break;
                    case "SearchMovie":
                        returnData.put("Action", "SearchMovie");
                        returnData.put("SearchList", Main.connectionREST.searchMovie((String) data.get("SearchField")));
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "GetTrailer":
                        returnData.put("Action", "GetTrailer");
                        returnData.put("VideoURL", Main.connectionREST.getTrailer((String) data.get("MovieName")));
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    case "GetTrailerSearched":
                        returnData.put("Action", "GetTrailerSearched");
                        returnData.put("VideoURL", Main.connectionREST.getTrailer((String) data.get("FieldText")));
                        sendSmtToClient(Main.gson.toJson(returnData));
                        break;
                    default:
                        sendAlert("Wrong Action");
                        break;
                }
                logAction((String) data.get("Action"));
                returnData.clear();
                data.clear();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client is disconnected");
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            ServerConnection.removeClient(clientSocket);
        }
    }

    private void sendAlert(String message, String fromAction) {
        StringMap<Object> returnData = new StringMap<>();
        returnData.put("Action", "Alert");
        if (fromAction != null) {
            returnData.put("FromAction", fromAction);
        }
        returnData.put("Message", message);
        logAction("Alert", message);
        sendSmtToClient(Main.gson.toJson(returnData));
    }

    private void sendAlert(String message) {
        sendAlert(message, null);
    }

    private void logAction(String action, String message) {
        Log log;
        if (message != null) {
            log = new Log(clientSocket.getRemoteSocketAddress().toString(), action, this.authToken != null);
        } else {
            log = new Log(clientSocket.getRemoteSocketAddress().toString(), action, message, this.authToken != null);
        }
        Main.serverLogger.addAction(log);
    }

    private void logAction(String action) {
        logAction(action, null);
    }

    private void sendSmtToClient(String json) {
        try {
            outToClient.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate(String token) {
        if (authToken == null && token == null) return false;
        return authToken.equals(token);
    }
}
