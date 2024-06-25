package com.project.echoproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DataController {

    private final ObjectMapper objectMapper;

    public DataController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/data")
    public Map<String, Object> getData(@RequestParam String region) {
        try {
            // 데이터를 저장할 맵
            Map<String, Integer> eleUsageBySIGUNGU_CD = new HashMap<>();
            Map<String, Integer> gasUsageBySIGUNGU_CD = new HashMap<>();
            Map<String, String> labelsBySIGUNGU_CD = new HashMap<>();

            // 서울 또는 경기 데이터 선택
            String eleDataPath = region.equals("seoul") ? "static/json/seoul_ele_use.json" : "static/json/gyeonggi_ele_use.json";
            String gasDataPath = region.equals("seoul") ? "static/json/seoul_gas_use.json" : "static/json/gyeonggi_gas_use.json";

            // 전기 사용량 데이터 읽기
            InputStream eleInputStream = new ClassPathResource(eleDataPath).getInputStream();
            Map<String, Object> eleData = objectMapper.readValue(eleInputStream, new TypeReference<Map<String, Object>>() {});
            processUsageData(eleData, eleUsageBySIGUNGU_CD, labelsBySIGUNGU_CD, "totalUseEle");

            // 가스 사용량 데이터 읽기
            InputStream gasInputStream = new ClassPathResource(gasDataPath).getInputStream();
            Map<String, Object> gasData = objectMapper.readValue(gasInputStream, new TypeReference<Map<String, Object>>() {});
            processUsageData(gasData, gasUsageBySIGUNGU_CD, labelsBySIGUNGU_CD, "totalUseGas");

            // JSON 응답으로 합친 데이터 반환
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("eleUsageBySIGUNGU_CD", eleUsageBySIGUNGU_CD);
            responseData.put("gasUsageBySIGUNGU_CD", gasUsageBySIGUNGU_CD);
            responseData.put("labelsBySIGUNGU_CD", labelsBySIGUNGU_CD);

            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }

    private void processUsageData(Map<String, Object> data, Map<String, Integer> usageBySIGUNGU_CD, Map<String, String> labelsBySIGUNGU_CD, String usageKey) {
        List<Map<String, String>> dataList = (List<Map<String, String>>) data.get("Data");
        for (Map<String, String> item : dataList) {
            String sigunguCd = item.get("SIGUNGU_CD");
            int totalUsage = Integer.parseInt(item.get(usageKey));
            usageBySIGUNGU_CD.put(sigunguCd, usageBySIGUNGU_CD.getOrDefault(sigunguCd, 0) + totalUsage);
            labelsBySIGUNGU_CD.putIfAbsent(sigunguCd, item.get("PLAT_PLC"));
        }
    }
}
