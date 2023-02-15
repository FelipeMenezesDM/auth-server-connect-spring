package br.com.felipemenezesdm.integration.authserver.service;

import br.com.felipemenezesdm.Suite;
import br.com.felipemenezesdm.infrastructure.enums.ProvidersEnum;
import br.com.felipemenezesdm.integration.authserver.props.OAuthClientProps;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
public class CredentialsService {
    @Autowired
    OAuthClientProps oAuthClientProps;

    @Autowired
    Suite suite;

    @PostConstruct
    public void init() {
        System.setProperty("app.suite", oAuthClientProps.getProvider());
        System.setProperty("app.aws.region", oAuthClientProps.getRegion());
        System.setProperty("app.aws.end-point", oAuthClientProps.getEndPoint());
        System.setProperty("app.gcp.project-id", oAuthClientProps.getProjectId());
    }

    public JSONObject getCredentials() {
        if(ProvidersEnum.getByValue(oAuthClientProps.getProvider()).equals(ProvidersEnum.ENVIRONMENT)) {
            return getCredentialsFromEnvironmentVariables();
        }else if(ProvidersEnum.getByValue(oAuthClientProps.getProvider()).equals(ProvidersEnum.APPLICATION)) {
            return getCredentialsFromApplicationProperties();
        }

        try {
            return JSONObjectUtils.parse(suite.get().getSecretData(oAuthClientProps.getSecretName()));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
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
