package com.myio.myincomeoutcome.security;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.myio.myincomeoutcome.dto.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        CommonResponse<String> commonResponse = CommonResponse.<String>builder()
                .message(authException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();

        // merubah object menjadi jsonString
        String commonResponseString = objectMapper.writeValueAsString(commonResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(commonResponseString);
    }
}
