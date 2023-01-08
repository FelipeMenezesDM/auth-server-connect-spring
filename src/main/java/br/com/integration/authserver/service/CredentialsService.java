package br.com.integration.authserver.service;

import br.com.infrastructure.enums.ProvidersEnum;
import br.com.integration.authserver.props.OAuthClientProps;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class CredentialsService {
    @Autowired
    OAuthClientProps oAuthClientProps;

    private final Map<ProvidersEnum, Supplier<?>> suppliers = new HashMap<>();

    private static final String DEFAULT_ENDPOINT = "//secretsmanager.%s.amazonaws.com";

    public CredentialsService() {
        suppliers.put(ProvidersEnum.GCP, this::getCredentialsFromGCPSecretManager);
        suppliers.put(ProvidersEnum.AWS, this::getCredentialsFromAWSSecretsManager);
        suppliers.put(ProvidersEnum.ENVIRONMENT, this::getCredentialsFromEnvironmentVariables);
        suppliers.put(ProvidersEnum.APPLICATION, this::getCredentialsFromApplicationProperties);
    }

    public JSONObject getCredentials() {
        ProvidersEnum provider = ProvidersEnum.getByValue(oAuthClientProps.getProvider());
        return (JSONObject) suppliers.get(provider).get();
    }

    private JSONObject getCredentialsFromAWSSecretsManager() {
        String region = oAuthClientProps.getRegion();
        String endPoint = Optional.ofNullable(oAuthClientProps.getEndpoint()).orElse(String.format(DEFAULT_ENDPOINT, region));
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region)).build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(oAuthClientProps.getSecretName());

        try {
            return JSONObjectUtils.parse(client.getSecretValue(getSecretValueRequest).getSecretString());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private JSONObject getCredentialsFromGCPSecretManager() {
        try {
            SecretManagerServiceClient client = SecretManagerServiceClient.create();
            SecretVersionName secretVersionName = SecretVersionName.of(oAuthClientProps.getProjectId(), oAuthClientProps.getSecretName(), "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            return JSONObjectUtils.parse(response.getPayload().getData().toStringUtf8());
        } catch (ParseException|IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
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
