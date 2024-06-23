package com.project.echoproject.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RequestMapping("/dashboard")
@Controller
public class DashboardController {

    @GetMapping("")
    public String dashboard() {
        return "dashboard"; // dashboard.html을 반환
    }

    @GetMapping("/data")
    public ResponseEntity<JsonNode> fetchData() {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://apis.data.go.kr/1611000/BldEngyService/getBeGasUsgInfo?&sigunguCd=11680&bjdongCd=10300&bun=0012&ji=0000&useYm=201501&numOfRows=3&pageNo=1&_type=json&serviceKey=4yuo1zqhcnAZ1I0MwJw5oKnEgJctJVv1ZP+jQM4BmmCalB/0edwLFr4b6C7IpGaoVGI/4ro9K9/UT/X6t19sDg==";

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(jsonNode);
    }
}
