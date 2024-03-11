package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Cart;
import com.LUXURYCLIQ.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    List<Wishlist> findByUserEntity_Username(String username);

}


