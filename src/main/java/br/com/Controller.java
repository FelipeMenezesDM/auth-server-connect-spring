package br.com;

import br.com.infrastructure.annotation.tokenconnection.TokenConnection;
import br.com.integration.authserver.AuthServerConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@TokenConnection
public class Controller {
    AuthServerConnection authServerConnection;

    @GetMapping
    public String test() {
        return authServerConnection.getAccessToken().getTokenValue();
    }
}
