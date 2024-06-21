package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/notice")
@Controller
public class NoticeController {

    //게시글 목록
    @GetMapping("/list")
    public String list() {
        return "notice_list";
    }

    //id값으로 받아오기
    @GetMapping("/list/detail")
    public String detail() {
        return "notice_detail";
    }

    //글 작성 form





}
