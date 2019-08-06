package com.example.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Pointcut("execution(* *..*(..))")
    public void methodsInExamplesPackage() {}

    //@Before("publicMethod() && methodsInExamplesPackage()")

    @Before("execution(* *(..)) && @annotation(Logging)")
    public void addLog( final JoinPoint joinPoint ) {
        System.out.println(" *********** XExecuting: "+joinPoint.getSignature());

        Object[] arguments = joinPoint.getArgs();
        for (Object argument : arguments) {
            if (argument != null) {
                System.out.println(" *********** " + argument.getClass().getSimpleName() + " = " + argument);
            }
        }
    }

}
