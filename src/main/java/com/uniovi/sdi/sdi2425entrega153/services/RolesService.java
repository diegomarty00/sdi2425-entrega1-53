package com.uniovi.sdi.sdi2425entrega153.services;

import org.springframework.stereotype.Service;

@Service
public class RolesService {
    String[] roles = {"ROLE_STANDARD", "ROLE_ADMIN"};

    public String[] getRoles() {
        return roles;
    }
}