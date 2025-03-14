package com.uniovi.sdi.sdi2425entrega153.controllers;

import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import com.uniovi.sdi.sdi2425entrega153.validators.PathValidator;
import com.uniovi.sdi.sdi2425entrega153.validators.VehicleRegistrationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.LinkedList;

@Controller
public class PathController {

    private final PathService pathService;
    private final UsersService usersService;
    private final HttpSession httpSession;

    @Autowired
    private PathValidator pathValidator;

    public PathController(PathService pathService, UsersService usersService, HttpSession httpSession) {
        this.pathService = pathService;
        this.usersService = usersService;
        this.httpSession = httpSession;
    }

    @RequestMapping("/path/personal")
    public String getList(Model model, Pageable pageable, Principal principal){
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Path> paths = pathService.getPathsForUser(pageable, user);
        model.addAttribute("pathList", paths.getContent());
        model.addAttribute("page", paths);
        return "path/personal";
    }
}
