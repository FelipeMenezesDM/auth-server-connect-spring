package br.com.integration.authserver.service;

import br.com.infrastructure.enums.ProvidersEnum;
import br.com.integration.authserver.props.OAuthClientProps;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {
    @Autowired
    OAuthClientProps oAuthClientProps;

    @Autowired
    AWSSecretsManager awsSecretsManager;

    public JSONObject getCredentials() {
        ProvidersEnum provider = ProvidersEnum.getByValue(oAuthClientProps.getProvider());

        switch(provider) {
            case GCP:
                return getCredentialsFromGCPSecretManager();
            case AWS :
                return getCredentialsFromAWSSecretsManager();
            case ENVIRONMENT:
                return getCredentialsFromEnvironmentVariables();
            case APPLICATION:
                return getCredentialsFromApplicationProperties();
            default:
                throw new IllegalArgumentException();
        }
    }

    private JSONObject getCredentialsFromAWSSecretsManager() {
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(oAuthClientProps.getSecretName());

        try {
            return JSONObjectUtils.parse(awsSecretsManager.getSecretValue(getSecretValueRequest).getSecretString());
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    private JSONObject getCredentialsFromGCPSecretManager() {
        return new JSONObject();
    }

    private JSONObject getCredentialsFromEnvironmentVariables() {
        return getCredentialsObject(
                System.getenv(oAuthClientProps.getClientIdKey()),
                System.getenv(oAuthClientProps.getClientSecretKey())
        );
    }

    private JSONObject getCredentialsFromApplicationProperties() {
        return getCredentialsObject(oAuthClientProps.getClientId(), oAuthClientProps.getClientSecret());
    }

    private JSONObject getCredentialsObject(String clientId, String clientSecret) {
        JSONObject credentials = new JSONObject();
        credentials.appendField(oAuthClientProps.getClientIdKey(), clientId);
        credentials.appendField(oAuthClientProps.getClientSecretKey(), clientSecret);

        return credentials;
    }
}
