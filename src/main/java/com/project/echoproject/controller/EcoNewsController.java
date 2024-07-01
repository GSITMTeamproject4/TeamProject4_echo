package com.project.echoproject.controller;

import com.project.echoproject.service.EcoNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class EcoNewsController {

    @Autowired
    private EcoNewsService ecoNewsService;

    @GetMapping("/ecoNews")
    public String getEcoNews(Model model) {
        List<Map<String, String>> newsList = ecoNewsService.searchNews("저탄소");

        model.addAttribute("newsList", newsList);

        return "eco_news";
    }
}
