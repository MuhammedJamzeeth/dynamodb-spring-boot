package com.dynamodb.main.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ConnectionCloseFilter extends HttpFilter {

    @Setter
    private static volatile boolean sendConnectionClose;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (sendConnectionClose && response != null) {
            if (response.isCommitted()) {
                log.info("Response is already committed - cannot send `Connection: close` header");
                return;
            }
            log.info("Sending `Connection: close` header on response");
            response.setHeader("Connection", "close");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
