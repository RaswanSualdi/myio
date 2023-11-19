package com.myio.myincomeoutcome.service;

import com.myio.myincomeoutcome.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadByUserId(String id);

}
