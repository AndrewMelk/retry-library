package com.retry.aspect;

import com.retry.api.RetryOnException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class RepeaterAspect {
    
    @Around("execution(* * (..)) && @annotation(com.wd.mycloud.das.aspect.RetryOnException)")
    public Object wrap(ProceedingJoinPoint point) throws Throwable {
        Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
        RetryOnException roe = point.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).getAnnotation(RetryOnException.class);
        
        int attempt = 0;
        int attempts = roe.attempts();
        long delay = roe.delay();
        Class[] exceptions = roe.types();

        while (true){
            try {
                return point.proceed();
            } catch (InterruptedException var12) {
                Thread.currentThread().interrupt();
                throw var12;
            } catch (Throwable throwable) {
                if (!matches(throwable.getClass(), exceptions)){
                    throw throwable;
                }

                ++attempt;
                if (attempt >= attempts){
                    throw  throwable;
                }

                if (delay > 0L) {
                    Thread.sleep(delay);
                }
            }
        }
    }

    private static boolean matches(Class<? extends Throwable> thrown, Class... types) {

        boolean matches = false;
        Class[] var6 = types;
        int var5 = types.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Class type = var6[var4];
            if (type.isAssignableFrom(thrown)) {
                matches = true;
                break;
            }
        }
        return matches;
    }
}
