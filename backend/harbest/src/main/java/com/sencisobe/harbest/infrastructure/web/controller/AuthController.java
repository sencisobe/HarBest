package com.sencisobe.harbest.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sencisobe.harbest.application.dto.ForgotPasswordRequest;
import com.sencisobe.harbest.application.dto.LoginResult;
import com.sencisobe.harbest.application.dto.ResetPasswordRequest;
import com.sencisobe.harbest.application.usecase.LoginUser;
import com.sencisobe.harbest.application.usecase.RegisterUser;
import com.sencisobe.harbest.application.usecase.RequestPasswordReset;
import com.sencisobe.harbest.infrastructure.web.dto.LoginRequest;
import com.sencisobe.harbest.infrastructure.web.dto.RegisterRequest;
import com.sencisobe.harbest.infrastructure.web.dto.TokenResponse;
import com.sencisobe.harbest.application.usecase.ResetPassword;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegisterUser registerUser;
    private final LoginUser loginUser;
    private final RequestPasswordReset requestPasswordReset;
    private final ResetPassword resetPassword;

    public AuthController(RegisterUser registerUser, LoginUser loginUser,RequestPasswordReset requestPasswordReset,ResetPassword resetPassword) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
        this.requestPasswordReset = requestPasswordReset;
        this.resetPassword = resetPassword;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        registerUser.execute(request.email(), request.username(), request.password());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
    LoginResult result = loginUser.execute(request.identifier(), request.password());
    return new TokenResponse(result.token(), result.username());  
    }
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        requestPasswordReset.execute(request.identifier());
    }
    @PostMapping("/reset-password") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPassword.execute(request.token(), request.newPassword());
}
}