package com.myio.myincomeoutcome.service;


import com.myio.myincomeoutcome.dto.request.AuthRequest;
import com.myio.myincomeoutcome.dto.response.LoginResponse;
import com.myio.myincomeoutcome.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
