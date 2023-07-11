package com.easybbs.aspect;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.StringTools;
import com.easybbs.utils.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class GlobalOperationAspect {
    public static final String[] TYPE_BASE = {"java.lang.String", "java.lang.Integer", "java.lang.Long"};

    @Pointcut("@annotation(com.easybbs.anotation.GlobalInterceptor)")
    public void requestInterceptor() {

    }

    @Before("requestInterceptor()")
    public void interceptorDo(JoinPoint point) throws BusinessException {
        try{
            Object target = point.getTarget();
            Object[] arguments = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if(null == interceptor) {
                return;
            }

            if(interceptor.checkParams()) {
                validateParams(method, arguments);
            }
        }catch (Exception e){
            throw new BusinessException("全局拦截器异常");
        }
    }

    private void validateParams(Method m, Object[] arguments) throws BusinessException{
        Parameter[] parameters = m.getParameters();
        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if(verifyParam == null) {
                continue;
            }
            if(ArrayUtils.contains(TYPE_BASE, parameter.getParameterizedType().getTypeName())) {
                checkValue(value, verifyParam);
            }else{
                checkObjValue(parameter, value);
            }
        }
    }

    private void checkObjValue(Parameter parameter, Object value) {
        try{
            String typeName = parameter.getParameterizedType().getTypeName();
            Class classz = Class.forName(typeName);
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldVerifyParam = field.getAnnotation(VerifyParam.class);
                if(fieldVerifyParam == null) {
                    continue;
                }
                field.setAccessible(true);
                Object resultValue = field.get(value);
                checkValue(resultValue, fieldVerifyParam);
            }
        }catch (Exception e) {
            throw new BusinessException("校验参数失败");
        }
    }

    private void checkValue(Object value, VerifyParam verifyParam) throws BusinessException{
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();

        if(isEmpty && verifyParam.required()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex(), String.valueOf(value))) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }
}
