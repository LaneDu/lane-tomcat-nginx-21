package com.galaxy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lane
 * @date 2021年05月15日 下午3:28
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/result")
    public String homePage(){

        return "result";

    }
    
}
