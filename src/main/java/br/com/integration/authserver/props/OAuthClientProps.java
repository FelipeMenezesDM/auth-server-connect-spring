package br.com.integration.authserver.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientProps {
    @Value("${auth-server.name:auth-server}")
    String clientName;

    @Value("${auth-server.uri}/token")
    String tokenUri;

    @Value("${auth-server:uri}/asset")
    String assetUri;

    @Value("${auth-server.redirect-uri:}")
    String redirectUri;

    @Value("${auth-server.grant-type:client_credentials}")
    String grantType;

    @Value("#{'${auth-server.scopes:}'.split(',')}")
    String[] scopes;

    @Value("${auth-server.source.provider:environment}")
    String provider;

    @Value("${auth-server.source.props.project-id:}")
    String projectId;

    @Value("${auth-server.source.props.region:us-east-1}")
    String region;

    @Value("${auth-server.source.props.client-id-key:client_id}")
    String clientIdKey;

    @Value("${auth-server.source.props.client-secret-key:client_secret}")
    String clientSecretKey;

    @Value("${auth-server.source.props.endpoint:}")
    String endpoint;

    @Value("${auth-server.source.props.secret-name:}")
    String secretName;

    @Value("${auth-server.source.props.client-id:}")
    String clientId;

    @Value("${auth-server.source.props.client-secret:}")
    String clientSecret;

    public String getClientName() {
        return clientName;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getAssetUri() {
        return assetUri;
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
