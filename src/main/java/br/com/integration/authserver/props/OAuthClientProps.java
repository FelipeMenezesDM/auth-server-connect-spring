package br.com.integration.authserver.props;

import br.com.infrastructure.annotation.tokenconnection.TokenConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientProps {
    @Value("${authentication.auth-server.token-uri}")
    String tokenUri;

    @Value("${authentication.auth-server.redirect-uri:#{null}}")
    String redirectUri;

    @Value("${authentication.auth-server.client-id}")
    String clientId;

    @Value("${authentication.auth-server.client-secret}")
    String clientSecret;

    @Value("#{'${authentication.auth-server.scopes:}'.split(',')}")
    String[] scopes;

    @Value("${authentication.auth-server.grant-type:#{'client_credentials'}}")
    String grantType;

    @Value("${authentication.auth-server.client-name:#{'auth-server'}}")
    String clientName;

    public String getTokenUri() {
        return tokenUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String[] getScopes() {
        return scopes;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setProps(TokenConnection props) {
        tokenUri = getProp(props.tokenUri(), getTokenUri());
        redirectUri = getProp(props.redirectUri(), getRedirectUri());
        clientId = getProp(props.clientId(), getClientId());
        clientSecret = getProp(props.clientSecret(), getClientSecret());
        grantType = getProp(props.grantType(), getGrantType());
        scopes = getProp(props.scopes(), getScopes());
        clientName = getProp(props.clientName(), getClientName());
    }

    private String getProp(String newValue, String oldValue) {
        return newValue.isEmpty() ? oldValue : newValue;
    }

    private String[] getProp(String[] newValue, String[] oldValue) {
        return newValue.length == 0 ? oldValue : newValue;
    }
}
