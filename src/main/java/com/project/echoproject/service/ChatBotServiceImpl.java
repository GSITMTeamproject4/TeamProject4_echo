package com.project.echoproject.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final Logger logger = Logger.getLogger(ChatBotServiceImpl.class.getName());

    @Value("${chatbot.secret-key}")
    private String SECRET_KEY;

    @Value("${chatbot.api-url}")
    private String API_URL;

    @Override
    public String sendMessage(String chatMessage) throws IOException {
        URL url = new URL(API_URL);
        String message = getReqMessage(chatMessage);
        String encodeBase64String = makeSignature(message, SECRET_KEY);

        logger.info("Request message: " + message);
        logger.info("Signature: " + encodeBase64String);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json;UTF-8");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(message.getBytes(StandardCharsets.UTF_8));
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        logger.info("Response Code: " + responseCode);

        String responseMessage;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder jsonString = new StringBuilder();
            String decodedString;
            while ((decodedString = br.readLine()) != null) {
                jsonString.append(decodedString);
            }

            logger.info("Response: " + jsonString.toString());

            if (responseCode == 200) { // 정상 호출
                JSONParser jsonparser = new JSONParser();
                JSONObject json = (JSONObject) jsonparser.parse(jsonString.toString());
                JSONArray bubblesArray = (JSONArray) json.get("bubbles");
                JSONObject bubbles = (JSONObject) bubblesArray.get(0);
                JSONObject data = (JSONObject) bubbles.get("data");
                responseMessage = (String) data.get("description");
            } else { // 에러 발생
                responseMessage = con.getResponseMessage();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during API call", e);
            responseMessage = "Error: " + e.getMessage();
        }

        return responseMessage;
    }

    @Override
    public String makeSignature(String message, String secretKey) {
        String encodeBase64String = "";
        try {
            byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec signingKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating signature", e);
        }
        return encodeBase64String;
    }

    @Override
    public String getReqMessage(String voiceMessage) {
        String requestBody = "";
        try {
            JSONObject obj = new JSONObject();
            long timestamp = new Date().getTime();

            obj.put("version", "v2");
            obj.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2");
            obj.put("timestamp", timestamp);

            JSONObject bubblesObj = new JSONObject();
            bubblesObj.put("type", "text");

            JSONObject dataObj = new JSONObject();
            dataObj.put("description", voiceMessage);

            bubblesObj.put("data", dataObj);

            JSONArray bubblesArray = new JSONArray();
            bubblesArray.add(bubblesObj);

            obj.put("bubbles", bubblesArray);
            obj.put("event", "send");

            requestBody = obj.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating request message", e);
        }
        return requestBody;
    }
}
