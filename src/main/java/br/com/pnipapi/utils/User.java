package br.com.pnipapi.utils;

import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.security.UserPrincipal;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;


public class User {

    public static Usuario getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal)auth.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        Usuario u = new Usuario();

        u.setId(Long.parseLong(accessToken.getSubject().split(":")[2]));
        u.setNome(accessToken.getPreferredUsername());
        u.setEmail(accessToken.getPreferredUsername());

        return u;
    }

    public static Long getIdCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal)auth.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        return Long.parseLong(accessToken.getSubject().split(":")[2]);
    }

    public static Optional<UserPrincipal> getOptionalUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .map(Authentication::getPrincipal)
                       .map(p -> p instanceof UserPrincipal ? (UserPrincipal) p : null);
    }

    public static String randomPassword() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(1, 12);
    }

}