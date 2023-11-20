package com.myio.myincomeoutcome.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myio.myincomeoutcome.dto.request.AuthRequest;
import com.myio.myincomeoutcome.dto.response.CommonResponse;
import com.myio.myincomeoutcome.dto.response.LoginResponse;
import com.myio.myincomeoutcome.dto.response.RegisterResponse;
import com.myio.myincomeoutcome.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTest {
    @Mock
    private AuthService authService;
    private MockMvc mockMvc;
    @InjectMocks
    private AuthController authController;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void registerCustomer_Success() throws Exception {
        AuthRequest request = new AuthRequest("username", "password");
        RegisterResponse mockRegisterResponse = new RegisterResponse("userId", "username");

        when(authService.register(any(AuthRequest.class))).thenReturn(mockRegisterResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        verify(authService, times(1)).register(request);

        String content = result.getResponse().getContentAsString();
        CommonResponse<RegisterResponse> response = new ObjectMapper().readValue(content, new TypeReference<CommonResponse<RegisterResponse>>() {});
        assertEquals("successfully register new customer", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(mockRegisterResponse, response.getData());
    }

    @Test
    void login_Success() throws Exception {

        AuthRequest request = new AuthRequest("username", "password");
        LoginResponse mockLoginResponse = new LoginResponse("token");
        when(authService.login(any(AuthRequest.class))).thenReturn(mockLoginResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        verify(authService, times(1)).login(request);

        String content = result.getResponse().getContentAsString();
        CommonResponse<LoginResponse> response = new ObjectMapper().readValue(content, new TypeReference<CommonResponse<LoginResponse>>() {});
        assertEquals("successfully login", response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(mockLoginResponse, response.getData());
    }
}