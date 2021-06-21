package server;

/**
 * @author lane
 * @date 2021年05月06日 下午5:17
 */
public interface Servlet {

    public void init() throws Exception;
    public void destory() throws Exception;
    public void service(Request request,Response response) throws Exception;

}



