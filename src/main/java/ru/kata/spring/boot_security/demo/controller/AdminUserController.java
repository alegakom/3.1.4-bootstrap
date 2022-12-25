package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;


@Controller
public class AdminUserController {

    private final UserService userService;


    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/admin")
    public String getUsers(Principal principal,ModelMap model){
        User mainUser = userService.findByUsername(principal.getName());
        model.addAttribute("mainUser", mainUser);
        List<User> userList;
        userList = userService.getAllUsers();
        model.addAttribute("userList",
                userList.stream().toList());
        // нужно не забыть указать передаваемый объект "userList" в .html
        List<Role> rolesList;
        rolesList = userService.getAllRoles();
        model.addAttribute("userRoles", rolesList);
        return "admin";
    }
    @PostMapping(value = "/admin/save")
    public String saveUser(@RequestParam(value = "name") String name,
                           @RequestParam(value = "lastName") String lastName,
                           @RequestParam(value = "age")  int age,
                           @RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "roles") String role) {
        String encodedPassword = userService.encode(password);
        User user = new User(name, lastName, age, username, encodedPassword);
        user.setUserRole(userService.getRoleByName(role));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{userId}")
    public String delete(@PathVariable("userId") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "admin/update/{userId}")
    public String update(@PathVariable(value = "userId") int id){
        return "/admin";
    }

    @PostMapping(value ="admin/update/{userId}")
    public String updateUserById(@PathVariable(value = "userId") int id,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "lastName") String lastName,
                                 @RequestParam(value = "age")  int age,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "roles") String role){
        String encodedPassword = userService.encode(password);
        User user = userService.getUserById(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setUserRole(userService.getRoleByName(role));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/index")
    public String welcomePage(ModelMap model){
        return "index";
    }
}
