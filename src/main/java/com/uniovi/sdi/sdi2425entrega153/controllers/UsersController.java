package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.services.RolesService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.validators.PasswordChangeFormValidator;
import com.uniovi.sdi.sdi2425entrega153.validators.RegisterFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final RolesService rolesService;
    private final RegisterFormValidator registerFormValidator;
    private final PasswordChangeFormValidator passwordChangeFormValidator;

    public UsersController(UsersService usersService, RegisterFormValidator registerFormValidator,
                           PasswordChangeFormValidator passwordChangeFormValidator,
                           RolesService rolesService) {
        this.usersService = usersService;
        this.registerFormValidator = registerFormValidator;
        this.passwordChangeFormValidator = passwordChangeFormValidator;
        this.rolesService = rolesService;
    }

    @RequestMapping("/user/list")
    public String getList(Model model, Pageable pageable) {
        Page<User> users = usersService.getUsers(pageable);
        model.addAttribute("usersList", users.getContent());
        model.addAttribute("page", users);
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
        return "redirect:/home";
    }

    @RequestMapping(value = "/password-change", method = RequestMethod.POST)
    public String changePassword(@Validated User user, Principal principal, BindingResult result) {
        passwordChangeFormValidator.validate(user, result, principal);
        if(result.hasErrors()) {
            return "user/password";
        }
        String dni = principal.getName(); // DNI es el name de la autenticación
        User activeUser = usersService.getUserByDni(dni);
        activeUser.setPassword(user.getPassword());
        usersService.changePassword(activeUser);
        return "redirect:/home";
    }

    @RequestMapping(value = "/password-change", method = RequestMethod.GET)
    public String changePassword(Model model) {
        model.addAttribute("user", new User());
        return "user/password";
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

        model.addAttribute("user", activeUser);
        return "home";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String register(Model model) {
        User user = new User();
        user.setPassword(usersService.generateUserPassword());
        model.addAttribute("user", user);
        return "user/register";
    }

    @RequestMapping("/user/list/update")
    public String updateList(Model model, Pageable pageable) {
        Page<User> users = usersService.getUsers(pageable);
        model.addAttribute("usersList", users.getContent());
        return "user/list :: usersTable";
    }
}