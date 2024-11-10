//package com.example.bank.config.aop.logging;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Component
//@Aspect
//public class LoggingAspect {
//    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Before("execution(* com.example.sample.*.controller.*.*(..))")
//    public void logBeforeController(JoinPoint joinPoint) {
//        logger.info(((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes())
//                .getRequest().getRemoteAddr());
////        logger.info(joinPoint.getArgs()[0].toString());
//    }
//}
