package com.sanko.visitor.service;

import com.sanko.visitor.entities.Message;
import com.sanko.visitor.entities.User;
import com.sanko.visitor.repositories.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private MessageRepo messageRepo;

    public List<Message> findByTag(String tag){
        return messageRepo.findByTag(tag);
    }

    public Iterable<Message> findAll(){
        return messageRepo.findAll();
    }

    public void save(Message message){
        messageRepo.save(message);
    }

    public Message findById(Long id){
       return messageRepo.findById(id).get();
    }

    public void addMessage(Message message, User user, MultipartFile file) throws IOException {
        message.setAuthor(user);
        saveFile(file, message);
        save(message);
    }

    public void updateMessage(Long messageId, User currentUser, String text, String tag, MultipartFile file) throws IOException {
        Message message = findById(messageId);
        if (message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            if (file != null){
                saveFile(file, message);
            }
            save(message);
        }
    }

    private void saveFile(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
    }
}
