package br.com.infrastructure.annotation.tokenvalidation;

import br.com.integration.authserver.config.AuthServerAuthorizerConfig;
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

@Aspect
@Component
public class AuthServerValidationAspect{
    @Autowired
    AuthServerAuthorizerConfig authServerAuthorizerConfig;

    @Around("@within(authServerValidation)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        if(method.getAnnotation(AuthServerValidation.class) == null) {
            String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
            authServerAuthorizerConfig.validate(token, authServerValidation.scopes());
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("@annotation(authServerValidation)")
    public Object traceMethod(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        String[] scopes = authServerValidation.scopes();
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        AuthServerValidation classAnnotation = proceedingJoinPoint.getTarget().getClass().getAnnotation(AuthServerValidation.class);

        if(classAnnotation != null) {
            scopes = StringUtils.concatenateStringArrays(scopes, classAnnotation.scopes());
        }

        authServerAuthorizerConfig.validate(token, scopes);
        return proceedingJoinPoint.proceed();
    }
}
