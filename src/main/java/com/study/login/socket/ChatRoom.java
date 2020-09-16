package com.study.login.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoom {
    private String id;
    private String name;
    private static final Set<WebSocketSession> sessions = new HashSet<>();


    /**
     * 채팅방을 생성한다.
     * @param name 채팅방 이름
     * @return 채팅방
     */
    public static ChatRoom create(String name) {
        ChatRoom created = new ChatRoom();
        created.id = UUID.randomUUID().toString();
        created.name = name;
        return created;
    }

    public static void close(WebSocketSession session) {
        sessions.remove(session);
    }

    /**
     * 소켓에 메시지를 전송한다.
     * @param messageObject 메시지
     * @param objectMapper JSON <-> 객체 맵퍼
     * @param <T> 객체 타입
     * @throws JsonProcessingException JSON 파싱 에러
     */
    public static  <T> void send(T messageObject, ObjectMapper objectMapper) throws JsonProcessingException {
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(messageObject));
        sessions.parallelStream().forEach(session -> {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Session에서 메시지가 수신됬을 때 실행.
     * Type이 JOIN일 경우 입장메시지 알림.
     * @param session Socket Session
     * @param chatMessage 메시지
     * @param objectMapper JSON <-> 객체 맵퍼
     * @throws JsonProcessingException JSON 파싱 에러
     */
    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws JsonProcessingException {
        if(chatMessage.getType() == MessageType.JOIN) {
            join(session);
            chatMessage.setMessage(chatMessage.getWriter() + "님이 입장했습니다.");
        }

        send(chatMessage, objectMapper);
    }

    /**
     * Socket 세션에 유저 추가.
     * @param session Socket Session
     */
    private void join(WebSocketSession session) {
        sessions.add(session);
    }
}
