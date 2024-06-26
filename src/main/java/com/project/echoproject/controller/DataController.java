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
    public Map<String, Object> getData(@RequestParam String region, @RequestParam(required = false) String subregion) {
        try {
            // 데이터를 저장할 맵
            Map<String, Integer> eleUsageBySIGUNGU_CD = new HashMap<>();
            Map<String, Integer> gasUsageBySIGUNGU_CD = new HashMap<>();
            Map<String, String> labelsBySIGUNGU_CD = new HashMap<>();

            // 전기 사용량 데이터 읽기
            String eleDataPath = getEleDataPath(region, subregion);
            InputStream eleInputStream = new ClassPathResource(eleDataPath).getInputStream();
            Map<String, Object> eleData = objectMapper.readValue(eleInputStream, new TypeReference<Map<String, Object>>() {});
            processUsageData(eleData, eleUsageBySIGUNGU_CD, labelsBySIGUNGU_CD, "totalUseEle");

            // 가스 사용량 데이터 읽기
            String gasDataPath = getGasDataPath(region, subregion);
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
            throw new RuntimeException("JSON 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String getEleDataPath(String region, String subregion) {
        if (region.equals("seoul")) {
            if (subregion.equals("gangnam")) {
                return "static/json/seoul_ele_use_South.json";
            } else if (subregion.equals("gangbuk")) {
                return "static/json/seoul_ele_use_North.json";
            } else {
                throw new IllegalArgumentException("올바르지 않은 서브지역입니다.");
            }
        } else if (region.equals("gyeonggi")) {
            if (subregion.equals("gyeongginorth")) {
                return "static/json/gyeonggi_ele_use_North.json";
            } else if (subregion.equals("gyeonggicentral")) {
                return "static/json/gyeonggi_ele_use_Center.json";
            } else if (subregion.equals("gyeonggisouth")) {
                return "static/json/gyeonggi_ele_use_South.json";
            } else {
                throw new IllegalArgumentException("올바르지 않은 서브지역입니다.");
            }
        } else {
            throw new IllegalArgumentException("올바르지 않은 지역입니다.");
        }
    }

    private String getGasDataPath(String region, String subregion) {
        if (region.equals("seoul")) {
            if (subregion.equals("gangnam")) {
                return "static/json/seoul_gas_use_South.json";
            } else if (subregion.equals("gangbuk")) {
                return "static/json/seoul_gas_use_North.json";
            } else {
                throw new IllegalArgumentException("올바르지 않은 서브지역입니다.");
            }
        } else if (region.equals("gyeonggi")) {
            if (subregion.equals("gyeongginorth")) {
                return "static/json/gyeonggi_gas_use_North.json";
            } else if (subregion.equals("gyeonggicentral")) {
                return "static/json/gyeonggi_gas_use_Center.json";
            } else if (subregion.equals("gyeonggisouth")) {
                return "static/json/gyeonggi_gas_use_South.json";
            } else {
                throw new IllegalArgumentException("올바르지 않은 서브지역입니다.");
            }
        } else {
            throw new IllegalArgumentException("올바르지 않은 지역입니다.");
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
