package br.com.felipemenezesdm.infrastructure.annotation.tokenvalidation;

import br.com.felipemenezesdm.integration.authserver.service.AuthServerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.lang.reflect.Method;
import static br.com.felipemenezesdm.infrastructure.constant.General.*;

@Aspect
@Component
public class AuthServerValidationAspect{
    @Autowired
    AuthServerService authServerService;

    @Around("@within(authServerValidation)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        if(method.getAnnotation(AuthServerValidation.class) == null) {
            String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader(STR_AUTHORIZATION);
            authServerService.validate(token, authServerValidation.scopes());
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("@annotation(authServerValidation)")
    public Object traceMethod(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        String[] scopes = authServerValidation.scopes();
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader(STR_AUTHORIZATION);
        AuthServerValidation classAnnotation = proceedingJoinPoint.getTarget().getClass().getAnnotation(AuthServerValidation.class);

        if(classAnnotation != null) {
            scopes = StringUtils.concatenateStringArrays(scopes, classAnnotation.scopes());
        }

        authServerService.validate(token, scopes);
        return proceedingJoinPoint.proceed();
    }
}
