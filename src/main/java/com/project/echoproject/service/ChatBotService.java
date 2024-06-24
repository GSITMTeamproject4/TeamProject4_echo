package com.project.echoproject.service;

import java.io.IOException;

public interface ChatBotService {
    String sendMessage(String chatMessage) throws IOException;
    String makeSignature(String message, String secretKey);
    String getReqMessage(String voiceMessage);

}
