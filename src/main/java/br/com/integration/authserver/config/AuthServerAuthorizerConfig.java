package br.com.integration.authserver.config;

import br.com.infrastructure.annotation.tokenconnection.TokenConnection;
import br.com.integration.authserver.AuthServerConnection;
import br.com.integration.authserver.props.OAuthClientProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;

@Configuration
public class AuthServerAuthorizerConfig {
    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    OAuthClientProps oAuthClientProps;

    public AuthServerConnection authorize() {
        return new AuthServerConnection(authorizedClientManager.authorize(this.authorizeRequest()));
    }

    public AuthServerAuthorizerConfig setClientProps(TokenConnection props) {
        oAuthClientProps.setProps(props);
        return this;
    }

    private OAuth2AuthorizeRequest authorizeRequest() {
        return OAuth2AuthorizeRequest
                .withClientRegistrationId(oAuthClientProps.getClientName())
                .principal(oAuthClientProps.getClientName())
                .build();
    }
}
