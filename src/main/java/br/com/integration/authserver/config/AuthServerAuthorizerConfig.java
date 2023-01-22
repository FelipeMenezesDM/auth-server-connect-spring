package br.com.integration.authserver.config;

import br.com.integration.authserver.AuthServerToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;

@Configuration
public class AuthServerAuthorizerConfig {
    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    OAuth2AuthorizeRequest authorizeRequest;

    public AuthServerToken authorize() {
        return new AuthServerToken(authorizedClientManager.authorize(authorizeRequest));
    }

    public void validate(String token, String[] scopes) {

    }
}
