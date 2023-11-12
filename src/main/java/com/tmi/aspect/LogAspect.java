package com.tmi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
}
