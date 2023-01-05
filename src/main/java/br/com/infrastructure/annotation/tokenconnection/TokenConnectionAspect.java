package br.com.infrastructure.annotation.tokenconnection;

import br.com.integration.authserver.dto.AuthServerResponse;
import br.com.integration.authserver.service.AuthServerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Aspect
@Component
public class TokenConnectionAspect {
    @Autowired
    AuthServerService authServerService;

    @Around("@within(TokenConnection)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();
        Field[] fields = target.getClass().getDeclaredFields();
        TokenConnection tokenConnection = target.getClass().getAnnotation(TokenConnection.class);

        for(Field field : fields) {
            if(field.getType().isAssignableFrom(AuthServerResponse.class)) {
                field.setAccessible(true);
                field.set(target, authServerService.getToken(
                        tokenConnection.url(),
                        tokenConnection.clientId(),
                        tokenConnection.clientSecret(),
                        tokenConnection.grantType()
                ));
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
