package com.wzyy.springbootinit.aop;


import com.wzyy.springbootinit.annotation.RequestLimit;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 权限校验 AOP
 *
 * @description
 * @author: Wangzhaoyi
 */
@Aspect
@Component
public class RequestLimitInterceptor {

    @Resource
    private UserService userService;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String REDISHEAD="requestcount:";


    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param requestLimit
     * @return
     */
    @Around("@annotation(requestLimit)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, RequestLimit requestLimit) throws Throwable {
        int count = requestLimit.count();
        long time = requestLimit.time();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser();
        // 必须有该权限才通过
        ThrowUtils.throwIf(loginUser==null, ErrorCode.NOT_LOGIN_ERROR);
        String key = REDISHEAD+loginUser.getId().toString() + request.getRequestURI();
        Boolean b = stringRedisTemplate.hasKey(key);
        if(Boolean.FALSE.equals(b)){
            stringRedisTemplate.opsForValue().set(key,"1",time, TimeUnit.SECONDS);
        }else {
            String hasCount = stringRedisTemplate.opsForValue().get(key);
            if(hasCount!=null&&Integer.parseInt(hasCount) <= count){
                int num = Integer.parseInt(hasCount)+1;
                stringRedisTemplate.opsForValue().set(key, String.valueOf(num), time, TimeUnit.SECONDS);
            }else {
                throw new BusinessException(ErrorCode.WAIT_TIME_ERROR);
            }
        }
        return joinPoint.proceed();
    }
}

