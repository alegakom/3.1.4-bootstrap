package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserController {

    private final UserService userService;

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Autowired()
    public UserController(UserService us) {
        this.userService = us;
    }

    @GetMapping(value = "/admin")
    public String getUsers(Principal principal, @RequestParam(value = "count", defaultValue = "100") int count, ModelMap model){
        User mainUser = userService.findByUserName(principal.getName());
        model.addAttribute("mainUser", mainUser);
        List<User> userList;
        userList = userService.getAllUsers();
        model.addAttribute("userList",
                userList.stream().limit(count).toList());
        // нужно не забыть указать передаваемый объект "userList" в .html
        return "admin";
    }
    @PostMapping(value = "/admin/save")
    public String saveUser(@RequestParam(value = "name") String name,
                           @RequestParam(value = "lastName") String lastName,
                           @RequestParam(value = "age")  int age,
                               @RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "roles") String role) {
        String encodedPassword = passwordEncoder().encode(password);
        User user = new User(name, lastName, age, username, encodedPassword);
        user.addUserRole(new Role(role.toUpperCase()));
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @PostMapping("/admin/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "admin/update/{id}")
    public String update(@PathVariable(value = "id") int id){
        return "/admin";
    }
    @PostMapping(value ="admin/update/{id}")
    public String updateUserById(@PathVariable(value = "id") int id,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "lastName") String lastName,
                                 @RequestParam(value = "age")  int age,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "roles") String role){
        String encodedPassword = passwordEncoder().encode(password);
        User user = userService.getUserById(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.addUserRole(new Role(role.toUpperCase()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String getUsers(Principal principal, ModelMap model){
       User user = userService.findByUserName(principal.getName());
       model.addAttribute("user", user);
//        через Principal получаем данные авторизованного пользователя bp POST request'а,
//        создается токен, а через UserService получаем у объекта User его имя из базы по username.
        return "user";
    }

    @PostMapping(value = "/user")
    public String changePassword(@RequestParam(value = "newPassword") String newPassword, Principal principal, ModelMap model){
        User user = userService.findByUserName(principal.getName());
        user = userService.getUserById(user.getId());
        user.setPassword(passwordEncoder().encode(newPassword));
        userService.updateUser(user);
        return "redirect:/user";
    }

    @GetMapping(value = "/index")
    public String welcomePage(ModelMap model){
        return "index";
    }
}
