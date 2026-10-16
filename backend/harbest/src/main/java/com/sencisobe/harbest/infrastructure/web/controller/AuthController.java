package com.sencisobe.harbest.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sencisobe.harbest.application.usecase.LoginUser;
import com.sencisobe.harbest.application.usecase.RegisterUser;
import com.sencisobe.harbest.infrastructure.web.dto.LoginRequest;
import com.sencisobe.harbest.infrastructure.web.dto.RegisterRequest;
import com.sencisobe.harbest.infrastructure.web.dto.TokenResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegisterUser registerUser;
    private final LoginUser loginUser;

    public AuthController(RegisterUser registerUser, LoginUser loginUser) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        registerUser.execute(request.email(), request.username(), request.password());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return new TokenResponse(loginUser.execute(request.identifier(), request.password()));
    }
}