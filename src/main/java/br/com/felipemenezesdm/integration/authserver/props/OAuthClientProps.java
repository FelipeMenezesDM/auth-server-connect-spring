package br.com.felipemenezesdm.integration.authserver.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientProps {
    @Value("${auth-server.name:auth-server}")
    String clientName;

    @Value("${auth-server.enabled:true}")
    Boolean enabled;

    @Value("${auth-server.uri}/token")
    String tokenUri;

    @Value("${auth-server.uri}/asset")
    String assetUri;

    @Value("${auth-server.redirect-uri:}")
    String redirectUri;

    @Value("${auth-server.grant-type:client_credentials}")
    String grantType;

    @Value("#{'${auth-server.scopes:}'.split(',')}")
    String[] scopes;

    @Value("${auth-server.timeout:300}")
    Integer timeout;

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

    @Value("${auth-server.source.props.end-point:}")
    String endPoint;

    @Value("${auth-server.source.props.secret-name:}")
    String secretName;

    @Value("${auth-server.source.props.client-id:initial-value}")
    String clientId;

    @Value("${auth-server.source.props.client-secret:initial-value}")
    String clientSecret;

    public String getClientName() {
        return clientName;
    }

    public Boolean getEnabled() {
        return enabled;
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

    public Integer getTimeout() {
        return timeout;
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

    public String getEndPoint() {
        return endPoint;
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
