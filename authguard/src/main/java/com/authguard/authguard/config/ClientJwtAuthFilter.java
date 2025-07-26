package com.authguard.authguard.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authguard.authguard.model.domain.ClientAuth;
import com.authguard.authguard.services.ClientService;
import com.authguard.authguard.services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientJwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ClientService clientService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException, UsernameNotFoundException, ExpiredJwtException {
        final String tokenHeader = request.getHeader("Authorization");
        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                System.out.println("Header is null or something in Client Jwt Filter");
                filterChain.doFilter(request, response);
                return;
            }
            String token = tokenHeader.split("Bearer ")[1];
            UUID clientId = jwtService.generateUserIdFromToken(token);
            ClientAuth clientUser = clientService.loadUserById(clientId)
                    .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(clientUser,
                        null,
                        null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            System.out.println("going next filter");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            System.out.println(ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token is expired\"}");
            response.getWriter().flush();
        }

    }

}
