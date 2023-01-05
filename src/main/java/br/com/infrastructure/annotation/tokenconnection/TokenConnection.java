package br.com.infrastructure.annotation.tokenconnection;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

@Retention(RUNTIME)
@Target(TYPE)
public @interface TokenConnection {
    String url() default "";

    String clientId() default "";

    String clientSecret() default "";

    String grantType() default "";

    String timeout() default "5000";
}