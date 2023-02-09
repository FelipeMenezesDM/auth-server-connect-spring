package br.com.felipemenezesdm.infrastructure.annotation.tokenconnection;

import br.com.felipemenezesdm.integration.authserver.AuthServerToken;
import br.com.felipemenezesdm.integration.authserver.service.AuthServerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Aspect
@Component
public class AuthServerConnectionAspect {
    @Autowired
    AuthServerService authServerService;

    @Around("@within(AuthServerConnection)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();

        for(Field field : target.getClass().getDeclaredFields()) {
            if(field.getType().isAssignableFrom(AuthServerToken.class)) {
                field.setAccessible(true);
                field.set(target, authServerService.authorize());
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
