package com.authguard.authguard.controller;

import java.net.http.HttpResponse;

import org.hibernate.annotations.View;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ResourceException {
        String token[] = authService.validateLogin(loginRequest);
        Cookie refreshToken = new Cookie("refresh-token", token[1]);
        refreshToken.setHttpOnly(true);
        refreshToken.setPath("/auth/user/refresh");
        response.addCookie(refreshToken);
        return new ResponseEntity<>(LoginResponse.builder().accessToken(token[0]).clientID(token[2]).build(),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/login")
    public RedirectView loginredirct(@RequestParam String clientId, @RequestParam String appId,
            @RequestParam String redirectUrl) {
        String loginUrl = "http://localhost:5173" + "?clientId=" + clientId + "&appId=" + appId + "&redirectUrl="
                + redirectUrl;
        return new RedirectView(loginUrl);
    }

    @PostMapping("/verify/app/login")
    public ResponseEntity<UserResponse> validateapplogin(
            @Valid @RequestBody ClientUserLoginRequest clientUserLoginRequest, HttpServletResponse response)
            throws ResourceException, UsernameNotFoundException {
        String[] data = appUserAuthService.authenticate(clientUserLoginRequest);
        //
        // String[] { accessToken, refreshToken, authUser.getUserId().toString(),
        // user.getFirstName(),
        // user.getLastName(), user.getEmail() };
        //
        String cookie = String.format(
                "refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                data[1], 7 * 24 * 60 * 60);
        response.setHeader("Set-Cookie", cookie);

        return new ResponseEntity<>(
                UserResponse.builder().accessToken(data[0]).firstName(data[3]).lastName(data[4]).email(data[5]).build(),
                HttpStatus.OK);
    }

}
