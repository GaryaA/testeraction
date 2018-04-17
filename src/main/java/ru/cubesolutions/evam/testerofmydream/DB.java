package ru.cubesolutions.evam.testerofmydream;

import com.evam.utils.model.domain.Event;
import com.intellica.evam.topology.client.EventSenderManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Garya on 22.01.2018.
 */
public class DB {

    private final static Logger log = Logger.getLogger(DB.class);

    public static List<Map<String, String>> getLabelsWithValues(ActionConfig config) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Class.forName(config.getJdbcDriverClass());
            try (Connection connection = DriverManager.getConnection(config.getJdbcUrl(), config.getDbUser(), config.getDbPassword())) {
                List<ColumnMeta> columnMetas = new ArrayList<>();
                log.debug("sql: " + config.getSql());
                PreparedStatement ps = connection.prepareStatement(
                        config.getSql()
                );
                ResultSet rs = ps.executeQuery();

                int mark = 0;
                while (rs.next()) {
                    ++mark;
                    Map<String, String> params = new HashMap<>();

                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                        ColumnMeta columnMeta = new ColumnMeta();
                        columnMeta.setLabel(metaData.getColumnLabel(i));
                        columnMeta.setName(metaData.getColumnName(i));
                        columnMeta.setType(metaData.getColumnTypeName(i));
                        columnMetas.add(columnMeta);
                    }

                    for (ColumnMeta columnMeta : columnMetas) {
                        params.put(columnMeta.getLabel(), "" + rs.getObject(columnMeta.getName()));
                    }

                    result.add(params);
                }
                if (mark == 0) {
                    log.info("Can't find any records");
                    return result;
                }

            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void main(String[] args) {
        send(getLabelsWithValues());
    }

    private static Map<String, String> getLabelsWithValues() {
        Map<String, String> result = new HashMap<>();
        try {
            Class.forName(Config.JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD)) {
                List<ColumnMeta> columnMetas = new ArrayList<>();
                PreparedStatement ps = connection.prepareStatement(
                        Config.SQL
                );
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                        ColumnMeta columnMeta = new ColumnMeta();
                        columnMeta.setLabel(metaData.getColumnLabel(i));
                        columnMeta.setName(metaData.getColumnName(i));
                        columnMeta.setType(metaData.getColumnTypeName(i));
                        columnMetas.add(columnMeta);
                    }

                    for (ColumnMeta columnMeta : columnMetas) {
                        result.put(columnMeta.getLabel(), "" + rs.getObject(columnMeta.getName()));
                    }

                    while (rs.next()) {
                        for (ColumnMeta columnMeta : columnMetas) {
                            result.put(columnMeta.getLabel(), "" + rs.getObject(columnMeta.getName()));
                        }
                    }
                    return result;
                } else {
                    System.out.println("Can't find any records");
                    return result;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void send(Map<String, String> params) {
        if (params.isEmpty()) {
            return;
        }
        Event event = new Event(Config.EVENT_NAME,
                params.get(Config.ACTOR_ID_LABEL),
                Config.SCENARIO_NAME,
                params
        );
        try {
            System.out.println("event is sendig: " + eventParamsTtoString(params));
            EventSenderManager.getInstance().sendEvent(event);
            System.out.println("event is sent");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String eventParamsTtoString(Map<String, String> params) {
        if (params.isEmpty()) {
            return "not found";
        }
        StringBuilder result = new StringBuilder("a," + params.get(Config.ACTOR_ID_LABEL) + ","
                + Config.SCENARIO_NAME + ","
                + Config.EVENT_NAME + ",");
        params.forEach((k, v) -> result.append(k + "," + v + ","));
        result.delete(result.length() - 1, result.length()).append("~");
        return result.toString();
    }


}
