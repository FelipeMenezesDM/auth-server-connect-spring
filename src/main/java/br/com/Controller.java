package br.com;

import br.com.infrastructure.annotation.tokenconnection.AuthServerConnection;
import br.com.integration.authserver.AuthServerToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AuthServerConnection
public class Controller {
    AuthServerToken authServerToken;

    @GetMapping
    public String test() {
        return authServerToken.getAccessToken().getTokenValue();
    }
}
