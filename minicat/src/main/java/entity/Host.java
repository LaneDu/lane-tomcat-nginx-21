package entity;

import server.HttpServlet;

import java.util.Map;

/**
 * @author lane
 * @date 2021年05月10日 下午3:28
 */
public class Host {

    private Map<String, Context> contextMap;

    public Host(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }
}
