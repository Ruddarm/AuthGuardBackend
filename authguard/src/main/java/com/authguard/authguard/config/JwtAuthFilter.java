package com.authguard.authguard.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authguard.authguard.model.domain.ClientUser;
import com.authguard.authguard.services.ClientUserService;
import com.authguard.authguard.services.JwtService;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ClientUserService clientUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException, UsernameNotFoundException {
        final String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            System.out.println("Header is null or something ");
            filterChain.doFilter(request, response);
            return;
        }
        String token = tokenHeader.split("Bearer ")[1];
        UUID clientId = jwtService.generateUserIdFromToken(token);
        ClientUser clientUser = clientUserService.loadUserById(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("Client not foudn"));
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Iside context settig something ");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(clientUser, null,
                    null);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

}
