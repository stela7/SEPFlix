package server.domain.mediator;

import server.Main;
import server.domain.model.Comment;
import server.domain.model.Movie;
import server.domain.model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by martin on 15/05/2017.
 */
public class DatabaseConnection {

    private final String URL = DBParameters.getURL();
    private final String USER = DBParameters.getUSER();
    private final String PASSWORD = DBParameters.getPASSWORD();
    private final String DATABASE = DBParameters.getDATABASE();
    private static DatabaseConnection databaseConnection;
    private Connection connection = null;

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + URL + DATABASE
                    , USER
                    , PASSWORD);
            connection.setSchema("public");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getDatabaseConnection() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    public void registerUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users " +
                            "(user_name, name, surname, email, password)" +
                            "VALUES (?,?,?,?,?)"
            );
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> usersList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM users;"
            );

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usersList.add(new User(resultSet, false));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public User getUserByUserName(String userName) {
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE user_name = ? LIMIT 1;"
            );
            statement.setString(1, userName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet, false);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void removeUserByUserName(String userName) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM users WHERE user_name = ?;"
            );
            statement.setString(1, userName);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserInformation(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users " +
                            "SET name = ?, surname = ?, email = ?" +
                            "WHERE user_name = ?;"
            );
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserName());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users " +
                            "SET  password = ?" +
                            "WHERE user_name = ?;"
            );
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getUserName());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getListOfFavourites(String username) {
        ArrayList<Movie> favouriteMovies = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT " +
                            "  movie.* " +
                            "FROM favourite_movies favmovies " +
                            "  LEFT JOIN movies movie ON (movie.id_movie = favmovies.id_movie) " +
                            "WHERE user_name = ?;"
            );
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                favouriteMovies.add(new Movie(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favouriteMovies;
    }


    public void addFavouriteMovie(String userName, int movie_id) {
        try {
            if (getMovieById(movie_id) == null) {
                addMovie(Main.connectionREST.getMovie(movie_id));
            }
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO favourite_movies (id_movie, user_name) VALUES (?,?);"
            );
            statement.setInt(1, movie_id);
            statement.setString(2, userName);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFavouriteMovie(String userName, int movie_id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM favourite_movies " +
                            "WHERE id_movie = ? AND user_name = ?; "
            );
            statement.setInt(1, movie_id);
            statement.setString(2, userName);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void rateMovie(String userName, int movie_id, double rate) {
        try {
            if (getMovieById(movie_id) == null) {
                addMovie(Main.connectionREST.getMovie(movie_id));
            }
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO rate_movies (id_movie, user_name, rate_sepflix) VALUES (?, ?, ?);"
            );
            statement.setInt(1, movie_id);
            statement.setString(2, userName);
            statement.setDouble(3, rate);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commentMovie(String userName, int movie_id, String comment) {
        try {
            if (getMovieById(movie_id) == null) {
                addMovie(Main.connectionREST.getMovie(movie_id));
            }
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO comment_movies (id_movie, user_name, comment) VALUES (?, ?, ?);"
            );
            statement.setInt(1, movie_id);
            statement.setString(2, userName);
            statement.setString(3, comment);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Comment> getMovieComments(int movie_id) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM comment_movies WHERE id_movie = ?;"
            );
            statement.setInt(1, movie_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(new Comment(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private void addMovie(Movie movie) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO movies " +
                            "(id_movie, poster, title, genres, overview, release_year, rating_imdb)" +
                            "VALUES (?,?,?,?,?,?,?);"
            );
            statement.setInt(1, movie.getId());
            statement.setString(2, movie.getPoster());
            statement.setString(3, movie.getTitle());
            statement.setString(4, movie.getGenres());
            statement.setString(5, movie.getOverview());
            statement.setDate(6, movie.getSQLReleaseYear());
            statement.setDouble(7, movie.getRatingImbd());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Movie getMovieById(int movie_id) {
        Movie movie = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM movies WHERE id_movie = ? LIMIT 1;"
            );
            statement.setInt(1, movie_id);

            ResultSet resultSetMovies = statement.executeQuery();

            if (resultSetMovies.next()) {
                movie = new Movie(resultSetMovies);
                movie.setComments(getMovieComments(movie_id));
            }
            resultSetMovies.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (movie != null) {
            movie.setComments(getMovieComments(movie_id));
        }
        return movie;
    }
}
