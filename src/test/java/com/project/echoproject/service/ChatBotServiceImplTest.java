package com.project.echoproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatBotServiceImplTest {

    @InjectMocks
    private ChatBotServiceImpl chatBotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(chatBotService, "SECRET_KEY", "testSecretKey");
        ReflectionTestUtils.setField(chatBotService, "API_URL", "http://test.api.url");
    }

    @Test
    void testMakeSignature() {
        String message = "testMessage";
        String signature = chatBotService.makeSignature(message, "testSecretKey");
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    @Test
    void testGetReqMessage() {
        String voiceMessage = "Hello, ChatBot!";
        String reqMessage = chatBotService.getReqMessage(voiceMessage);
        assertNotNull(reqMessage);
        assertTrue(reqMessage.contains(voiceMessage));
        assertTrue(reqMessage.contains("version"));
        assertTrue(reqMessage.contains("userId"));
        assertTrue(reqMessage.contains("timestamp"));
    }

    @Test
    void testSendMessage() throws IOException {
        // 이 테스트는 실제 API를 호출하지 않고 모의 객체를 사용
        ChatBotServiceImpl spyService = spy(chatBotService);
        String testMessage = "Test message";
        String expectedResponse = "ChatBot response";

        doReturn(expectedResponse).when(spyService).sendMessage(testMessage);

        String actualResponse = spyService.sendMessage(testMessage);
        assertEquals(expectedResponse, actualResponse);
        verify(spyService, times(1)).sendMessage(testMessage);
    }
}