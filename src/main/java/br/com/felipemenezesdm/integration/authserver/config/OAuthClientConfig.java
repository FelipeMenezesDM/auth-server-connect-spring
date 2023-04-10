package br.com.felipemenezesdm.integration.authserver.config;

import br.com.felipemenezesdm.integration.authserver.props.OAuthClientProps;
import br.com.felipemenezesdm.integration.authserver.service.CredentialsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class OAuthClientConfig {
    @Autowired
    OAuthClientProps oAuthClientProps;

    @Autowired
    CredentialsService credentialsService;

    JSONObject credentials;

    @PostConstruct
    public void init() {
        credentials = credentialsService.getCredentials();
    }

    @Bean
    public OAuth2AuthorizeRequest authorizeRequest() {
        return OAuth2AuthorizeRequest
                .withClientRegistrationId(oAuthClientProps.getClientName())
                .principal(oAuthClientProps.getClientName())
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getClientRegistration(AuthorizationGrantType.CLIENT_CREDENTIALS));
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return inMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(OAuth2AuthorizedClientService authorizedClientService) {
        return getAuthorizedClientManager(clientRegistrationRepository(), authorizedClientService);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManagerRefreshToken(OAuth2AuthorizedClientService authorizedClientService) {
        return getAuthorizedClientManager(new InMemoryClientRegistrationRepository(getClientRegistration(AuthorizationGrantType.REFRESH_TOKEN)), authorizedClientService);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManagerPassword(OAuth2AuthorizedClientService authorizedClientService) {
        return getAuthorizedClientManager(new InMemoryClientRegistrationRepository(getClientRegistration(AuthorizationGrantType.PASSWORD)), authorizedClientService);
    }

    @Bean
    public InMemoryOAuth2AuthorizedClientService inMemoryOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    private ClientCredentialsOAuth2AuthorizedClientProvider getProvider() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(oAuthClientProps.getTimeout());
        simpleClientHttpRequestFactory.setReadTimeout(oAuthClientProps.getTimeout());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);

        DefaultClientCredentialsTokenResponseClient defaultClientCredentialsTokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
        defaultClientCredentialsTokenResponseClient.setRestOperations(restTemplate);

        ClientCredentialsOAuth2AuthorizedClientProvider credentialsOAuth2AuthorizedClientProvider = new ClientCredentialsOAuth2AuthorizedClientProvider();
        credentialsOAuth2AuthorizedClientProvider.setAccessTokenResponseClient(defaultClientCredentialsTokenResponseClient);

        return credentialsOAuth2AuthorizedClientProvider;
    }

    private AuthorizedClientServiceOAuth2AuthorizedClientManager getAuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .provider(getProvider())
                .clientCredentials()
                .refreshToken()
                .password()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    private ClientRegistration getClientRegistration(AuthorizationGrantType grantType) {
        return ClientRegistration
                .withRegistrationId(oAuthClientProps.getClientName())
                .clientId(credentials.get(oAuthClientProps.getClientIdKey()).toString())
                .clientSecret(credentials.get(oAuthClientProps.getClientSecretKey()).toString())
                .authorizationGrantType(grantType)
                .redirectUri(oAuthClientProps.getRedirectUri())
                .scope(oAuthClientProps.getScopes())
                .tokenUri(oAuthClientProps.getTokenUri())
                .build();
    }
}
