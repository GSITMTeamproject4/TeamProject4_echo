package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/notice")
@Controller
public class NoticeController {
    @GetMapping("/list")
    public String hello() {
        return "notice_list";
    }
}
