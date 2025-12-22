package com.example.boot.aspect;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author ljx
 * @version 1.0.0
 * @Aspect:  切面类的注解
 * 位置: 放在某个类的上面
 * 作用: 表示当前类是切面类
 * @create 2023/2/15 10:44
 */
@Aspect
@Component
public class MyAspect {

    /**
     * @Pointcut: 定义和管理切入点，不是通知注解
     * 属性: value 切入点表达式
     * 位置: 在一个自定义方法的上面，此方法被看作是切入点表达式的别名
     * 在其他通知注解中，可以使用此方法名称，表示使用这个切入点表达式
     */
    @Pointcut(value = "execution( * com.example.boot.controller.MyController.*(..))")
    public void helloPointCut() {
    }

    /*
      before 目标方法执行前执行，前置通知  ==>token验证
      after 目标方法执行后执行，后置通知  =》记录操纵日志
      after returning 目标方法返回时执行 ，后置返回通知 =》记录操纵日志
      after throwing 目标方法抛出异常时执行 异常通知   ==>记录异常日志
      around 在目标函数执行中执行，可控制目标函数是否执行，环绕通知 ==>token验证
     */

    /**
     * 环绕通知方法的定义
     * 1) 方法是public
     * 2) 必须有返回值，推荐使用Object类型
     * 3) 方法名称自定义
     * 4) 必须有ProceedingJoinPoint参数
     *
     * @Around: 环绕通知
     * 属性: value 切入点表达式
     * 位置: 在方法定义的上面
     * 测试方法中，调用目标方法doFirst(String name)去执行，
     * 实际上目标方法doFirst(String name)并未执行，而是执行了myAround(ProceedingJoinPoint proceedingJoinPoint)
     * 特点: 1) 在目标方法的前和后都能增强功能
     * 2) 控制目标方法是否执行
     * 3) 修改目标方法的执行结果
     */
    @Around("helloPointCut()")
    public Object aroundHello(ProceedingJoinPoint pjp) throws Throwable {
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        String className = pjp.getTarget().getClass().toString();
        String methodName = pjp.getSignature().getName();
        //获取参数
        Object[] parameters = pjp.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("调用前：" + className + "." + methodName + "传递的参数为" + mapper.writeValueAsString(parameters));
        Object obj = pjp.proceed(parameters);
        System.out.println("调用后 ：" + className + "." + methodName + "返回值" + mapper.writeValueAsString(obj));
        return obj;
    }

    /**
     * 前置通知方法的定义
     * 1) 方法是public
     * 2) 返回值是void
     * 3) 方法名称自定义
     * 4) 可以有参数，也可以无参数。如果有，参数是JoinPoint
     *
     * @Before: 前置通知
     * 属性: value 切入点表达式，表示切面的执行位置。在这个方法执行时，会同时执行切面的功能
     * 位置: 放在方法的上面
     * 特点: 1) 执行时间在目标方法之前先执行
     * 2) 不会影响目标方法的执行
     * 3) 不会修改目标方法的执行结果
     * 切面类中的通知方法，可以有参数。必须是JoinPoint
     * JoinPoint: 表示正在执行的业务方法，相当于反射中的Method
     * 使用要求: 必须是参数列表的第一个
     * 作用: 获取方法执行时的信息，例如方法名称、方法的参数集合
     */
    @Before("helloPointCut()")
    public void before(JoinPoint joinPoint) {
        //获取方法的完整定义
        System.out.println("前置通知，获取目标方法的定义: " + joinPoint.getSignature());
        //获取方法的名称
        System.out.println("前置通知，获取目标方法的名称: " + joinPoint.getSignature().getName());
        //获取方法执行时的参数
        Object[] args = joinPoint.getArgs();
        for (Object obj : args) {
            System.out.println("前置通知，获取目标方法的参数: " + obj);
        }
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("doSome")) {
            //切面的代码
            System.out.println("doSome输出日志=========前置通知，切面的功能，在目标方法之前先执行: " + new Date());
        } else if (methodName.equals("doOther")) {
            //切面的代码
            System.out.println("doOther输出日志========前置通知，作为方法名称、参数的记录");
        }
    }

    /**
     * 最终通知方法的定义
     * 1) 方法是public
     * 2) 返回值是void
     * 3) 方法名称自定义
     * 4) 方法无参数
     *
     * @After: 最终通知
     * 属性: value 切入点表达式
     * 位置: 在方法上面
     * 特点: 1) 在目标方法之后执行的
     * 2) 总是会被执行
     * 3) 可以用来做程序最后的收尾工作，例如清除临时数据、变量，清理内存
     * 可以看作如下语句块:
     * try {
     * SomeServiceImpl.doThird(..)
     * }finally {
     * myAfter()
     * }
     */
    @After("helloPointCut()")
    public void after() {
        System.out.println("最终通知，总是会被执行的");
    }

    /**
     * 后置通知方法的定义
     * 1) 方法是public
     * 2) 返回值是void
     * 3) 方法名称自定义
     * 4) 方法有参数，推荐使用Object
     *
     * @AfterReturning: 后置通知
     * 属性: value 切入点表达式
     * returning 自定义的变量，表示目标方法的返回值的返回值。
     * 自定义变量的名称必须和通知方法的形参名一样
     * 位置: 放在方法的上面
     * 特点: 1) 在目标方法之后执行
     * 2) 能获取到目标方法的执行结果
     * 3) 不会影响目标方法的执行
     * 方法的参数Object res，表示目标方法的返回值，使用res接收doOther的调用结果
     */
    @AfterReturning(pointcut = "helloPointCut()", returning = "result")
    public void afterReturn(Object result) {
        System.out.println("后置通知，在目标方法之后执行的，可以拿到执行结果：" + result.toString());
    }

    /**
     * 异常通知方法的定义
     * 1) 方法是public
     * 2) 返回值是void
     * 3) 方法名称自定义
     * 4) 方法有参数Exception
     *
     * @AfterThrowing: 异常通知
     * 属性: value 切入点表达式
     * throwing 自定义变量，表示目标方法抛出的异常。变量名必须和通知方法的形参名一样
     * 位置: 在方法上面
     * 特点: 1) 在目标方法抛出异常之后执行的，若没有异常 则不执行
     * 2) 能获取到目标方法的异常信息
     * 3) 不是异常处理程序，可以得到发生异常的通知，可以发送邮件、短信通知开发人员
     * 可以看作是目标方法的监控程序
     * 可以看作以下语句块:
     * try {
     * SomeServiceImpl.doSecond(..)
     * }catch(Exception ex) {
     * myAfterThrowing(Exception ex)
     * }
     */
    @AfterThrowing(pointcut = "helloPointCut()", throwing = "ex")
    public void AfterThrowing(Exception ex) {
        System.out.println("AfterThrowingHello" + ex.getMessage());
    }
}