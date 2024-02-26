package com.example.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    //Get All Messages
    public List<Message> getAllMessages(){
        Iterable<Message> allMessages = messageRepository.findAll();
        List<Message> allMessagesList = new ArrayList<>();
        allMessages.forEach(allMessagesList::add);

        return allMessagesList;
    }

    //New Messages
    public Message addMessage(Message message){
        return messageRepository.save(message);
    }
    
    //Retrieve Message By ID
    public Message getMessageByID(int message_ID){
        Optional<Message> message = getAllMessages()
                                        .stream()
                                        .filter(m -> m.getMessage_id() == message_ID)
                                        .findFirst();
        return message.isPresent() ? message.get() : null;
    }

    //Patch Message by ID
    public Message patchMessageByID(int message_ID, String newMessageText){

        Optional<Message> message = getAllMessages()
                                        .stream()
                                        .filter(m -> m.getMessage_id() == message_ID)
                                        .findFirst();
        if (message.isPresent()){
            message.get().setMessage_text(newMessageText);
            return messageRepository.save(message.get());
        } else {
            return null;
        }
    }

    //Put Message by ID
    public Message updateMessage(Message message){
        return messageRepository.save(message);
    }

    //Delete Message By ID
    public int deleteMessageByID(int message_ID){
        Message message = getMessageByID(message_ID);
        if (message != null){
            messageRepository.delete(message);

            if (getMessageByID(message_ID) == null){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
        
        
    }

    //Retrieve all messages by User
    public List<Message> getMessagesByID(int account_ID){
        List<Message> messagesByID = getAllMessages()
                                            .stream()
                                            .filter(e -> e.getPosted_by() == account_ID)
                                            .collect(Collectors.toList());
        return messagesByID;
        
    }
}
