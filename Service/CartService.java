package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.CartRepo;
import com.LUXURYCLIQ.Repository.CategoryRepository;
import com.LUXURYCLIQ.entity.Cart;
import com.LUXURYCLIQ.entity.Category;
import com.LUXURYCLIQ.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class CartService {


    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    CartRepo cartRepo;


    public Optional<Category> getCategoryDetails(UUID uuid) {
        return categoryRepo.findById(uuid);
    }


    public List<Cart> getCartItems() {
        List<Cart> cartList = cartRepo.findAll();
        return cartList;
    }

    public Optional<Cart> getCartById(UUID uuid) {
        Optional<Cart> cartList = cartRepo.findById(uuid);
        return cartList;
    }

//    @Override
//    public List<Cart> getCartInfo() {
//        return cartRepo.findAll();
//    }


    public void deleteProduct(UUID uuid) {
        System.out.println("service'''''" + uuid);
        cartRepo.deleteById(uuid);
    }


    public void saveToCart(Cart cart) {
        cartRepo.save(cart);
    }


    public List<Cart> findByUserEntity_Usernames(String username) {

        List<Cart> cartList = cartRepo.findByUserEntity_Username(username);

        return cartList;
    }


    public BigDecimal findTotal(List<Cart> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            BigDecimal quantityBigDecimal = BigDecimal.valueOf(cart.getQuantity());
            BigDecimal price = cart.getProductInfo().getPrice();
            BigDecimal subtotal = quantityBigDecimal.multiply(price);
            total = total.add(subtotal);
        }
        System.out.println("total: " + total);
        return total;
    }


    public Optional<Product> getProduct(UUID uuid) {
        Optional<Cart> cartItem = cartRepo.findById(uuid);
        System.out.println("cartItems in service" + cartItem);
        // Check if the wishlist item with the given UUID exists
        if (cartItem.isPresent()) {
            System.out.println("cartItems in service ifff/////" + cartItem);
            Product productInfo = cartItem.get().getProductInfo();
            return Optional.ofNullable(productInfo);
        } else {
            return Optional.empty();
        }
    }

    public void deleteAll(UUID uuid) {
        cartRepo.deleteById(uuid);
    }
}

























































