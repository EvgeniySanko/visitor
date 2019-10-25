package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.Message;
import com.sanko.visitor.entities.User;
import com.sanko.visitor.repositories.MessageRepo;
import com.sanko.visitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserService userService;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(){
        return "greeting";
    }

    @GetMapping("/main")
    public String messages(@RequestParam (required = false) String filter, Model model){
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult, Model model, @RequestParam MultipartFile file) throws IOException {
       message.setAuthor(user);
       if(bindingResult.hasErrors()){
           Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
           model.mergeAttributes(errorsMap);
           model.addAttribute("message", message);
       } else {
           saveFile(file, message);
           messageRepo.save(message);
           model.addAttribute("message", null);
       }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @GetMapping("/userMessages/{userId}")
    public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable Long userId, Model model, @RequestParam (required = false) Long messageId){
        User user = userService.findById(userId).get();
        Message message = null;
        if (messageId != null){
            message = messageRepo.findById(messageId).get();
        }
        Set<Message> messages = user.getMessages();
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping("/userMessages/{userId}")
    public String updateMessage(@AuthenticationPrincipal User currentUser, @PathVariable Long userId, @Valid Message message, BindingResult bindingResult,
                                Model model, @RequestParam(name = "id", required = false) Long messageId, @RequestParam("text") String text,
                                @RequestParam("tag") String tag, @RequestParam MultipartFile file
    ) throws IOException {
        if (messageId != null){
            Message editMessage = messageRepo.findById(messageId).get();
            if (editMessage.getAuthor().equals(currentUser)){
                if(!StringUtils.isEmpty(text)){
                    editMessage.setText(text);
                }
                if(!StringUtils.isEmpty(tag)){
                    editMessage.setTag(tag);
                }
                if (file != null){
                    saveFile(file, editMessage);
                }
                messageRepo.save(editMessage);
            }
            return "redirect:/userMessages/" + userId;
        } else {
            return add(currentUser, message, bindingResult, model, file);
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
