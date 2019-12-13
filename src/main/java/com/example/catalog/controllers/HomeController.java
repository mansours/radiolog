package com.example.catalog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {

    @RequestMapping(method = GET, value = "/")
    public String home() {
        return "home";
    }

    @RequestMapping(method = GET, value = "admin")
    public String admin() {
        return "admin";
    }
}
