package com.example.MEEK.controllers;

import com.example.MEEK.ChatMessageDto;
import com.example.MEEK.repositories.MessagesRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Message;
import com.example.MEEK.resources.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class MessagesController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Principal principal, @Payload ChatMessageDto dto){
        User sender = userRepository.findByUserName(principal.getName()).orElseThrow();
        User receiver = userRepository.findByUserName(dto.getReceiverUsername()).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(dto.getContent());
        messagesRepository.save(message);

        messagingTemplate.convertAndSendToUser(
                dto.getReceiverUsername(),"/queue/messages",dto
        );

    }
}
