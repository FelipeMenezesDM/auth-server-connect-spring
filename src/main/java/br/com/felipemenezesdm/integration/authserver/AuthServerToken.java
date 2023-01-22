package br.com.felipemenezesdm.integration.authserver;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

public class AuthServerToken {
    private OAuth2AuthorizedClient authorizedClient;

    public AuthServerToken(OAuth2AuthorizedClient authorizedClient) {
        this.authorizedClient = authorizedClient;
    }

    public OAuth2AccessToken getAccessToken() {
        return authorizedClient.getAccessToken();
    }

    public OAuth2RefreshToken getRefreshToken() {
        return authorizedClient.getRefreshToken();
    }
}
