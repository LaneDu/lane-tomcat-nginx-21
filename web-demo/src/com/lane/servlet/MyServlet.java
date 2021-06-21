package com.lane.servlet;

import server.HttpProtocolUtil;
import server.HttpServlet;
import server.Request;
import server.Response;


import java.io.IOException;

/**
 * @author lane
 * @date 2021年05月06日 下午5:22
 */
public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>myservlet get</h1>";
        try {
            //阻塞睡眠下100s
//            Thread.sleep(100000);
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>myservlet post</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
