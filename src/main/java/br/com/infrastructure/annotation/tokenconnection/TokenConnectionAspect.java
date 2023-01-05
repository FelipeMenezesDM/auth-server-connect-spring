package br.com.infrastructure.annotation.tokenconnection;

import br.com.integration.authserver.AuthServerConnection;
import br.com.integration.authserver.config.AuthServerAuthorizerConfig;
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
    AuthServerAuthorizerConfig authServerAuthorizerConfig;

    @Around("@within(TokenConnection)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();

        for(Field field : target.getClass().getDeclaredFields()) {
            if(field.getType().isAssignableFrom(AuthServerConnection.class)) {
                field.setAccessible(true);
                field.set(target, authServerAuthorizerConfig.authorize());
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
