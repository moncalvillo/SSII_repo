package ssi.pai3.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssi.pai3.server.model.Message;
import ssi.pai3.server.model.User;
import ssi.pai3.server.repository.MessageRepository;
import ssi.pai3.server.repository.UserRepository;

@Service
public class ServerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;


    public boolean userExists(User user) {
        User userRegistered = userRepository.findByUsername(user.getUsername()).orElse(null);
        return userRegistered != null;  
    }

    public boolean verifyUser(User user) {
        User userRegistered = userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
        return userRegistered != null;  
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }
}
