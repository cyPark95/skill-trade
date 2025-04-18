package com.connect.skilltrade.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String THREAD_ID = "REQUEST_ID";
    private static final int MAX_LOG_BODY_LENGTH = 500;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put(THREAD_ID, UUID.randomUUID().toString().substring(0, 8));

        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            requestLogging(cachingRequestWrapper);
            filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        } finally {
            stopWatch.stop();
            responseLogging(cachingResponseWrapper, stopWatch.getTotalTimeMillis());
            cachingResponseWrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void requestLogging(ContentCachingRequestWrapper request) {
        log.info(
                "[REQUEST] {} {}{} Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                System.lineSeparator(),
                getLimitedBody(request.getContentAsByteArray())
        );
    }

    private void responseLogging(ContentCachingResponseWrapper response, long totalTimeMillis) {
        log.info(
                "[RESPONSE] Status: {} | Duration: {} ms{} Body: {}",
                response.getStatus(),
                totalTimeMillis,
                System.lineSeparator(),
                getLimitedBody(response.getContentAsByteArray())
        );
    }

    private String getLimitedBody(byte[] body) {
        String bodyStr = new String(body, StandardCharsets.UTF_8);
        if (bodyStr.length() > MAX_LOG_BODY_LENGTH) {
            return bodyStr.substring(0, MAX_LOG_BODY_LENGTH) + "...(truncated)";
        }
        return bodyStr;
    }
}
