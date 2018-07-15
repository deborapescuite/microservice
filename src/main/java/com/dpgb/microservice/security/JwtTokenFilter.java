package com.dpgb.microservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = (token != null) ? jwtTokenProvider.getAuthentication(token) : null;
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);
        } catch (RuntimeException e) {
            FilterErrorDetail filterErrorDetail = new FilterErrorDetail(e.getMessage(),HttpStatus.UNAUTHORIZED);
            setErrorResponse((HttpServletResponse) res, filterErrorDetail);
        }
    }

    public void setErrorResponse(HttpServletResponse response, FilterErrorDetail filterErrorDetail){
        response.setStatus(filterErrorDetail.getStatus().value());
        response.setContentType("application/json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JSR310Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String json = mapper.writeValueAsString(filterErrorDetail);
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class FilterErrorDetail {
        private LocalDateTime timestamp;
        private String message;
        private HttpStatus status;

        public FilterErrorDetail(String message, HttpStatus status) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }
    }
}
