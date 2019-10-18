package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.User;
import com.sanko.visitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUser(User user, Model model) {
        if(!userService.addUser(user)){
            model.addAttribute("message", "User is exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivate = userService.activateUser(code);
        if (isActivate){
            model.addAttribute("message", "User is successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }
        return "login";
    }
}
