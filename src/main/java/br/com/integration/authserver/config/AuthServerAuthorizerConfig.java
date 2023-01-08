package br.com.integration.authserver.config;

import br.com.integration.authserver.AuthServerConnection;
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

    public AuthServerConnection authorize() {
        return new AuthServerConnection(authorizedClientManager.authorize(authorizeRequest));
    }
}
