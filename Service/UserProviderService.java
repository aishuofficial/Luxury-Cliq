package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Security.UserToUserDetailsConversion;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProviderService implements UserDetailsService {

    @Autowired
    UserRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userInfo =   userInfoRepository.findByUsername(username);
        System.out.println(username+userInfo.get());
        return userInfo.map(UserToUserDetailsConversion::new)
                .orElseThrow(()-> new UsernameNotFoundException("user not found:"+username));

    }
}




