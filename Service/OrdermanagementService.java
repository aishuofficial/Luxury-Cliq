package com.LUXURYCLIQ.Service;


import com.LUXURYCLIQ.Repository.OrdermanagementRepository;
import com.LUXURYCLIQ.entity.Address;
import com.LUXURYCLIQ.entity.Cart;
import com.LUXURYCLIQ.entity.Ordermanagement;
import com.LUXURYCLIQ.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;



@Service
public class OrdermanagementService {
    @Autowired
    AddressService addressService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    OrdermanagementRepository ordermanagementRepository;


    public void processOrder(UUID addressId, List<Cart> cartItems)
    {

        Address address= addressService.getAddressById(addressId);

        for (Cart cartItem : cartItems) {
            System.out.println(cartItem);
            Ordermanagement order = new Ordermanagement();
            order.setAddress(address);
            order.setProduct(cartItem.getProductInfo());
            order.setUser(cartItem.getUserEntity());
            order.setQuantity(cartItem.getQuantity());
           Product product=cartItem.getProductInfo();
           product.setQuantity(product.getQuantity()-cartItem.getQuantity());
           productService.save(product);






             /** place order- setquantity(get-1)- repo.save
             */



            ordermanagementRepository.save(order);
            cartService.deleteAll(cartItem.getUuid());
        }



    }


    public List<Ordermanagement> getAllOrders(UUID uuid) {
        // Replace this with your actual logic to fetch all orders for the specified user
        return ordermanagementRepository.findByUserUuid( uuid);
    }


//    public String getOrderByUserName(String username)
//    {
//        return ordermanagementRepository.findByUserName(username);
//    }

    public List<Ordermanagement> getOrderByUsername(String username) {
        List<Ordermanagement> userOrders=ordermanagementRepository.findByUser_Username(username);
        System.out.println(">>>>>>>>>>>>>>>>>>userOrders"+userOrders);
        return  userOrders;
    }
}
