package server.domain.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Created by Stela on 25.5.2017.
 */
public class Log {
    private final SimpleStringProperty ip;
    private final SimpleStringProperty action;
    private final SimpleStringProperty message;
    private final SimpleBooleanProperty loggedIn;
    private final Date date;

    /**
     * Constructor for log object with message
     */
    public Log(String ip, String action, String message, Boolean loggedIn) {
        this.ip = new SimpleStringProperty(ip);
        this.action = new SimpleStringProperty(action);
        this.message = new SimpleStringProperty(message);
        this.loggedIn = new SimpleBooleanProperty(loggedIn);
        this.date = new Date();
    }

    /**
     * Constructor for log object with empty message
     */
    public Log(String ip, String action, Boolean loggedIn) {
        this.ip = new SimpleStringProperty(ip);
        this.action = new SimpleStringProperty(action);
        this.loggedIn = new SimpleBooleanProperty(loggedIn);
        this.message = new SimpleStringProperty();
        this.date = new Date();
    }

    /**
     * @return IP address from log
     */
    public String getIp() {
        return ip.get();
    }

    /**
     * @return action from log
     */
    public String getAction() {
        return action.get();
    }

    /**
     *
     * @return message from log
     */
    public String getMessage() {
        return message.get();
    }

    /**
     *
     * @return true if user was logged in when log was created
     */
    public Boolean getLoggedIn() {
        return loggedIn.get();
    }

    /**
     *
     * @return date from log
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return log converted to string
     */
    public String toString() {
        return "Log{" +
                "ip=" + ip.get() +
                ", action=" + action.get() +
                ", message=" + message.get() +
                ", loggedIn=" + loggedIn.get() +
                ", date=" + date.toString() +
                '}';
    }
}
