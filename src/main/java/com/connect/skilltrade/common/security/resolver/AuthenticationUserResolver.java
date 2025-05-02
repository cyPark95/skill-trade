package com.connect.skilltrade.common.security.resolver;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(AuthenticationUser.class);
        boolean isLongType = parameter.getParameterType().equals(Long.class);
        return hasAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        if (requestContext == null) {
            throw new BusinessException(SecurityExceptionStatus.MISSING_REQUEST_CONTEXT);
        }

        Object attribute = requestContext.getAttribute(HttpHeaders.AUTHORIZATION, RequestAttributes.SCOPE_REQUEST);

        if (parameter.getParameterAnnotation(AuthenticationUser.class).required() && attribute == null) {
            throw new BusinessException(SecurityExceptionStatus.REQUIRED_AUTHENTICATION_USER_ID);
        }

        return attribute;
    }
}
