package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.Role;
import com.sanko.visitor.entities.User;
import com.sanko.visitor.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromDb = userRepo.findByUserName(user.getUserName());
        if (userFromDb != null){
            model.addAttribute("userExist", "true");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return "redirect:/login";
    }
}
