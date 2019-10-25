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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/userMessages/{userId}")
public class UserMessagesController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @GetMapping
    public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable Long userId, Model model, @RequestParam(required = false) Long messageId){
        User user = userService.findById(userId).get();
        Message message = null;
        if (messageId != null){
            message = messageService.findById(messageId);
        }
        Set<Message> messages = user.getMessages();
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping
    public String updateMessage(@AuthenticationPrincipal User currentUser, @PathVariable Long userId, @Valid Message message, BindingResult bindingResult,
                                Model model, @RequestParam(name = "id", required = false) Long messageId, @RequestParam("text") String text,
                                @RequestParam("tag") String tag, @RequestParam MultipartFile file
    ) throws IOException {
        if (messageId != null){
            messageService.updateMessage(messageId, currentUser, text, tag, file);
            return "redirect:/userMessages/" + userId;
        } else {
            ControllerUtils.addMessage(currentUser, message, bindingResult, model, file, messageService);
            Iterable<Message> messages = messageService.findAll();
            model.addAttribute("messages", messages);
            return "main";
        }
    }
}
