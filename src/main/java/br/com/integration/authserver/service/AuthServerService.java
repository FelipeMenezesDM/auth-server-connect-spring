package br.com.integration.authserver.service;

import br.com.integration.authserver.dto.AuthServerResponse;

public interface AuthServerService {
    AuthServerResponse getToken(String url, String clientId, String clientSecret, String grantType);
}
