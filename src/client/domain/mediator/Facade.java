package client.domain.mediator;

import com.google.gson.internal.StringMap;
import server.domain.model.User;

/**
 * Created by Stela on 17.5.2017.
 */
public class Facade {


    /**
     * Adds given movie to users favorites
     *
     * @param user  user to add movie to
     * @param movie movie to add
     */
    public static void addMovieToFavourites(User user, StringMap<Object> movie) {
        user.addFavourite(movie);
    }

    /**
     * @return username of given user
     */
    public static String getUsername(User user) {
        return user.getUserName();
    }

    /**
     *@return name of given user
     */
    public static String getFirstName(User user) {
        return user.getFirstName();
    }

    /**
     *@return surname of given user
     */
    public static String getLastName(User user) {
        return user.getLastName();
    }

    /**
     *@return email of given user
     */
    public static String getEmail(User user) {
        return user.getEmail();
    }

    /**
     * Removes movie from users favorites
     *
     * @param user user to remove movie from
     * @param id   id of movie to be removed
     */
    public static void removeFromFavourites(User user, Double id) {
        user.removeFavourite(id);
    }
}
