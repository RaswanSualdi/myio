package com.myio.myincomeoutcome.service.impl;


import com.myio.myincomeoutcome.dto.request.AuthRequest;
import com.myio.myincomeoutcome.dto.response.LoginResponse;
import com.myio.myincomeoutcome.dto.response.RegisterResponse;
import com.myio.myincomeoutcome.entity.AppUser;
import com.myio.myincomeoutcome.entity.UserCredential;
import com.myio.myincomeoutcome.repository.UserCredentialRepository;
import com.myio.myincomeoutcome.security.JwtUtil;
import com.myio.myincomeoutcome.service.AuthService;
import com.myio.myincomeoutcome.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(AuthRequest request) {
        try {
            validationUtil.validate(request);
            // usercredential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            return RegisterResponse.builder()
                    .id(userCredential.getId())
                    .username(userCredential.getUsername())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }


    @Override
    public LoginResponse login(AuthRequest request) {
        validationUtil.validate(request);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername().toLowerCase(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // object AppUser
        AppUser appUser = (AppUser) authenticate.getPrincipal();
        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .token(token)
                .build();
    }


}
