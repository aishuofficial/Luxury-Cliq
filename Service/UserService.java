package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userInfoRepository;


    public List<User> loadAllUsers() {
        return userInfoRepository.findAll();
    }

    public Page<User> loadAllUsers(Pageable pageable) {
        return userInfoRepository.findAll(pageable);
    }


//	search


    public User getUser(UUID uuid) {
        return userInfoRepository.findById(uuid).orElse(null);
    }

    public void updateUser(User userInfo) {
        userInfoRepository.save(userInfo);
    }

    public User save(User userInfo) {
        return userInfoRepository.save(userInfo);
    }

    public void delete(UUID uuid) {
        userInfoRepository.deleteById(uuid);
    }


    public Optional<User> searchUsers(String keyword) {
        return userInfoRepository.findByUsername(keyword);
    }

    public User findByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userInfoRepository.findByUsername(username).orElse(null);
    }

    public User findByPhone(String phone) {
        return userInfoRepository.findByPhone(phone);
    }

    public Page<User> search(String keyword, Pageable pageable) {
        return userInfoRepository.search(keyword, pageable);
    }


    public Optional<User> getUserdata(String username) {
        return userInfoRepository.findByusername(username);
    }


    public User findByUsernames(String username) {
        return userInfoRepository.findByusername(username).orElse(null);
    }

   public  void updateForgetPassword(String token,String email)
   {
       User user = userInfoRepository.findByEmail(email);
       if (user != null)
       {
           user.setForgetpassword(token);
           userInfoRepository.save(user);
       } else
       {
           throw new UsernameNotFoundException("could not find any user with email" + email);
       }
   }

  public  User get(String forgetPassword) {
      return userInfoRepository.findByForgetpassword(forgetPassword);
  }
  public void updatePassword(User user,String newPassword){
       BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
       String encodedPassword=passwordEncoder.encode(newPassword);

       user.setPassword(encodedPassword);
       user.setForgetpassword(null);

       userInfoRepository.save(user);

  }


}