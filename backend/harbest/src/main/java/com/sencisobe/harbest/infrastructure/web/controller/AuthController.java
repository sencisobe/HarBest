package com.sencisobe.harbest.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sencisobe.harbest.application.usecase.LoginUser;
import com.sencisobe.harbest.application.usecase.RegisterUser;
import com.sencisobe.harbest.infrastructure.web.dto.AuthRequest;
import com.sencisobe.harbest.infrastructure.web.dto.TokenResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterUser registerUser;
    private final LoginUser loginUser;

    public AuthController(RegisterUser registerUser, LoginUser loginUser) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody AuthRequest request) {
        registerUser.execute(request.email(), request.password());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AuthRequest request) {
        return new TokenResponse(loginUser.execute(request.email(), request.password()));
    }
}