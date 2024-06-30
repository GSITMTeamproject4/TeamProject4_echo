package com.project.echoproject.controller;

import com.project.echoproject.service.ChatBotService;
import com.project.echoproject.service.ChatBotServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatBotController {

    private final ChatBotService chatbotService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String chatMessage) throws IOException {
        return chatbotService.sendMessage(chatMessage);
    }

    @GetMapping
    public String showChatbotPage() {
        return "chatbot";
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
    }
}
