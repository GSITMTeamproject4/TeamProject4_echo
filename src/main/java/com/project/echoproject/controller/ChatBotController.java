package com.project.echoproject.controller;

import com.project.echoproject.service.ChatBotServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatBotController {

    private final ChatBotServiceImpl chatbotService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String chatMessage) throws IOException {
        return chatbotService.sendMessage(chatMessage);
    }

    @GetMapping
    public String chatPage() {
        return "chatbot";
    }
}
