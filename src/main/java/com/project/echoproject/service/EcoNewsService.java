package com.project.echoproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
                    newsItem.put("title", itemNode.get("title").asText());
                    newsItem.put("link", itemNode.get("link").asText());
                    newsItem.put("description", itemNode.get("description").asText());
                    newsItem.put("pubDate", itemNode.get("pubDate").asText());  // pubDate 추가
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
}
