package com.sanko.visitor.config;

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
public class GreetingController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model){
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("text", "start page");
        return "index";
    }

    @GetMapping("/messages")
    public String messages(Model model){
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "messages";
    }

    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag, Model model){
        Message message = new Message(text, tag);
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "messages";
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
        return "messages";
    }
}
