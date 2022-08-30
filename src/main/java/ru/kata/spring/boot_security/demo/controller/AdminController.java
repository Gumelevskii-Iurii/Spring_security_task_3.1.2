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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


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
//        model.addAttribute("user", new User());
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
                              @RequestParam(required=false) String roleAdmin) {
        Collection<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        user.setRoles(roles);
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

    @PostMapping(value = "/changeUsers")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required=false) String roleAdmin,
                               @RequestParam(required=false) String roleUser) {

        Collection<Role> roles = new ArrayList<>();

        if (roleUser != null && roleUser.equals("ROLE_USER") && (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN"))) {
            roles.add(roleService.getRoleByName("ROLE_USER"));
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        } else if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
//        else roles.add(roleService.getRoleByName("ROLE_USER"));

        user.setRoles(roles);
        userService.changeUser(user);
        return "redirect:/admin";
    }




}




