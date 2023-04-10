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
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import static br.com.felipemenezesdm.infrastructure.constant.General.*;

@Aspect
@Component
public class AuthServerValidationAspect{
    @Autowired
    AuthServerService authServerService;

    @Around("@within(authServerValidation)")
    public Object trace(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        if(Objects.isNull(method.getAnnotation(AuthServerValidation.class))) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            authServerService.validate(
                    request.getHeader(STR_AUTHORIZATION),
                    request.getHeader(STR_CORRELATION_ID),
                    request.getHeader(STR_FLOW_ID),
                    request.getHeader(STR_API_KEY),
                    authServerValidation.scopes()
            );
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("@annotation(authServerValidation)")
    public Object traceMethod(ProceedingJoinPoint proceedingJoinPoint, AuthServerValidation authServerValidation) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        AuthServerValidation annotation = proceedingJoinPoint.getTarget().getClass().getAnnotation(AuthServerValidation.class);
        String[] scopes = authServerValidation.scopes();

        if(!Objects.isNull(annotation)) {
            scopes = Arrays.stream(Objects.requireNonNull(StringUtils.concatenateStringArrays(scopes, annotation.scopes()))).distinct().toArray(String[]::new);
        }

        authServerService.validate(
                request.getHeader(STR_AUTHORIZATION),
                request.getHeader(STR_CORRELATION_ID),
                request.getHeader(STR_FLOW_ID),
                request.getHeader(STR_API_KEY),
                scopes
        );

        return proceedingJoinPoint.proceed();
    }
}
