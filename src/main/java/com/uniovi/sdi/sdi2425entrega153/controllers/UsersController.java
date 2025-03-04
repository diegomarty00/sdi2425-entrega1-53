package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.services.RolesService;
import com.uniovi.sdi.sdi2425entrega153.services.SecurityService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.validators.RegisterFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final SecurityService securityService;
    private final RolesService rolesService;
    private final RegisterFormValidator registerFormValidator;

    public UsersController(UsersService usersService, SecurityService securityService,
                           RegisterFormValidator registerFormValidator, RolesService rolesService) {
        this.usersService = usersService;
        this.securityService = securityService;
        this.registerFormValidator = registerFormValidator;
        this.rolesService = rolesService;
    }

    @RequestMapping("/user/list")
    public String getList(Model model) {
        model.addAttribute("usersList", usersService.getUsers());
        return "user/list";
    }

    @RequestMapping("/user/details/{id}")
    public String getDetail(Model model, @PathVariable Long id) {
        model.addAttribute("user", usersService.getUser(id));
        return "user/details";
    }

    @RequestMapping(value = "/user/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        User user = usersService.getUser(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.POST)
    public String setEdit(@PathVariable Long id, @ModelAttribute User user) {
        User originalUser = usersService.getUser(id);
        originalUser.setDni(user.getDni());
        originalUser.setName(user.getName());
        originalUser.setLastName(user.getLastName());
        usersService.addUser(originalUser);
        return "redirect:/user/details/" + id;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register(@Validated User user, BindingResult result) {
        registerFormValidator.validate(user, result);
        if(result.hasErrors()) {
            return "user/register";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        securityService.autoLogin(user.getDni(), user.getPasswordConfirm());
        return "redirect:home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        User activeUser = usersService.getUserByDni(dni);
        //Page<Mark> marks = marksService.getMarksForUser(pageable, activeUser);
        //model.addAttribute("marksList", marks.getContent());
        //model.addAttribute("page", marks);
        return "home";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @RequestMapping("/user/list/update")
    public String updateList(Model model) {
        model.addAttribute("usersList", usersService.getUsers() );
        return "user/list :: usersTable";
    }
}