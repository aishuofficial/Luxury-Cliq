package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUser(User userInfo) {

        if(userInfo.getUsername().equals("anonymousUser")){
            return "username";
        }

        Optional<User> existingUser1=userInfoRepository.findByUsername(userInfo.getUsername());

        User existingUser2=userInfoRepository.findByEmail(userInfo.getEmail());
        User existingUser3=userInfoRepository.findByPhone(userInfo.getPhone());

        if(existingUser1.isPresent()) {
            return "username";
        }
        if(existingUser2!=null) {
            return "email";
        }
        if(existingUser3!=null) {
            return "phone";
        }

        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        userInfo.setVerified(false);

        userInfo=userInfoRepository.save(userInfo);

        return "signupSuccess";

    }
}


