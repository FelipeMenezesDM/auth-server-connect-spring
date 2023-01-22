package br.com.integration.authserver.service;

import br.com.integration.authserver.AuthServerToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthServerService {
    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    OAuth2AuthorizeRequest authorizeRequest;

    @Autowired
    AssetTokenRequestService assetTokenRequestService;

    public AuthServerToken authorize() {
        return new AuthServerToken(authorizedClientManager.authorize(authorizeRequest));
    }

    public void validate(String token, String[] scopes) {
        assetTokenRequestService.validate(token, scopes);
    }
}
