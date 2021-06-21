package entity;

import java.util.Map;

/**
 * @author lane
 * @date 2021年05月10日 下午3:14
 */
public class Mapper {

    // Service 中的 Host
    private Map<String, Host> hostMap;

    public Mapper(Map<String, Host> hostMap) {
        this.hostMap = hostMap;
    }

    public Map<String, Host> getHostMap() {
        return hostMap;
    }

    public void setHostMap(Map<String, Host> hostMap) {
        this.hostMap = hostMap;
    }


}
