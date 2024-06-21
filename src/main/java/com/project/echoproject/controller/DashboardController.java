package com.project.echoproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/dashboard")
@Controller
public class DashboardController {

    @GetMapping("")
    public String dashboard() {
        return "dashboard"; // dashboard.html을 반환
    }

    // Ajax 요청에 대한 핸들러
    @GetMapping("/data")
    public ResponseEntity<String> fetchData() {
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // API 요청 URL
        String apiUrl = "https://apis.data.go.kr/1611000/BldEngyService/getBeGasUsgInfo?&sigunguCd=11680&bjdongCd=10300&bun=0012&ji=0000&useYm=201501&numOfRows=3&pageNo=1&_type=json&serviceKey=\t\n" +
                "4yuo1zqhcnAZ1I0MwJw5oKnEgJctJVv1ZP+jQM4BmmCalB/0edwLFr4b6C7IpGaoVGI/4ro9K9/UT/X6t19sDg==";


        // API 호출 및 데이터 받아오기
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response;
    }
}
