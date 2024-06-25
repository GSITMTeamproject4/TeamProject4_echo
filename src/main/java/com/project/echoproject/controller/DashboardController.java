package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private final DataController dataController;

    public DashboardController(DataController dataController) {
        this.dataController = dataController;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // 여기서 "seoul" 또는 "gyeonggi"를 region 파라미터로 전달합니다.
            Map<String, Object> jsonData = dataController.getData("seoul"); // 데이터를 가져옵니다.
            model.addAttribute("jsonData", jsonData); // 모델에 JSON 데이터를 추가합니다.
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리: 데이터를 가져오는 과정에서 문제가 발생할 경우 처리합니다.
            model.addAttribute("jsonData", new HashMap<>()); // 빈 객체로 처리할 수도 있습니다.
        }

        return "dashboard";
    }
}
