package com.myio.myincomeoutcome.controller;

import com.myio.myincomeoutcome.dto.request.AuthRequest;
import com.myio.myincomeoutcome.dto.response.CommonResponse;
import com.myio.myincomeoutcome.dto.response.LoginResponse;
import com.myio.myincomeoutcome.dto.response.RegisterResponse;
import com.myio.myincomeoutcome.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest request) {
        RegisterResponse registerResponse = authService.register(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("successfully register new customer")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        LoginResponse login = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("successfully login")
                .statusCode(HttpStatus.OK.value())
                .data(login)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}