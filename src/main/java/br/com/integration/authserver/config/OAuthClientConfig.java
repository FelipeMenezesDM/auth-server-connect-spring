package br.com.integration.authserver.config;

import br.com.integration.authserver.props.OAuthClientProps;
import br.com.integration.authserver.service.CredentialsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuthClientConfig {
    @Autowired
    OAuthClientProps oAuthClientProps;

    @Autowired
    CredentialsService credentialsService;

    @Bean
    public ClientRegistration clientRegistration() {
        JSONObject credentials = credentialsService.getCredentials();

        return ClientRegistration
                .withRegistrationId(oAuthClientProps.getClientName())
                .clientId(credentials.get(oAuthClientProps.getClientIdKey()).toString())
                .clientSecret(credentials.get(oAuthClientProps.getClientSecretKey()).toString())
                .authorizationGrantType(new AuthorizationGrantType(oAuthClientProps.getGrantType()))
                .redirectUri(oAuthClientProps.getRedirectUri())
                .scope(oAuthClientProps.getScopes())
                .tokenUri(oAuthClientProps.getTokenUri())
                .build();
    }

    @Bean
    public OAuth2AuthorizeRequest authorizeRequest() {
        return OAuth2AuthorizeRequest
                .withClientRegistrationId(oAuthClientProps.getClientName())
                .principal(oAuthClientProps.getClientName())
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration clientRegistration) {
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
