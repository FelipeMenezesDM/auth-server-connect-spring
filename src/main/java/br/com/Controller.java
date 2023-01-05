package br.com;

import br.com.infrastructure.annotation.tokenconnection.TokenConnection;
import br.com.integration.authserver.dto.AuthServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@TokenConnection(
        url = "https://api.authserver.dev.felipemenezes.me/api/v1/token",
        clientId = "980fce89-55e8-46c5-b097-4be45ec11109",
        clientSecret = "fb856867-2530-442d-a7e4-4ff76d941831",
        grantType = "client_credentials"
)
public class Controller {
    AuthServerResponse authServerResponse;

    @GetMapping
    public String test() {
        return authServerResponse.getAccessToken();
    }
}
