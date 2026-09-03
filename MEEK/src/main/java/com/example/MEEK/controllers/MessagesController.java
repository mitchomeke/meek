package com.example.MEEK.controllers;

import com.example.MEEK.ChatMessageDto;
import com.example.MEEK.repositories.MessagesRepository;
import com.example.MEEK.repositories.NotificationRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Controller
public class MessagesController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Principal principal, @Payload ChatMessageDto dto){
        User sender = userRepository.findByUserName(principal.getName()).orElseThrow();
        User receiver = userRepository.findByUserName(dto.getReceiverUsername()).orElseThrow();

        Message message = new Message();
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setTimestamp(Instant.now());
        if (!sender.getBlockedUsers().contains(receiver) && !receiver.getBlockedUsers().contains(sender)){
            notificationRepository.save(message);
        }
        messagingTemplate.convertAndSendToUser(dto.getReceiverUsername(),"/queue/messages",dto);
        userRepository.save(receiver);

    }

    @GetMapping("/messages")
    public String openChat(@RequestParam(required = false) String recipient,
                           @RequestParam(required = false) String messenger,
                            @RequestParam(required = false) Long notiId,
                            Model model){
        User receiver = userRepository.findByUserName(recipient).orElseThrow();
        User sender = userRepository.findByUserName(messenger).orElseThrow();

        List<Message> chatHistory = messagesRepository.chatHistoryBetween(recipient,messenger);
        model.addAttribute("chatHistory",chatHistory);
        model.addAttribute("receiver",receiver);
        model.addAttribute("sender",sender.getUserName());
        model.addAttribute("receiverName",receiver.getUserName());

        if (notiId != null){
            Message message = messagesRepository.findById(notiId).orElseThrow();
            message.setDismissed(true);
            messagesRepository.save(message);
        }
        return "messages";
    }
}
