package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "error/access_denied";
    }
}
