package br.com.integration.authserver.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientProps {
    @Value("${auth.name:auth-server}")
    String clientName;

    @Value("${auth.token-uri}")
    String tokenUri;

    @Value("${auth.redirect-uri:}")
    String redirectUri;

    @Value("${auth.grant-type:client_credentials}")
    String grantType;

    @Value("#{'${auth.scopes:}'.split(',')}")
    String[] scopes;

    @Value("${auth.source.provider:environment}")
    String provider;

    @Value("${auth.source.props.project-id:}")
    String projectId;

    @Value("${auth.source.props.account-id:}")
    String accountId;

    @Value("${auth.source.props.region:us-east-1}")
    String region;

    @Value("${auth.source.props.client-id-key:client_id}")
    String clientIdKey;

    @Value("${auth.source.props.client-secret-key:client_secret}")
    String clientSecretKey;

    @Value("${auth.source.props.endpoint:}")
    String endpoint;

    @Value("${auth.source.props.secret-name:}")
    String secretName;

    @Value("${auth.source.props.client-id:}")
    String clientId;

    @Value("${auth.source.props.client-secret:}")
    String clientSecret;

    public String getClientName() {
        return clientName;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public String[] getScopes() {
        return scopes;
    }

    public String getProvider() {
        return provider;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getRegion() {
        return region;
    }

    public String getClientIdKey() {
        return clientIdKey;
    }

    public String getClientSecretKey() {
        return clientSecretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getSecretName() {
        return secretName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
