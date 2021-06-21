package entity;

import java.util.Map;

/**
 * @author lane
 * @date 2021年05月10日 下午3:40
 */
public class Server {

    // Server 里的 Service
    private Map<String, Mapper> serviceMap;

    public Server() {
    }

    public Server(Map<String, Mapper> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Map<String, Mapper> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, Mapper> serviceMap) {
        this.serviceMap = serviceMap;
    }
}