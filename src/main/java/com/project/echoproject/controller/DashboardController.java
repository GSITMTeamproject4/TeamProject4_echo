package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DataController dataController;

    public DashboardController(DataController dataController) {
        this.dataController = dataController;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 데이터 가져오기
        model.addAttribute("jsonData", dataController.getData());

        return "dashboard";
    }
}