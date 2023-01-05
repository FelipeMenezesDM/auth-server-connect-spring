package br.com.integration.authserver.service;

import br.com.integration.authserver.dto.AuthServerRequest;
import br.com.integration.authserver.dto.AuthServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class AuthServerServiceImpl implements AuthServerService {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    RestTemplate restTemplate;

    private AuthServerResponse token;

    private static final Integer timeout = 5000;

    @PostConstruct
    public void init() {
        restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(timeout)).build();
    }

    /**
     * Get cached token or generate if current cached token is expired.
     *
     * @param url           Auth Server URL to get token
     * @param clientId      Client ID from Auth Server
     * @param clientSecret  Client Secret from Auth Server
     * @param grantType     Token grant type
     * @return TokenDTO
     */
    public AuthServerResponse getToken(String url, String clientId, String clientSecret, String grantType) {
        if(token == null || LocalDateTime.now().isAfter(token.getExpiresAt())) {
            AuthServerRequest authServerRequest = new AuthServerRequest();
            authServerRequest.setClientId(clientId);
            authServerRequest.setClientSecret(clientSecret);
            authServerRequest.setGrantType(grantType);

            LocalDateTime currentTime = LocalDateTime.now();
            HttpEntity<AuthServerRequest> request = new HttpEntity<>(authServerRequest);
            ResponseEntity<AuthServerResponse> exchange = restTemplate.exchange(url, HttpMethod.POST, request, AuthServerResponse.class);

            token = exchange.getBody();
            Objects.requireNonNull(token).setExpiresAt(currentTime.plus(token.getExpiresIn(), ChronoUnit.SECONDS));
        }

        return token;
    }
}
