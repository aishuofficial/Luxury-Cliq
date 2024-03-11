package com.LUXURYCLIQ.Controller;


import com.LUXURYCLIQ.Repository.ProductRepository;
import com.LUXURYCLIQ.Service.*;
import com.LUXURYCLIQ.entity.*;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.Order;
import java.security.Principal;
import java.util.*;

@Controller
public class Ordermanagementcontroller {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrdermanagementService ordermanagementService;

    @Autowired
    ProductRepository productRepository;


    @GetMapping("/checkout")
    public String checkout(Model model, @RequestParam(required = false) UUID addressUUID) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        System.out.println("hello" + username);
        List<Cart> cartItems = cartService.findByUserEntity_Usernames(username);

        System.out.println(cartItems);


        User user = userService.getUserdata(username).orElseThrow();
        List<Address> userAddresses = user.getSaveAddAddress();

        // Pass user addresses to the checkout page
        model.addAttribute("userAddresses", userAddresses);
        model.addAttribute("cartItems", cartItems);
        return "order";
    }


    @PostMapping("/place-order")
    public String placeOrder(
            @RequestParam(name = "addressId") UUID addressId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        List<Cart> cartItems = cartService.findByUserEntity_Usernames(username);
        System.out.println("bye" + addressId);
        ordermanagementService.processOrder(addressId, cartItems);


        return "orderconfirmed";
    }


//     @GetMapping("/odersummary")
//    public String orderSummaryPage(@PathVariable UUID uuid,Model model)
//     {
//        List <Ordermanagement> order = ordermanagementService.getAllOrders(uuid);
//
//         System.out.println(order+"oops");
//         if (order == null)
//         {
//             return "Base";
//         }
//
//         model.addAttribute("order", order);
//         return "redirect:/ordersummarypage";
//     }
//     }



    @GetMapping("/myorder")
    public String myorder(Model model,Principal principal) {
        System.out.println("haiiiiiiiiiiiiiiiiii");
        String username=principal.getName();
        List<Ordermanagement> userOrderList=ordermanagementService.getOrderByUsername(username);
        System.out.println(username);
        model.addAttribute("userOrderList",userOrderList);
    return "ordersuccesspage";
}


//    @PostMapping("/cancelorder")
//    public String cancelOrder(@RequestParam("uuid") UUID uuid,
//                              RedirectAttributes redirectAttributes) {
//        try {
//
//
//            Ordermanagement order = ordermanagementService.findById(uuid);
//            if (order != null && order.getOrderStatus() != OrderStatus.CANCELLED) {
//                order.setOrderStatus(OrderStatus.CANCELLED);
//
//                ordermanagementService.save(order);
//            }
//            List<Ordermanagement> orderItems=order.getItems();
//
//            for ( ordermanagementService : orderItems) {
//                Product product = orderItem.getProductId();
//                int newStock = product.getQuantity() + orderItem.getQuantity();
//                product.setStock(newStock);
//                productService.save(product);
//            }
//
//
//
//            return "redirect:/orders";
//        } catch (Exception e) {
//            return"redirect:/orders";
//        }
//    }


//    @PostMapping("/cancelorder")
//    public ResponseEntity<String> cancelOrder(@PathVariable UUID uuid) {
//        try {
//            Optional<Order> optionalOrder = ordermanagementService.findByUuid(uuid);
//
//            if (optionalOrder.isEmpty()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            Order order = optionalOrder.get();
//
//            // Check if the order is not already cancelled
//            if (!order.getStatus().equals("Cancelled")) {
//                // Update the order status to "Cancelled"
//                order.setStatus("Cancelled");
//                ordermanagementService.save(order);
//
//                // Retrieve the product associated with the order
//                Product product = order.getProduct();
//
//                // Increase the product quantity
//                int newQuantity = product.getQuantity() + order.getQuantity();
//                product.setQuantity(newQuantity);
//                productRepository.save(product);
//            }
//
//            return ResponseEntity.ok("Order cancelled successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error cancelling order.");
//        }
//    }
}


