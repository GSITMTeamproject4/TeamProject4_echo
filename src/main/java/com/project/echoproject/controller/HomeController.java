package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/index", "/", "/main"})
    public String index() {
        return "index";
    }

}
