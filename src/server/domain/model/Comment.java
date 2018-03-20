package server.domain.model;

import com.google.gson.internal.StringMap;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Stela on 26.5.2017.
 */
public class Comment {
    private String comment;
    private int idMovie;
    private String username;

    /**
     * Constructor which contructs comment object from its parameters
     *
     * @param comment  comment text
     * @param idMovie  id of movie which was commented
     * @param username username of user who placed comment
     */
    public Comment(String comment, int idMovie, String username) {
        this.comment = comment;
        this.idMovie = idMovie;
        this.username = username;
    }

    /**
     * Constructor which constructs comment object from given resultset
     *
     * @param resultSet result set to construct comment from
     */
    public Comment(ResultSet resultSet) {
        try {
            this.idMovie = resultSet.getInt("id_movie");
            this.comment = resultSet.getString("comment");
            this.username = resultSet.getString("user_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return comment text
     */
    public String getComment() {
        return comment;
    }

    /**
     * Converts comment object to StrinMap object
     * @return StringMap with following set of keys: user_name, comment, id_Movie
     */
    public StringMap<Object> toStringMap() {
        StringMap<Object> comment = new StringMap<>();
        comment.put("user_name", this.username);
        comment.put("comment", this.comment);
        comment.put("id_Movie", this.idMovie);
        return comment;
    }
}
