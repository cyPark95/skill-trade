package com.connect.skilltrade.security.filter;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.common.exception.ExceptionStatus;
import com.connect.skilltrade.common.response.ExceptionResponse;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.TokenExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper objectMapper;
    private final TokenExecutor tokenExecutor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        log.info("requestURI: {}", request.getRequestURI());

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isValidAuthorizationHeader(authorization)) {
            sendExceptionResponse(response, SecurityExceptionStatus.INVALID_AUTHORIZATION);
            return;
        }

        String accessToken = authorization.substring(BEARER_PREFIX.length()).trim();
        if (!StringUtils.hasText(accessToken)) {
            sendExceptionResponse(response, SecurityExceptionStatus.INVALID_AUTHORIZATION);
            return;
        }

        try {
            Long userId = tokenExecutor.executeUserId(accessToken);
            log.info("Authorization user ID: {}", userId);

            RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute(HttpHeaders.AUTHORIZATION, userId, RequestAttributes.SCOPE_REQUEST);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            log.warn("Authentication Exception Message: {}", e.getMessage(), e.getCause());
            sendExceptionResponse(response, e.getExceptionStatus(), e.getMessage());
        } catch (Exception e) {
            log.error("Authentication Exception Message: {}", e.getMessage(), e);
            sendExceptionResponse(response, SecurityExceptionStatus.INTERNAL_AUTH_ERROR);
        }
    }

    private boolean isValidAuthorizationHeader(String value) {
        return StringUtils.hasText(value) && value.startsWith(BEARER_PREFIX);
    }

    private void sendExceptionResponse(HttpServletResponse response, ExceptionStatus status) throws IOException {
        sendExceptionResponse(response, status, status.getMessage());
    }

    private void sendExceptionResponse(HttpServletResponse response, ExceptionStatus status, String message) throws IOException {
        response.setStatus(status.getHttpStatus().value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, status.getCode());
        objectMapper.writeValue(response.getWriter(), exceptionResponse);
    }
}
