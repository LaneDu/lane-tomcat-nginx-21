package entity;

import server.HttpServlet;

import java.util.Map;

/**
 * @author lane
 * @date 2021年05月10日 下午3:29
 */
public class Context {


    private Map<String, HttpServlet> servletMap;


    public Context() {
    }

    public Context(Map<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
    }

    public Map<String, HttpServlet> getServletMap() {
        return servletMap;
    }

    public void setServletMap(Map<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
    }
}
