// package com.authguard.authguard.config;

// import java.io.IOException;

// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.FilterConfig;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;

// @Component
// @Order(-1000) // Spring Security ke filters se bhi pehle chalega
// public class RequestLoggingFilter implements Filter {

//     @Override
//     public void init(FilterConfig filterConfig) {}

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {

//         System.out.println("[👀 LOG] Incoming request: " + request.getRemoteAddr() + " to " + request.getContentType());
//         System.out.println("[👀 LOG] Full Request: " + request.toString());

//         chain.doFilter(request, response); // agla filter/process chalayega
//     }

//     @Override
//     public void destroy() {}
// }

