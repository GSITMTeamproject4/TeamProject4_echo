package com.project.echoproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String getData() {
        try {
            // 강원도 전기 사용량 데이터를 읽기
            InputStream eleInputStream = new ClassPathResource("static/json/ganwondo_ele_use.json").getInputStream();
            Map<String, Object> eleData = objectMapper.readValue(eleInputStream, new TypeReference<Map<String, Object>>() {});

            // 강원도 가스 사용량 데이터를 읽기
            InputStream gasInputStream = new ClassPathResource("static/json/ganwondo_gas_use.json").getInputStream();
            Map<String, Object> gasData = objectMapper.readValue(gasInputStream, new TypeReference<Map<String, Object>>() {});

            // 세종 전기 사용량 데이터를 읽기
            InputStream sejjoungEleInputStream = new ClassPathResource("static/json/sejjoung_ele_use.json").getInputStream();
            Map<String, Object> sejjoungEleData = objectMapper.readValue(sejjoungEleInputStream, new TypeReference<Map<String, Object>>() {});

            // 세종 가스 사용량 데이터를 읽기
            InputStream sejjoungGasInputStream = new ClassPathResource("static/json/sejjoung_gas_use.json").getInputStream();
            Map<String, Object> sejjoungGasData = objectMapper.readValue(sejjoungGasInputStream, new TypeReference<Map<String, Object>>() {});

            // 전기 사용량 데이터를 시군구 코드 기준으로 맵으로 변환
            Map<String, Integer> eleUsageBySIGUNGU_CD = new HashMap<>();
            processUsageData(eleData, eleUsageBySIGUNGU_CD, "totalUseEle");
            processUsageData(sejjoungEleData, eleUsageBySIGUNGU_CD, "totalUseEle");

            // 가스 사용량 데이터를 시군구 코드 기준으로 맵으로 변환
            Map<String, Integer> gasUsageBySIGUNGU_CD = new HashMap<>();
            processUsageData(gasData, gasUsageBySIGUNGU_CD, "totalUseGas");
            processUsageData(sejjoungGasData, gasUsageBySIGUNGU_CD, "totalUseGas");

            // JSON 응답으로 합친 데이터 반환
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("eleUsageBySIGUNGU_CD", eleUsageBySIGUNGU_CD);
            responseData.put("gasUsageBySIGUNGU_CD", gasUsageBySIGUNGU_CD);

            return objectMapper.writeValueAsString(responseData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }

    private void processUsageData(Map<String, Object> data, Map<String, Integer> usageBySIGUNGU_CD, String usageKey) {
        List<Map<String, String>> dataList = (List<Map<String, String>>) data.get("Data");
        for (Map<String, String> item : dataList) {
            String sigunguCd = item.get("SIGUNGU_CD");
            int totalUsage = Integer.parseInt(item.get(usageKey));
            usageBySIGUNGU_CD.put(sigunguCd, usageBySIGUNGU_CD.getOrDefault(sigunguCd, 0) + totalUsage);
        }
    }
}
