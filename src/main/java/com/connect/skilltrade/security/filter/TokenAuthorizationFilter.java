package com.connect.skilltrade.security.filter;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.common.exception.ExceptionStatus;
import com.connect.skilltrade.common.response.ExceptionResponse;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final TokenExtractor tokenExtractor;
    private final String grantType;

    public TokenAuthorizationFilter(
            ObjectMapper objectMapper,
            TokenExtractor tokenExtractor,
            @Value("${security.grant-type}") String grantType
    ) {
        this.objectMapper = objectMapper;
        this.tokenExtractor = tokenExtractor;
        this.grantType = grantType;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isValidAuthorizationHeader(authorization)) {
            sendExceptionResponse(response, SecurityExceptionStatus.INVALID_AUTHORIZATION);
            return;
        }

        String accessToken = authorization.substring(grantType.length()).trim();
        if (!StringUtils.hasText(accessToken)) {
            sendExceptionResponse(response, SecurityExceptionStatus.INVALID_AUTHORIZATION);
            return;
        }

        try {
            Long userId = tokenExtractor.extractUserId(accessToken);
            log.info("Authorization User ID: {}", userId);

            RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute(HttpHeaders.AUTHORIZATION, userId, RequestAttributes.SCOPE_REQUEST);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            log.warn("Authorization Exception Message: {}", e.getMessage(), e.getCause());
            sendExceptionResponse(response, e.getExceptionStatus(), e.getMessage());
        } catch (Exception e) {
            log.error("Authorization Exception Message: {}", e.getMessage(), e);
            sendExceptionResponse(response, SecurityExceptionStatus.INTERNAL_AUTHORIZATION_ERROR);
        }
    }

    private boolean isValidAuthorizationHeader(String value) {
        return StringUtils.hasText(value) && value.startsWith(grantType);
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
