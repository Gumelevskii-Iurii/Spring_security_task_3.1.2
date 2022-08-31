package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String users(Model model) {
        Collection<Role> roles = roleService.listRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.listUsers());
        return "admin";
    }

    @GetMapping("/addUsers")
    public String addNewUsers(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUsers";
    }

    @PostMapping("/addUsers")
    public String saveNewUser(@ModelAttribute("user") User user,
                              @RequestParam(required = false) String roleAdmin,
                              @RequestParam(required = false) String roleUser) {
        roleService.addUserRole(user, roleAdmin, roleUser);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("deleteUsers/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/changeUsers/{id}")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "changeUsers";
    }

    @PutMapping(value = "/changeUsers")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required = false) String roleAdmin,
                               @RequestParam(required = false) String roleUser) {
        roleService.addUserRole(user, roleAdmin, roleUser);
        userService.changeUser(user);
        return "redirect:/admin";
    }

}