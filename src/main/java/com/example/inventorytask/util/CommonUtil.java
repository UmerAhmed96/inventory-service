package com.example.inventorytask.util;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    HttpServletRequest httpServletRequest;

    public KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) httpServletRequest.getAttribute(KeycloakSecurityContext.class.getName());
    }

}
