package com.example.inventorytask.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.ws.rs.client.Client;
import java.util.concurrent.TimeUnit;

@Configuration
public class  KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthUrl;

    @Value("${kc.master.realm}")
    private String masterRealm;

    @Value("${kc.master.client}")
    private String masterClient;

    @Value("${kc.admin.username}")
    private String adminUsername;

    @Value("${kc.admin.password}")
    private String adminPassword;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Instead of the default Keycloak.json, use the Spring Boot properties file.
     */
    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Keycloak admin user credentials
     */
    @Bean
    public Keycloak getAdminKeycloakUser() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAuthUrl)
                .realm(masterRealm)
                .clientId(masterClient)
                .username(adminUsername)
                .password(adminPassword)
                .resteasyClient((Client) new ResteasyClientBuilderImpl().connectionPoolSize(20).connectionTTL(1, TimeUnit.MINUTES).build()).build();
    }
    /**
     * Realm used across application
     */
    @Bean
    public RealmResource getRealm() {
        return getAdminKeycloakUser().realm(realm);
    }

}
