package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<Cart,UUID> {



        List<Cart> findByUserEntity_Username(String username);

    }








//    List<Cart> findAll();

//    Optional<Cart> findByUuidAndDeletedFalse(@Param("uuid") UUID uuid);
