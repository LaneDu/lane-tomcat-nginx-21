package server;

import entity.Context;
import entity.Host;
import entity.Mapper;
import entity.Server;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author lane
 * @date 2021年05月10日 下午5:18
 */

    public class RequestProcessorHomework extends Thread {

        private Socket socket;

        private Server server;

        public RequestProcessorHomework() {

        }

        public RequestProcessorHomework(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                OutputStream outputStream = socket.getOutputStream();
                // 分别封装 Request 和 Response
                Request request = new Request(socket.getInputStream());
                Response response = new Response(outputStream);
                HttpServlet httpServlet = findHttpServlet(request);
                if (httpServlet == null) {
                    response.outputHtml(request.getUrl());
                } else {
                    httpServlet.service(request, response);
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 根据请求信息找到对应业务 Servlet
         * <pre>
         *  GET web-greet/greet HTTP/1.1
         *  Host: suremotoo.com
         * </pre>
         *
         * @param request
         * @return 具体要执行的 servlet
         */
        private HttpServlet findHttpServlet(Request request) {
            HttpServlet businessServlet = null;
            Map<String, Mapper> serviceMap = server.getServiceMap();
            for (String key : serviceMap.keySet()) {
                String hostName = request.getHost();
//                String hostName = "localhost";
                Map<String, Host> hostMap = serviceMap.get(key).getHostMap();
                Host host = hostMap.get(hostName);
                if (host != null) {
                    Map<String, Context> contextMap = host.getContextMap();
                    // 处理 url
                    // eg: web-demo-1/myServlet
                    String url = request.getUrl();
                    String[] urlPattern = url.split("/");
                    String contextName = urlPattern[1];
                    String servletStr = "/";
                    if (urlPattern.length > 2) {
                        servletStr += urlPattern[2];
                    }
                    // 获取上下文
                    Context context = contextMap.get(contextName);
                    if (context != null) {
                        Map<String, HttpServlet> servletMap = context.getServletMap();
                        businessServlet = servletMap.get(url);
                        return businessServlet;
                    }
                }
            }
            return businessServlet;
        }
    }