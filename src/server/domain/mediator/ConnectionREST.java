package server.domain.mediator;

import com.google.gson.internal.StringMap;
import server.Main;
import server.domain.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by aykon on 10-May-17.
 */
public class ConnectionREST {


    public ArrayList<StringMap<Object>> getLatestMovies() {
        String urlAddress = "movie/popular";
        String output = this.getRequest(urlAddress, "");
        StringMap<Object> response = Main.gson.fromJson(output, StringMap.class);
        ArrayList<StringMap<Object>> fullLatestMoviesList = (ArrayList) response.get("results");
        //cleaning unnecessary data
        ArrayList<StringMap<Object>> latestMoviesList = new ArrayList<>();
        for (StringMap<Object> movie : fullLatestMoviesList) {
            Movie movieObject = new Movie(movie);
            latestMoviesList.add(movieObject.toStringMap());
        }
        return latestMoviesList;
    }

    public Movie getMovie(int id) {
        String urlAddress = "movie/" + id;
        String output = this.getRequest(urlAddress, "");
        StringMap<Object> response = Main.gson.fromJson(output, StringMap.class);
        return new Movie(response);
    }


    public ArrayList<StringMap<Object>> searchMovie(String name) {
        String urlAddress = "search/movie";
        String output = null;
        try {
            output = this.getRequest(urlAddress, "&query=" + URLEncoder.encode(name, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringMap<Object> response = Main.gson.fromJson(output, StringMap.class);
        ArrayList<StringMap<Object>> fullSearchResult = (ArrayList) response.get("results");
        //cleaning unnecessary data
        ArrayList<StringMap<Object>> searchResult = new ArrayList<>();
        for (StringMap<Object> movie : fullSearchResult) {
            Movie movieObject = new Movie(movie);
            searchResult.add(movieObject.toStringMap());
        }
        return searchResult;
    }

    public String getTrailer(String title) {
        String output = null;
        try {
            URL url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" +
                    URLEncoder.encode(title, "UTF-8") +
                    "+trailer&type=video&key=AIzaSyDuTI4P28XHLbygh3-50h5TIhPlt3ahAys");
            output = this.getRequest(url);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringMap<Object> results = Main.gson.fromJson(output, StringMap.class);
        ArrayList<StringMap<Object>> items = (ArrayList<StringMap<Object>>) results.get("items");
        StringMap<Object> IDItem = (StringMap<Object>) items.get(0).get("id");
        return "https://www.youtube.com/embed/" + IDItem.get("videoId");
    }

    private String getRequest(String urlAddress, String getParameters) {
        String output = null;
        try {
            String baseUrl = "https://api.themoviedb.org/3/";
            String apiKey = "?api_key=b97edb3572a6a9f660d0b90dc10453b6";
            URL url = new URL(baseUrl + urlAddress + apiKey + getParameters);
            output = this.getRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return output;
    }

    private String getRequest(URL urlAddress) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) urlAddress.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = bufferedReader.readLine()) != null) {
                stringBuilder.append(output);
            }

            conn.disconnect();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return stringBuilder.toString();
    }
}

