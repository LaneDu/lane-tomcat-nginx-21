package server;

/**
 * @author lane
 * @date 2021年05月06日 下午5:19
 */
public abstract class HttpServlet implements Servlet {

    public abstract void doGet(Request request,Response response);
    public abstract void doPost(Request request,Response response);

    public void service(Request request,Response response){
        if ("get".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }
        if ("post".equalsIgnoreCase(request.getMethod())){
            doPost(request,response);
        }
    }

}
