package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/index", "/", "/main"})
    public String index() {
        return "index";
    }

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment"; // src/main/resources/templates/payment.html 파일을 가리킴
    }
}
