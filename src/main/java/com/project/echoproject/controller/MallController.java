package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mall")
@Controller
public class MallController {
    @GetMapping("")
    public String mallHome() {
        return "mall";
    }
}
