package br.com;

import br.com.infrastructure.annotation.tokenvalidation.AuthServerValidation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AuthServerValidation(scopes = "teste1")
public class Controller {
    @GetMapping
    @AuthServerValidation(scopes = {"teste1", "teste2", "teste3"})
    public String test() {
        return "";
    }
}
