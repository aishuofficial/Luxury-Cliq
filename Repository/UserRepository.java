package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Optional;
import java.util.UUID;




@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    List<User> findByUsernameLike(String pattern);

    @Query(value = "SELECT * FROM user_info WHERE username LIKE :keyword% OR phone LIKE :keyword% OR phone LIKE :keyword% OR first_name LIKE :keyword% OR last_name LIKE :keyword% OR uuid like :keyword%", nativeQuery = true)
    Page<User> search(String keyword, Pageable pageable);

    Optional<User> findByusername(String username);

    User findByForgetpassword(String forgetpassword);

}

