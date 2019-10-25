package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.Message;
import com.sanko.visitor.entities.User;
import com.sanko.visitor.service.MessageService;
import com.sanko.visitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@Controller
public class MainController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting(){
        return "greeting";
    }

    @GetMapping("/main")
    public String messages(@RequestParam (required = false) String filter, Model model){
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()){
            messages = messageService.findByTag(filter);
        } else {
            messages = messageService.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult, Model model, @RequestParam MultipartFile file) throws IOException {
        ControllerUtils.addMessage(user, message, bindingResult, model, file, messageService);
        Iterable<Message> messages = messageService.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }
}
