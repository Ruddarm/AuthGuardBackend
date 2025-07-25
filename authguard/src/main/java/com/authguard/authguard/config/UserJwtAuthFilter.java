package com.authguard.authguard.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authguard.authguard.model.domain.UserAuth;
import com.authguard.authguard.services.ClientService;
import com.authguard.authguard.services.JwtService;
import com.authguard.authguard.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException, UsernameNotFoundException, ExpiredJwtException {
        System.out.println("Inside user JWT AUTHFILter");
        final String tokenHeader = request.getHeader("Authorization");
        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                System.out.println("Header is null or something in UserJwt filter ");
                filterChain.doFilter(request, response);
                return;
            }
            String token = tokenHeader.split("Bearer ")[1];
            UUID clientId = jwtService.generateUserIdFromToken(token);
            UserAuth userAuth = userService.loadUserById(clientId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Iside context settig something in userJwt Filter");

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userAuth,
                        null,
                        null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            // Directly write error to response

            System.out.println(ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token is expired\"}");
            response.getWriter().flush();
        }

    }
    

}
