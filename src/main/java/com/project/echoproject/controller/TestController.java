package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("hello")  // localhost:8080/hello
    @ResponseBody
    public String hello() {
        return "Hello World";
    }
}
