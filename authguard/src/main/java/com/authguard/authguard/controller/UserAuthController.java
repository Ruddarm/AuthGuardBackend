package com.authguard.authguard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.ClientUserLoginRequest;
import com.authguard.authguard.model.dto.LoginRequest;
import com.authguard.authguard.model.dto.LoginResponse;
import com.authguard.authguard.model.dto.UserRequest;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.mapper.UserMapper;
import com.authguard.authguard.services.AppUserAuthService;
import com.authguard.authguard.services.AuthService;
import com.authguard.authguard.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserAuthController {
        private final UserService userService;
        private final AuthService authService;
        private final UserMapper userMapper;
        private final AppUserAuthService appUserAuthService;

        @PostMapping("/signup")
        public ResponseEntity<UserResponse> userSignup(@Valid @RequestBody UserRequest userRequest,
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ResourceException {
                UserResponse userResponse = userService.saveUser(userMapper.userRequestToUserEntity(userRequest));
                return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        }

        // user login route
        @PostMapping("/login")
        public ResponseEntity<LoginResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ResourceException {
                String token[] = authService.validateUserLogin(loginRequest);
                // Cookie refreshToken = new Cookie("refresh-token", token[1]);
                // refreshToken.setHttpOnly(true);
                // refreshToken.setPath("/auth/user/refresh");
                // response.addCookie(refreshToken);

                String cookie = String.format(
                                "user_refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                                token[1], 7 * 24 * 60 * 60);
                response.setHeader("Set-Cookie", cookie);
                return new ResponseEntity<>(
                                LoginResponse.builder().accessToken(token[0]).userId(token[2]).userEmail(token[3])
                                                .build(),
                                HttpStatus.ACCEPTED);
        }

        // user token refresh route
        @GetMapping("/refresh")
        public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request,
                        HttpServletResponse response) throws ResourceException {
                Cookie[] cookies = request.getCookies();
                String refreshToken = null;
                for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("user_refresh_token")) {
                                refreshToken = cookie.getValue();
                                break;
                        }
                }
                if (refreshToken == null)
                        throw new ResourceException("refresh token not found");
                // System.out.println(refreshToken);
                String[] tokens = authService.refreshToken(refreshToken);
                // Cookie NewrefreshToken = new Cookie("refresh-token", tokens[1]);
                // NewrefreshToken.setHttpOnly(true);
                // NewrefreshToken.setPath("/auth/client/refresh");
                // response.addCookie(NewrefreshToken);
                String cookie = String.format(
                                "user_refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                                tokens[1], 7 * 24 * 60 * 60);
                response.setHeader("Set-Cookie", cookie);
                return new ResponseEntity<>(
                                LoginResponse.builder().accessToken(tokens[0]).userId(tokens[2]).userEmail(tokens[3])
                                                .build(),
                                HttpStatus.ACCEPTED);
                // return new ResponseEntity<LoginResponse>();
        }

        // Redirect to authgaurd client user login portal
        @GetMapping("/login")
        public RedirectView loginredirct(@RequestParam String clientId, @RequestParam String appId,
                        @RequestParam String redirectUrl) {
                String loginUrl = "http://localhost:5173" + "?clientId=" + clientId + "&appId=" + appId
                                + "&redirectUrl="
                                + redirectUrl;
                return new RedirectView(loginUrl);
        }

        // verify login of client app for user
        @PostMapping("/oath/app/code")
        public ResponseEntity<String> generateCodeApp(
                        @Valid @RequestBody ClientUserLoginRequest clientUserLoginRequest,
                        HttpServletResponse response)
                        throws ResourceException, UsernameNotFoundException, JsonProcessingException {
                String authCode = appUserAuthService.authenticateAndGenerateCode(clientUserLoginRequest);
                return ResponseEntity.ok(authCode);
        }

}
