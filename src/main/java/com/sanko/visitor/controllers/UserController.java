package com.sanko.visitor.controllers;

import com.sanko.visitor.entities.Role;
import com.sanko.visitor.entities.User;
import com.sanko.visitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public String userList(Model model){
        Iterable<User> users = userService.findAll();
        model.addAttribute("userList", userService.findAll());
        return "userList";
    }

    @GetMapping("{userId}")
    public String userEdit(@PathVariable Long userId, Model model){
        User user = userService.findById(userId).get();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(@RequestParam String username, @RequestParam Map<String, String> form, @RequestParam ("userId") Long userId){
        userService.saveUser(username, form, userId);
        return "redirect:/user";
    }
}
