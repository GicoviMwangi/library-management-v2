package com.library.app.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

        //extracting the username
        String username = context.getVariables().get("username");
        Authentication auth = authentication.get();

        //check if the username we are getting authentication for is the correct one and grant access
        if (auth.getName().equals(username)){
            return new AuthorizationDecision(true);
        }

        //assigning permissions and access control
        boolean hasPrivilegedRole = auth.getAuthorities().stream()
                .anyMatch(grantAuthority ->
                        grantAuthority.getAuthority().equals("ROLE_ADMIN") ||
                        grantAuthority.getAuthority().equals("ROLE_SUPER_ADMIN")
                 );

        return new AuthorizationDecision(hasPrivilegedRole);
    }
}
