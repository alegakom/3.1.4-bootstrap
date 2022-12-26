package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    public String getUsers(ModelMap model){
        model.addAttribute("user", userService.getPrincipalUser()); // реализовал метод, где получаю авторизованного юзера из авторитихолдера

//        через Principal получаем данные авторизованного пользователя bp POST request'а,
//        создается токен, а через UserService получаем у объекта User его имя из базы по username.
        return "user";
    }


    @PostMapping(value = "/user")
    public String changePassword(@RequestParam(value = "newPassword") String newPassword, Principal principal, ModelMap model){
        User user = userService.findByUsername(principal.getName());
        user = userService.getPrincipalUser();
        user.setPassword(userService.encode(newPassword));
        userService.updateUser(user);
        return "redirect:/user";
    }
}
