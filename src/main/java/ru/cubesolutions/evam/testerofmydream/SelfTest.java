package ru.cubesolutions.evam.testerofmydream;

import com.evam.utils.model.domain.Event;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.topology.client.EventSenderManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Garya on 18.01.2018.
 */
public class SelfTest extends AbstractOutputAction {

    private final static Logger log = Logger.getLogger(SelfTest.class);

    private ActionConfig config = new ActionConfig();

    @Override
    public synchronized void init() {
        isInited = false;
        Properties props = new Properties();
        try (InputStream is = new FileInputStream("./conf/self-test.properties")) {
            props.load(is);
            config.setJdbcDriverClass(props.getProperty("jdbc-driver"));
            config.setJdbcUrl(props.getProperty("jdbc-url"));
            config.setDbUser(props.getProperty("jdbc-user"));
            config.setDbPassword(props.getProperty("jdbc-password"));
        } catch (Throwable t) {
            log.error("File config self-test.properties not found", t);
            System.out.println("File config self-test.properties not found");
            return;
        }
        isInited = true;
    }

    @Override
    public int execute(OutputActionContext outputActionContext) throws Exception {
        String actorIdLabel = (String) outputActionContext.getParameter("actor_id_label");
        log.debug("actor_id_label is " + actorIdLabel);
        String scenarioName = (String) outputActionContext.getParameter("scenario_name");
        log.debug("scenario_name is " + scenarioName);
        String eventName = (String) outputActionContext.getParameter("event_name");
        log.debug("event_name is " + eventName);
        String sql = (String) outputActionContext.getParameter("sql");
        log.debug("sql is " + sql);

        config.setActorIdLabel(actorIdLabel);
        config.setScenarioName(scenarioName);
        config.setEventName(eventName);
        config.setSql(sql);
        List<Map<String, String>> events = DB.getLabelsWithValues(config);
        for (Map<String, String> params : events) {
            send(params);
        }
        return 0;
    }

    private void send(Map<String, String> params) {
        if (params.isEmpty()) {
            return;
        }
        Event event = new Event(config.getEventName(),
                params.get(config.getActorIdLabel()),
                config.getScenarioName(),
                params
        );
        try {
            log.info("event is sendig: " + eventParamsTtoString(params));
            EventSenderManager.getInstance().sendEvent(event);
            log.info("event is sent");
        } catch (Exception e) {
            log.error("error while sending event", e);
            e.printStackTrace();
        }
    }

    private String eventParamsTtoString(Map<String, String> params) {
        if (params.isEmpty()) {
            return "not found";
        }
        StringBuilder result = new StringBuilder("a," + params.get(config.getActorIdLabel()) + ","
                + config.getScenarioName() + ","
                + config.getEventName() + ",");
        params.forEach((k, v) -> result.append(k).append(",").append(v));
        return result.toString();
    }

    @Override
    protected ArrayList<IOMParameter> getParameters() {
        ArrayList<IOMParameter> params = new ArrayList<>();
        params.add(new IOMParameter("actor_id_label", "поле, которое мы считаем actor id"));
        params.add(new IOMParameter("scenario_name", "имя сценария"));
        params.add(new IOMParameter("event_name", "имя события"));
        params.add(new IOMParameter("sql", "sql"));
        return params;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
