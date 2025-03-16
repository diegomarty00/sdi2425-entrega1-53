package com.uniovi.sdi.sdi2425entrega153;

import com.uniovi.sdi.sdi2425entrega153.services.LogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final LogService logService;

    public CustomLogoutHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            logService.saveLog(username, "LOGOUT", "Cierre de sesión exitoso");
        }
    }


}
