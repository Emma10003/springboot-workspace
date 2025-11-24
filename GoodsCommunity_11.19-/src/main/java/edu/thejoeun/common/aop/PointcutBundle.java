package edu.thejoeun.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutBundle {

    @Pointcut("execution(* edu.thejoeun..*Controller*.*(..))")
    public void controllerPointCut(){}

    @Pointcut("execution(* edu.thejoeun..*ServiceImpl*.*(..))")
    public void serviceImplPointCut(){}
}


