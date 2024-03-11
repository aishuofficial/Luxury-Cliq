package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.CartRepo;
import com.LUXURYCLIQ.Repository.CategoryRepository;
import com.LUXURYCLIQ.Repository.WishlistRepository;
import com.LUXURYCLIQ.entity.Cart;
import com.LUXURYCLIQ.entity.Category;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class WishlistService {


    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CategoryRepository categoryRepository;


    public Optional<Category> getCategoryDetails(UUID uuid) {
        return categoryRepository.findById(uuid);
    }


    public List<Wishlist> getWishlistItems() {
        List<Wishlist> wishlists = wishlistRepository.findAll();
        return wishlists;
    }

    public Optional<Wishlist> getWishlistById(UUID uuid) {
        Optional<Wishlist> wishlist = wishlistRepository.findById(uuid);
        return wishlist;
    }

    public void deleteProduct(UUID uuid) {
        System.out.println("service'''''" + uuid);
        wishlistRepository.deleteById(uuid);
    }


    public void saveToWishlist(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }


    public List<Wishlist> findByUserEntity_Usernames(String username) {

        List<Wishlist> wishlists = wishlistRepository.findByUserEntity_Username(username);
        return wishlists;
    }

    /*public BigDecimal findTotal(List<Cart> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (Wishlist wishlist : wishlistItems) {
            BigDecimal quantityBigDecimal = BigDecimal.valueOf(wishlist.getQuantity());
            BigDecimal price = wishlist.getProductInfo().getPrice();
            BigDecimal subtotal = quantityBigDecimal.multiply(price);
            total = total.add(subtotal);
        }
        System.out.println("total: " + total);
        return total;
    }*/


    public Optional<Product> getProduct(UUID uuid) {
        Optional<Wishlist> wishlistItem = wishlistRepository.findById(uuid);
        System.out.println("cartItems in service" + wishlistItem);
        // Check if the wishlist item with the given UUID exists
        if (wishlistItem.isPresent()) {
            System.out.println("cartItems in service ifff/////" + wishlistItem);
            Product productInfo = wishlistItem.get().getProductInfo();
            return Optional.ofNullable(productInfo);
        } else {
            return Optional.empty();
        }

    }
}

