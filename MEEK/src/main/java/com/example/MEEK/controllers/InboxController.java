package com.example.MEEK.controllers;

import com.example.MEEK.repositories.MessagesRepository;
import com.example.MEEK.repositories.NotificationRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Message;
import com.example.MEEK.resources.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class InboxController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/inbox")
    public String openInbox(@RequestParam(required = true) Long userId, Model model){
        User user = userRepository.findById(userId).orElseThrow();
        List<Message> allMessagesOfUser = messagesRepository.getUserInbox(user);
        Map<User,Message> eachInbox = new HashMap<>();

        for (Message m : allMessagesOfUser){
            User partner;
            if (m.getSender().equals(user)){
                partner = m.getReceiver();
            }
            else partner = m.getSender();
            eachInbox.putIfAbsent(partner,m);
        }
        model.addAttribute("inboxConversations",eachInbox);
        model.addAttribute("loggedInUser",user);
        return "inbox";
    }
}
