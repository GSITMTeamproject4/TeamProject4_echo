package com.project.echoproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EcoNewsService {

    private final String clientId = "pAipiUJ23hJkdobFn6q9";
    private final String clientSecret = "40u2NZ1_qq";

    public List<Map<String, String>> searchNews(String query) {
        try {
            String text = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + text;

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);

            String responseBody = get(apiUrl, requestHeaders);

            // JSON 문자열을 JsonNode 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode itemsNode = objectMapper.readTree(responseBody).get("items");

            // JsonNode를 리스트로 변환
            List<Map<String, String>> newsList = new ArrayList<>();
            if (itemsNode.isArray()) {
                for (JsonNode itemNode : (ArrayNode) itemsNode) {
                    Map<String, String> newsItem = new HashMap<>();
                    newsItem.put("title", cleanText(itemNode.get("title").asText()));
                    newsItem.put("link", itemNode.get("link").asText());
                    newsItem.put("description", cleanText(itemNode.get("description").asText()));
                    newsItem.put("pubDate", formatDate(itemNode.get("pubDate").asText()));  // pubDate 추가 및 형식 변환
                    newsList.add(newsItem);
                }
            }
            return newsList;

        } catch (Exception e) {
            throw new RuntimeException("검색어 인코딩 실패 또는 API 호출 실패", e);
        }
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException("연결 실패: " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        StringBuilder responseBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(body))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("API 응답 읽기 실패", e);
        }
        return responseBody.toString();
    }

    private String cleanText(String text) {
        if (text == null) {
            return "";
        }
        // 태그와 이스케이프 문자 등을 빈칸으로 대체
        return text.replaceAll("<.*?>", "")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("\\n", " ")
                .replaceAll("\\r", " ")
                .replaceAll("\\t", " ");
    }

    private String formatDate(String dateStr) {
        try {
            // pubDate 예시: "Thu, 01 Jun 2023 00:00:00 +0900"
            DateTimeFormatter inputFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return zonedDateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // 변환 실패 시 원본 문자열 반환
            return dateStr;
        }
    }
}
