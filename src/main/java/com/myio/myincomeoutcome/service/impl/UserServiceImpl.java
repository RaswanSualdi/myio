package com.myio.myincomeoutcome.service.impl;

import com.myio.myincomeoutcome.entity.AppUser;
import com.myio.myincomeoutcome.entity.UserCredential;
import com.myio.myincomeoutcome.repository.UserCredentialRepository;
import com.myio.myincomeoutcome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;
    @Override
    public AppUser loadByUserId(String id) {
        UserCredential userCredential = userCredentialRepository.findById(id).
                orElseThrow(()->new UsernameNotFoundException("Invalid credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("invalid credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .build();
    }
}
