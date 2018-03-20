package server.domain.model;

import com.google.gson.internal.StringMap;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The class represents the movies
 * Created by andreea on 5/19/2017.
 */
public class Movie {
    private int id;
    private String poster;
    private String title;
    private String releaseYear;

    private String genres;
    private double ratingImbd;
    private double ratingSepFlix;
    private String overview;
    private ArrayList<Comment> comments = new ArrayList<>();


    /**
     * Constructor for a movie from StringMap
     *
     * @param data StringMap to create movie object from
     */
    public Movie(StringMap<Object> data) {
        Double idDouble = (Double) data.get("id");
        this.id = idDouble.intValue();
        this.poster = (String) data.get("poster_path");
        this.title = (String) data.get("title");
        this.releaseYear = (String) data.get("release_date");
        this.ratingImbd = (Double) data.get("vote_average");
        if (data.get("genres") != null) {
            for (StringMap<Object> genre : (ArrayList<StringMap<Object>>) data.get("genres")) {
                if (this.genres == null) {
                    this.genres = (String) genre.get("name");
                } else {
                    this.genres = this.genres + ", " + genre.get("name");
                }
            }
            this.overview = (String) data.get("overview");
        }
    }

    /**
     * Constructor for movie from a resultset
     *
     * @param resultSet result set to create movie from
     */
    public Movie(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id_movie");
            this.poster = resultSet.getString("poster");
            this.title = resultSet.getString("title");
            this.overview = resultSet.getString("overview");
            this.genres = resultSet.getString("genres");
            this.releaseYear = resultSet.getString("release_year");
            this.ratingImbd = resultSet.getDouble("rating_imdb");
            this.ratingSepFlix = resultSet.getDouble("rating_sepflix");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     *
     */
    public String getPoster() {
        return poster;
    }

    /**
     *
     *
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     *
     */
    public String getGenres() {
        return genres;
    }

    /**
     *
     *
     */
    public int getId() {
        return id;
    }

    /**
     *
     *
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     *
     *
     */
    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    /**
     *
     *
     */
    public String getComment() {
        return this.comments.get(0).getComment();
    }

    /**
     *
     * @return rating from external server
     */
    public double getRatingImbd() {
        return ratingImbd;
    }

    /**
     *
     * @return movie converted to String map with following keys: id, poster_path, title, release_date, vote_average, genres, overview, vote_sepflix, comments
     */
    public StringMap<Object> toStringMap() {
        ArrayList<StringMap<Object>> commentsMap = new ArrayList<>();
        StringMap<Object> movie = new StringMap<>();
        movie.put("id", this.id);
        movie.put("poster_path", this.poster);
        movie.put("title", this.title);
        movie.put("release_date", this.releaseYear);
        movie.put("vote_average", this.ratingImbd);
        if (this.genres != null) {
            movie.put("genres", this.genres);
            movie.put("overview", this.overview);
            movie.put("vote_sepflix", this.ratingSepFlix);
            for (Comment comment : comments) {
                commentsMap.add(comment.toStringMap());
            }
            movie.put("comments", commentsMap);
        }
        return movie;
    }

    /**
     *
     * @return movie object converted to string
     */
    public String toString() {
        return this.poster + ", title: " + this.title + ", release year: " + this.releaseYear + ", genres: " + this.genres + ", rating Imbd: " + this.ratingImbd + ", rating Sep Flix: " + this.ratingSepFlix + ", overview: " + this.overview;
    }

    /**
     *
     * @return Date object of release date
     */
    public Date getSQLReleaseYear() {
        return Date.valueOf(this.releaseYear);
    }
}
