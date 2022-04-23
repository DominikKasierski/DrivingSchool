package pl.lodz.p.it.dk.common.utils;

import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

@Log
public class LoggingInterceptor {

    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        String className = invocationContext.getTarget().getClass().getName();
        String methodName = invocationContext.getMethod().getName();
        Principal principal = securityContext.getCallerPrincipal();
        String caller = principal != null ? securityContext.getCallerPrincipal().getName() : "Guest";

        StringBuilder parameters = new StringBuilder();

        if (invocationContext.getParameters() != null && invocationContext.getParameters().length > 0) {
            for (Object parameter : invocationContext.getParameters()) {
                parameters.append(parameter.getClass().getSimpleName()).append(" ").append(parameter).append(", ");
            }
            parameters.delete(parameters.length() - 2, parameters.length());
        }

        log.info(String.format("%s.%s(%s) is called by %s", className, methodName, parameters.toString(), caller));

        Object result;

        try {
            result = invocationContext.proceed();
        } catch (Exception e) {
            String cause = e.getCause() != null ? ", caused by " + e.getCause().getClass().getName() : "";
            log.info(String.format("%s.%s(%s) threw %s%s", className, methodName, parameters.toString(),
                    e.getClass().getName(), cause));
            throw e;
        }

        //TODO: Mozna sie pokusic o rozroznienie miedzy void a reszta
        log.info(String.format("%s.%s(%s) returned %s", className, methodName, parameters.toString(), result));
        return result;
    }
}
