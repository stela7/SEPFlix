package server.domain.model;

import com.google.gson.internal.StringMap;
import org.junit.Before;
import org.junit.Test;
import server.domain.mediator.DatabaseConnection;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by andreea on 5/28/2017.
 */
public class MovieTest {
    private Movie movie;
    private DatabaseConnection databaseConnection;
    private StringMap<Object> movieMap;

    @Before
    public void setUp() throws Exception {
        StringMap<Object> gender1 = new StringMap<>();
        StringMap<Object> gender2 = new StringMap<>();
        StringMap<Object> gender3 = new StringMap<>();
        StringMap<Object> gender4 = new StringMap<>();
        gender1.put("name", "Music");
        gender2.put("name", "Family");
        gender3.put("name", "Fantasy");
        gender4.put("name", "Romance");
        ArrayList<StringMap<Object>> gendersList = new ArrayList<>();
        gendersList.add(gender1);
        gendersList.add(gender2);
        gendersList.add(gender3);
        gendersList.add(gender4);
        movieMap = new StringMap<>();
        movieMap.put("id", 335797.00);
        movieMap.put("poster_path", "/tWqifoYuwLETmmasnGHO7xBjEtt.jpg");
        movieMap.put("title", "Beauty and the Beast");
        movieMap.put("overview", "A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.");
        movieMap.put("genres", gendersList);
        movieMap.put("release_date", "2017-03-16");
        movieMap.put("vote_average", 6.80);
        movieMap.put("vote_sepflix", 5.00);
        StringMap<Object> user = new StringMap<>();
        user.put("Username", "a");
        user.put("Password", "a");
        user.put("FirstName", "a");
        user.put("SecondName", "a");
        user.put("Email", "aa@yahoo.com");

        User user1 = new User(user, true);
        Comment c1 = new Comment("good", 335797, user1.getUserName());
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(c1);
        ArrayList<StringMap<Object>> commentsMap = new ArrayList<>();
        commentsMap.add(c1.toStringMap());
        movieMap.put("comments", commentsMap);

        this.movie = new Movie(movieMap);
    }

    @Test
    public void getId() throws Exception {
        this.movieMap.put("id", 12.00);
        this.movieMap.put("id", 22.00);
        this.movie = new Movie(movieMap);
        assertEquals(12.00, 12.00, this.movie.getId());
        assertEquals(335797.00, 335797.00, this.movie.getId());
        assertEquals(22.00, 22.00, this.movie.getId());
        assertNotEquals(111, this.movie.getId());
        try {
            this.movieMap.put("id", null);
            this.movie = new Movie(movieMap);
            assertEquals(null, this.movie.getId());
        } catch (NullPointerException e) {
        }
        try {
            this.movieMap.put("id", "123");
            this.movie = new Movie(movieMap);
            assertEquals("123", this.movie.getId());
        } catch (ClassCastException e) {
        }
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals("Beauty and the Beast", this.movie.getTitle());
        assertNotEquals("BEAUTY AND THE BEAST", this.movie.getTitle());
        try {
            this.movieMap.put("title", null);
            this.movie = new Movie(movieMap);
            assertEquals(null, this.movie.getTitle());
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void getPoster() throws Exception {
        assertEquals("/tWqifoYuwLETmmasnGHO7xBjEtt.jpg", this.movie.getPoster());
        assertNotEquals(111, this.movie.getPoster());
        assertNotEquals("/tWqifoYuwLETmmasnGHO7xBjEtt.jpg".toLowerCase(), this.movie.getPoster());
    }

    @Test
    public void getOverview() throws Exception {
        assertEquals("A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.", this.movie.getOverview());
        assertNotEquals(null, this.movie.getOverview());
    }

    @Test
    public void getGenres() throws Exception {
        assertEquals("Music, Family, Fantasy, Romance", this.movie.getGenres());
        assertNotEquals("Music, Family, Fantasy", this.movie.getOverview());
        try {
            this.movieMap.put("genres", null);
            this.movie = new Movie(movieMap);
            assertEquals(null, this.movie.getGenres());
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void setComments() throws Exception {
        Comment c1 = new Comment("good", 335797, "a");
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(c1);
        this.movie.setComments(comments);
        assertEquals("good", this.movie.getComment());
        assertNotEquals("GOOD", this.movie.getComment());
    }

    @Test
    public void getRatingImbd() throws Exception {
        assertEquals(6.80, 6.80, this.movie.getRatingImbd());
        assertNotEquals(10.00, this.movie.getRatingImbd());
    }

    @Test
    public void toStringMap() throws Exception {
        StringMap<Object> result = new StringMap<>();
        StringMap<Object> gender1 = new StringMap<>();
        StringMap<Object> gender2 = new StringMap<>();
        StringMap<Object> gender3 = new StringMap<>();
        StringMap<Object> gender4 = new StringMap<>();
        gender1.put("name", "Music");
        gender2.put("name", "Family");
        gender3.put("name", "Fantasy");
        gender4.put("name", "Romance");
        ArrayList<StringMap<Object>> gendersList = new ArrayList<>();
        gendersList.add(gender1);
        gendersList.add(gender2);
        gendersList.add(gender3);
        gendersList.add(gender4);
        result.put("id", 335797.00);
        result.put("poster_path", "/tWqifoYuwLETmmasnGHO7xBjEtt.jpg");
        result.put("title", "Beauty and the Beast");
        result.put("overview", "A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.");
        result.put("genres", gendersList);
        result.put("release_date", "2017-03-16");
        result.put("vote_average", 6.80);

        Movie movie2 = new Movie(result);
        assertEquals(movie2.toStringMap(), this.movie.toStringMap());
    }


    @Test
    public void getSQLReleaseYear() throws Exception {
        assertEquals(Date.valueOf("2017-03-16"), this.movie.getSQLReleaseYear());
        assertNotEquals("2017 - 03 - 16", this.movie.getSQLReleaseYear());
        assertNotEquals(2017 - 03 - 16, this.movie.getSQLReleaseYear());
        try {
            this.movieMap.put("release_date", 2017 - 03 - 16);
            this.movie = new Movie(movieMap);
            assertEquals(2017 - 03 - 16, this.movie.getSQLReleaseYear());
        } catch (ClassCastException e) {
        }
    }
}