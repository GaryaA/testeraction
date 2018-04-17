package ru.cubesolutions.evam.testerofmydream;

/**
 * Created by Garya on 20.02.2018.
 */
public class ActionConfig {

    private String jdbcDriverClass;
    private String jdbcUrl;
    private String dbUser;
    private String dbPassword;

    private String sql;

    private String actorIdLabel;
    private String eventName;
    private String scenarioName;

    public String getJdbcDriverClass() {
        return jdbcDriverClass;
    }

    public void setJdbcDriverClass(String jdbcDriverClass) {
        this.jdbcDriverClass = jdbcDriverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getActorIdLabel() {
        return actorIdLabel;
    }

    public void setActorIdLabel(String actorIdLabel) {
        this.actorIdLabel = actorIdLabel;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }
}
