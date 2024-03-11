package com.LUXURYCLIQ.SuccessHandler;

import com.LUXURYCLIQ.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsernameProvider {
    @Autowired
    UserService userInfoService;

    public UUID get() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInfoService.findByUsername(username).getUuid();
    }

}


