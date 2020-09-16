package com.study.login.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String payLoad = message.getPayload();

        ChatMessage chatMessage = objectMapper.readValue(payLoad, ChatMessage.class);
        ChatRoom chatRoom = chatRoomRepository.getChatRoom(chatMessage.getChatRoomId());
        chatRoom.handleMessage(session, chatMessage, objectMapper);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        ChatRoom.close(session);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(String.format("%s 님이 퇴장하셨습니다.", session.getId()));
        ChatRoom.send(chatMessage , objectMapper);


    }


}
