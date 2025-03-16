package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.LogEntry;
import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.services.LogService;
import com.uniovi.sdi.sdi2425entrega153.services.RolesService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.validators.EditFormValidator;
import com.uniovi.sdi.sdi2425entrega153.validators.PasswordChangeFormValidator;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final RolesService rolesService;
    private final RegisterFormValidator registerFormValidator;
    private final PasswordChangeFormValidator passwordChangeFormValidator;
    private final EditFormValidator editFormValidator;

    private final LogService logService;

    public UsersController(UsersService usersService, RegisterFormValidator registerFormValidator,
                           PasswordChangeFormValidator passwordChangeFormValidator,
                           RolesService rolesService, EditFormValidator editFormValidator, LogService logService) {
        this.usersService = usersService;
        this.registerFormValidator = registerFormValidator;
        this.passwordChangeFormValidator = passwordChangeFormValidator;
        this.editFormValidator = editFormValidator;
        this.rolesService = rolesService;
        this.logService = logService;
    }

    //imprimir logs antes de cada solicitud
    @ModelAttribute
    public void printLogs() {
        List<LogEntry> logs = logService.getAllLogs();
        System.out.println("=== Logs en la base de datos ===");
        for (LogEntry log : logs) {
            System.out.println(log);
        }
        System.out.println("===============================");
    }

    @RequestMapping("/user/list")
    public String getList(Model model, Pageable pageable) {
        Page<User> users = usersService.getUsers(pageable);
        model.addAttribute("usersList", users.getContent());
        model.addAttribute("page", users);

        logService.saveLog(getCurrentUsername(), "PET", "/user/list");

        return "user/list";
    }

    @RequestMapping("/user/logs")
    public String logs(Model model, Pageable pageable) {
        List<LogEntry> logs = logService.getAllLogs();
        Collections.reverse(logs);
        model.addAttribute("logsList", logs);

        logService.saveLog(getCurrentUsername(), "PET", "/user/logs");

        return "user/listLogs";
    }

    @RequestMapping(value = "/user/logs/delete", method = RequestMethod.POST)
    public String deleteLogs() {
        logService.deleteLogs();
        return "redirect:/user/logs";
    }





    @RequestMapping(value = "/user/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        User user = usersService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Arrays.asList(rolesService.getRoles()));
        return "user/edit";
    }

    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable Long id, @ModelAttribute User user, BindingResult result, Model model) {
        User originalUser = usersService.getUser(id);
        editFormValidator.validate(user, result, originalUser.getDni());
        if(result.hasErrors()) {
            model.addAttribute("roles", Arrays.asList(rolesService.getRoles()));
            return "user/edit";
        }
        originalUser.setDni(user.getDni());
        originalUser.setName(user.getName());
        originalUser.setLastName(user.getLastName());
        originalUser.setRole(user.getRole());
        usersService.editUser(originalUser);
        return "redirect:/user/list";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register(@Validated User user, BindingResult result) {
        registerFormValidator.validate(user, result);
        if(result.hasErrors()) {
            return "user/register";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);

        String username = getCurrentUsername();
        logService.saveLog(username, "PET", "/user/register");
        logService.saveLog(username, "ALTA", "Usuario registrado: " + user.getDni());

        return "redirect:/user/list";
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

        logService.saveLog(dni, "PET", "/password-change");

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
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        User activeUser = usersService.getUserByDni(dni);

        model.addAttribute("user", activeUser);

        logService.saveLog(dni, "PET", "/home");

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

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}