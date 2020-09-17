package com.study.login.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stomp-chat")
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/join")
    public void join(ChatMessage chatMessage) {
        chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다." );
        template.convertAndSend("/subscribe/stomp-chat/room/" + chatMessage.getChatRoomId(), chatMessage);
    }

    @MessageMapping("/message")
    public void message(ChatMessage chatMessage) {
        template.convertAndSend("/subscribe/stomp-chat/room/" + chatMessage.getChatRoomId(), chatMessage);
    }


}
