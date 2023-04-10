package br.com.felipemenezesdm.integration.authserver.service;

import br.com.felipemenezesdm.integration.authserver.AuthServerToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthServerService {
    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManagerPassword;

    @Autowired
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManagerRefreshToken;

    @Autowired
    OAuth2AuthorizeRequest authorizeRequest;

    @Autowired
    AssetTokenRequestService assetTokenRequestService;

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;

    public AuthServerToken authorize() {
        return new AuthServerToken(authorizedClientManager.authorize(authorizeRequest));
    }

    public AuthServerToken grantPassword(String username, String password) {
        authorizedClientService.removeAuthorizedClient(authorizeRequest.getClientRegistrationId(), authorizeRequest.getPrincipal().getName());
        authorizedClientManagerPassword.setContextAttributesMapper(getContextAttributesMapper(username, password));

        return new AuthServerToken(authorizedClientManagerPassword.authorize(authorizeRequest));
    }

    public void validate(String token, String correlationId, String flowId, String apiKey, String[] scopes) {
        assetTokenRequestService.validate(token, correlationId, flowId, apiKey, scopes);
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> getContextAttributesMapper(String username, String password) {
        return authorizeRequest -> {
            Map<String, Object> contextAttributes = new HashMap<>();
            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);

            return contextAttributes;
        };
    }
}
