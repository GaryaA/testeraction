package ru.cubesolutions.evam.testerofmydream;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Garya on 12.11.2017.
 */
public class Config {

    private final static Logger log = Logger.getLogger(Config.class);

    public final static String JDBC_DRIVER;
    public final static String JDBC_URL;
    public final static String JDBC_USER;
    public final static String JDBC_PASSWORD;

    public final static String SQL;

    public final static String ACTOR_ID_LABEL;
    public final static String EVENT_NAME;
    public final static String SCENARIO_NAME;


    static {
        Properties props = new Properties();
        try (InputStream input = Config.class.getResourceAsStream("/db.properties")) {
            props.load(input);
        } catch (Throwable t) {
            log.error("File config db.properties not found", t);
            System.out.println("File config db.properties not found");
            throw new RuntimeException(t);
        }
        JDBC_DRIVER = props.getProperty("jdbc-driver");
        JDBC_URL = props.getProperty("jdbc-url");
        JDBC_USER = props.getProperty("jdbc-user");
        JDBC_PASSWORD = props.getProperty("jdbc-password");
        SQL = props.getProperty("sql");
        ACTOR_ID_LABEL = props.getProperty("actor-id");
        EVENT_NAME = props.getProperty("event-name");
        SCENARIO_NAME = props.getProperty("scenario-name");
    }

}
