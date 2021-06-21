package com.galaxy.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * @author lane
 * @date 2021年05月15日 下午3:36
 */
public class RequestInterceptor implements HandlerInterceptor {


    /**
     * handle执行之前拦截
     * 判断是否在session中存储了用户信息
     *
     * @author lane
     * @date 2021/5/15 下午3:46
     * @param request
     * @param response
     * @param handler
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        //获取sessionID
        String sessionId = session.getId();
        //获取session中的信息
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()){
            // 获取session键值
            String name = attributeNames.nextElement().toString();
            Object attribute = session.getAttribute(name);
            System.out.println("------" + name + ":" + attribute +"--------\n");

        }
        Object username = session.getAttribute("username");
        System.out.println("当前用户为："+username);
        System.out.println("当前URL为："+request.getRequestURI());

        if (username==null){
            System.out.println("返回登陆页面");
            response.sendRedirect(request.getContextPath() +"/login/toLogin");
            return false;

        }else {

            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
