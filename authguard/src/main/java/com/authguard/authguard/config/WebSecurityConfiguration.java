package com.authguard.authguard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authguard.authguard.services.ClientUserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final ClientUserService clientUserSerivce;
    private final JwtAuthFilter jwtAuthFilter;
    private final PasswordEncoder pswdEncoder;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                (auth) -> auth.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
                .csrf(crsfConfig -> crsfConfig.disable()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return httpSecurity.build();
    }

    @Bean
    DaoAuthenticationProvider daoProvdier() {
        DaoAuthenticationProvider daoprovider = new DaoAuthenticationProvider(clientUserSerivce);
        daoprovider.setPasswordEncoder(pswdEncoder);
        return daoprovider;
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }


}
