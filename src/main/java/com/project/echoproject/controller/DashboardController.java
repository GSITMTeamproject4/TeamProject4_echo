package com.project.echoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value={"/", "/main", "/index", "index"})
@Controller
public class DashboardController {

    private final DataController dataController;

    public DashboardController(DataController dataController) {
        this.dataController = dataController;
    }

    @GetMapping(value = {"/index", "/", "/main"})
    public String dashboard(Model model) {
        try {
            // 서울 지역의 "gangnam" 서브지역 데이터를 가져옵니다.
            Map<String, Object> seoulGangnamData = dataController.getData("seoul", "gangnam");
            model.addAttribute("seoulGangnamData", seoulGangnamData);

            // 서울 지역의 "gangbuk" 서브지역 데이터를 가져옵니다.
            Map<String, Object> seoulGangbukData = dataController.getData("seoul", "gangbuk");
            model.addAttribute("seoulGangbukData", seoulGangbukData);

            // 경기 북부 지역 데이터를 가져옵니다.
            Map<String, Object> gyeonggiNorthData = dataController.getData("gyeonggi", "gyeongginorth");
            model.addAttribute("gyeonggiNorthData", gyeonggiNorthData);

            // 경기 중부 지역 데이터를 가져옵니다.
            Map<String, Object> gyeonggiCentralData = dataController.getData("gyeonggi", "gyeonggicentral");
            model.addAttribute("gyeonggiCentralData", gyeonggiCentralData);

            // 경기 남부 지역 데이터를 가져옵니다.
            Map<String, Object> gyeonggiSouthData = dataController.getData("gyeonggi", "gyeonggisouth");
            model.addAttribute("gyeonggiSouthData", gyeonggiSouthData);

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리: 데이터를 가져오는 과정에서 문제가 발생할 경우 처리합니다.
            model.addAttribute("errorMessage", "데이터를 가져오는 중 오류가 발생했습니다.");
        }

        return "index";
    }
}
