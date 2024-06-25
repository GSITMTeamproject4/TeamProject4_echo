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
            // JSON 데이터 구조에 맞게 Map으로 읽기
            InputStream eleInputStream = new ClassPathResource("static/json/ganwondo_ele_use.json").getInputStream();
            Map<String, Object> eleData = objectMapper.readValue(eleInputStream, new TypeReference<Map<String, Object>>() {});

            InputStream gasInputStream = new ClassPathResource("static/json/ganwondo_gas_use.json").getInputStream();
            Map<String, Object> gasData = objectMapper.readValue(gasInputStream, new TypeReference<Map<String, Object>>() {});

            // 전기 사용량 데이터를 시군구 코드 기준으로 맵으로 변환
            Map<String, Integer> eleUsageBySIGUNGU_CD = new HashMap<>();
            List<Map<String, String>> eleDataList = (List<Map<String, String>>) eleData.get("Data");
            for (Map<String, String> eleItem : eleDataList) {
                String sigunguCd = eleItem.get("SIGUNGU_CD");
                int totalUseEle = Integer.parseInt(eleItem.get("totalUseEle"));
                eleUsageBySIGUNGU_CD.put(sigunguCd, eleUsageBySIGUNGU_CD.getOrDefault(sigunguCd, 0) + totalUseEle);
            }

            // 가스 사용량 데이터를 시군구 코드 기준으로 맵으로 변환
            Map<String, Integer> gasUsageBySIGUNGU_CD = new HashMap<>();
            List<Map<String, String>> gasDataList = (List<Map<String, String>>) gasData.get("Data");
            for (Map<String, String> gasItem : gasDataList) {
                String sigunguCd = gasItem.get("SIGUNGU_CD");
                int totalUseGas = Integer.parseInt(gasItem.get("totalUseGas"));
                gasUsageBySIGUNGU_CD.put(sigunguCd, gasUsageBySIGUNGU_CD.getOrDefault(sigunguCd, 0) + totalUseGas);
            }

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
}
