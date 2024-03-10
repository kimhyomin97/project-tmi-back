package com.tmi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("@annotation(LogExecutionTime)") // @LogExecutionTime이 붙은 메소드에만 적용
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(); // 성능측정 시작

        Object proceed = joinPoint.proceed(); // 커스텀 어노테이션을 붙여놓은 메소드 실행

        stopWatch.stop(); // 성능측정 종료
        log.info(stopWatch.prettyPrint());

        return proceed;
    }
    @Pointcut("execution(* com.tmi.controller..*.*(..))")
    private void cut() {}

    @Before("cut()")
    public void before(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("method: {}", method.getName());

        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i= 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            Class<?> parameterType = parameter.getType();
            if (parameterType.isPrimitive() || parameterType.getName().startsWith("java.lang")) {
                // 기본 타입 출력
                log.info("{} : {}", parameter.getName(), arg);
            } else {
                // 객체 타입 출력
                log.info("{} : {}", parameter.getName(), arg);
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        log.info("    {} : {}", field.getName(), field.get(arg));
                    } catch (IllegalAccessException e) {
                        log.error("Error accessing field {}", field.getName(), e);
                    }
                }
            }
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, Object returnObj){
        log.info("return obj : {}", returnObj);
    }
}
