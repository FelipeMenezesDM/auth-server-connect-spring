package br.com.integration.authserver.config;

import br.com.integration.authserver.props.OAuthClientProps;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Optional;

@Configuration
public class AWSSecretsManagerConfig {
    @Autowired
    OAuthClientProps oAuthClientProps;

    private static final String DEFAULT_ENDPOINT = "//secretsmanager.%s.amazonaws.com";

    @Bean
    public AWSSecretsManager awsSecretsManager() {
        return AWSSecretsManagerClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(getEndpoint(), getRegion()))
                .build();
    }

    private String getEndpoint() {
        return Optional.ofNullable(oAuthClientProps.getEndpoint()).orElse(String.format(DEFAULT_ENDPOINT, getRegion()));
    }

    private String getRegion() {
        return oAuthClientProps.getRegion();
    }
}
