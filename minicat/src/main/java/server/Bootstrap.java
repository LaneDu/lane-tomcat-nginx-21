package server;

import entity.Context;
import entity.Host;
import entity.Mapper;
import entity.Server;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * minicat的主类
 * @author lane
 * @date 2021年05月06日 下午3:07
 */
public class Bootstrap {

    //定义端口号
    private  int port = 8080;

    private Map<String,Mapper> serviceMap = new HashMap<>();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 启动初始化
     * minicat1.0版本的需求，请求返回字符串
     * @author lane
     * @date 2021/5/6 下午3:10
     */
    public void startV1() throws IOException {


        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            //有了socket,接收请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "hello minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length)+data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }

    }
    /**
     * 启动初始化
     * minicat2.0版本的需求，请求返回html
     * @author lane
     * @date 2021/5/6 下午3:10
     */
    public void startV2() throws IOException {


        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());
            socket.close();
        }

    }
    /**
     * 启动初始化
     * minicat3.0版本的需求，请求返回动态资源Servlet
     * @author lane
     * @date 2021/5/6 下午3:10
     */
    public void startV3() throws IOException {

        loadServlet();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            if (servletMap.get(request.getUrl())==null){
                response.outputHtml(request.getUrl());
            }else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();
        }

    }
    /**
     * 启动初始化
     * minicat4.0版本的需求，请求返回动态资源Servlet,多线程不阻塞
     * @author lane
     * @date 2021/5/6 下午6:40
     */
    public void startV4() throws IOException {

        loadServlet();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);

            requestProcessor.start();
        }

    }
    // 定义一个线程池
    int corePoolSize = 10;
    int maximumPoolSize =50;
    long keepAliveTime = 100L;
    TimeUnit unit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            threadFactory,
            handler
    );

    /**
     * 启动初始化
     * minicat4.10版本的需求，请求返回动态资源Servlet,多线程不阻塞，使用线程池
     * @author lane
     * @date 2021/5/6 下午6:40
     */
    public void startV5() throws IOException {

        loadServlet();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            threadPoolExecutor.execute(requestProcessor);

        }

    }
    /**
     * 启动初始化
     * minicat6.0版本的需求,加载外部项目
     * @author lane
     * @date 2021/5/6 下午6:40
     */
    public void startV6() throws IOException {

        loadServletHomework();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat启动的端口号为："+port);
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessorHomework requestProcessor = new RequestProcessorHomework(socket,new Server(serviceMap));
//            threadPoolExecutor.execute(requestProcessor);
            requestProcessor.start();

        }

    }
    /**
     * 启动入口
     * @author lane
     * @date 2021/5/6 下午3:10
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {

            //启动minicat
//        bootstrap.startV1();
//        bootstrap.startV2();
//        bootstrap.startV3();
//        bootstrap.startV4();
//        bootstrap.startV5();
        bootstrap.startV6();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>myServlet</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.myServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /myServlet
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }



        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载实际项目里配置的 Servlet
     *
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public Context loadContextServlet(String path) throws Exception {
        String webPath = path + "/web.xml";
        if (!(new File(webPath).exists())) {
            System.out.println("not found " + webPath);
            return null;
        }
        InputStream resourceAsStream = new FileInputStream(webPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(resourceAsStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//servlet");
        Map<String, HttpServlet> servletMap = new HashMap<>(16);
        for (Element element : list) {
            // <servlet-name>show</servlet-name>
            Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
            String servletName = servletnameElement.getStringValue();
            // <servlet-class>server.ShowServlet</servlet-class>
            Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
            String servletClass = servletclassElement.getStringValue();

            // 根据 servlet-name 的值找到 url-pattern
            Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
            // /show
            String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
            // 自定义类加载器，来加载 webapps 目录下的 class
            WebClassLoader webClassLoader = new WebClassLoader();
            Class<?> aClass = webClassLoader.findClass(path, servletClass);
            servletMap.put(urlPattern, (HttpServlet) aClass.getDeclaredConstructor().newInstance());
        }
        return new Context(servletMap);
    }

    /**
     * 加载 server.xml，解析并初始化 webapps 下面的各个项目的 servlet
     */
    @SuppressWarnings({"unchecked"})
    public void loadServletHomework() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            // 解析 server 标签
            Element serverElement = (Element) rootElement.selectSingleNode("//Server");
            // 解析 server 下的 Service 标签
            List<Element> serviceNodes = serverElement.selectNodes("//Service");
            // 存储各个 Host
            Map<String, Host> hostMap = new HashMap<>(8);
            //遍历 service
            for (Element service : serviceNodes) {
                String serviceName = service.attributeValue("name");
                Element engineNode = (Element) service.selectSingleNode("//Engine");
                List<Element> hostNodes = engineNode.selectNodes("//Host");
                // 存储有多少个项目
                Map<String, Context> contextMap = new HashMap<>(8);
                for (Element hostNo : hostNodes) {
                    String hostName = hostNo.attributeValue("name");
                    String appBase = hostNo.attributeValue("appBase");
                    File file = new File(appBase);
                    if (!file.exists() || file.list() == null) {
                        break;
                    }
                    String[] list = file.list();
                    //遍历子文件夹，即：实际的项目列表
                    for (String path : list) {
                        //将项目封装成 context，并保存入map
                        if (path==null||path.contains("."))continue;
                        contextMap.put(path, loadContextServlet(appBase + "/" + path));
                    }
                    // hsot:port
                    // eg: localhost:8080
                    hostMap.put(hostName + ":" + port, new Host(contextMap));
                }
                serviceMap.put(serviceName, new Mapper(hostMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
