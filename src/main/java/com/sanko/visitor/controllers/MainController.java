package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.Message;
import com.sanko.visitor.repositories.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Model model){
        return "greeting";
    }

    @GetMapping("/main")
    public String messages(Model model){
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String text, @RequestParam String tag, Model model){
        Message message = new Message(text, tag);
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String tag, Model model){
        List<Message> messages = null;
        if (tag != null && !tag.isEmpty()){
            messages = messageRepo.findByTag(tag);
        } else {
            messages = (List<Message>) messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        return "main";
    }
}
