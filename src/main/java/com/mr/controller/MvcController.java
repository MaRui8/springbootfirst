package com.mr.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MvcController {

    @RequestMapping("/")
    public String index1(){
        return "index";
    }

}
