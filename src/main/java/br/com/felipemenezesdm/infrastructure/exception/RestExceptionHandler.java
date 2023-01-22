package br.com.felipemenezesdm.infrastructure.exception;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    protected ResponseEntity<Object> handler(HttpClientErrorException.Forbidden e) throws ParseException {
        return buildResponseEntity(e.getResponseBodyAsString(), e.getStatusCode());
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    protected ResponseEntity<Object> handler(HttpClientErrorException.Unauthorized e) throws ParseException {
        return buildResponseEntity(e.getResponseBodyAsString(), e.getStatusCode());
    }

    private ResponseEntity<Object> buildResponseEntity(String payload, HttpStatus status) throws ParseException {
        return new ResponseEntity<>(JSONUtils.parseJSONKeepingOrder(payload), status);
    }
}
