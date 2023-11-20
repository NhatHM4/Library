package com.nhathm4.reactlibrary.service;

import com.nhathm4.reactlibrary.dao.MessageRepository;
import com.nhathm4.reactlibrary.entity.Message;
import com.nhathm4.reactlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail){
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(String userAdminEmail, AdminQuestionRequest adminQuestionRequest) throws Exception {
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());
        if (!message.isPresent()){
            throw new Exception("Message is not exists !!");
        }
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setAdmin_email(userAdminEmail);
        message.get().setClosed(true);
        messageRepository.save(message.get());

    }
}
