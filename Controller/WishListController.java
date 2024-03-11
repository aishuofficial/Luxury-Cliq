package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Service.CartService;
import com.LUXURYCLIQ.Service.ProductService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.Service.WishlistService;
import com.LUXURYCLIQ.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("wishlist")
public class WishListController {
    @Autowired
    WishlistService wishlistService;

//    @Autowired
//    ImageService imageService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    //    @Autowired
//    WishListService wishListService;
    @PostMapping("/showProductByCategory/{uuid}")
    public String wishlistView(@PathVariable UUID uuid , Model model) {
        System.out.println("wishlist view.........................................................................."+uuid);
        Optional<Category> wishlistView= wishlistService.getCategoryDetails(uuid);
//        List<Image>images= imageService.findAllImage();
        System.out.println("wishlist>>>>>>>>>>>>>>>>>>>>>>>>"+wishlistView);
        model.addAttribute("wishlist",wishlistView);
//        model.addAttribute("images",images);
        return "shop/wishlist";

    }

    @GetMapping("/addTowishlist/{uuid}")
    public String addToWishlist(@PathVariable UUID uuid)
                           /* @AuthenticationPrincipal(expression = "username")String username)*/
    {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("product>>>>>>>>>>>>>>>>>"+uuid);
        Product productInfo = productService.getProduct(uuid).orElse(null);
        User user=userService.findByUsernames(username);
        System.out.println("username"+user);
        if (productInfo != null) {
            Wishlist wishlistItem = new Wishlist();
            wishlistItem.setProductInfo(productInfo);
            wishlistItem.setUserEntity(user);
            /*wishlistItem.setQuantity(1);*/
            System.out.println("cartlist>>>>>>"+wishlistItem);
            wishlistService.saveToWishlist(wishlistItem);
        }
        return "redirect:/wishlist/viewwishlist";
    }

    @GetMapping("/viewwishlist")
    public String viewWishlist(Model model/*@AuthenticationPrincipal(expression = "username")String username*/) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        List<Wishlist> wishlistItems = wishlistService.findByUserEntity_Usernames(username);
        System.out.println("view wishlist "+username);

        System.out.println("cart after delete[[[[[" + wishlistItems);
        Optional<User> user = userService.getUserdata(username);
        System.out.println("user/////"+user);
      /*  model.addAttribute("total", wishlistService.findTotal(wishlistItems));*/
        model.addAttribute("wishlistItems", wishlistItems);
        model.addAttribute("user", user.orElse(null));
        return "/wishlist";
    }

    /*@Remove Product from Cart or delete product*/
    @GetMapping("/removeFromWishlist/{uuid}")
    public String removeProduct(@PathVariable UUID uuid)
    {
        wishlistService.deleteProduct(uuid);
        System.out.println("product uuid///////"+uuid);

        return "redirect:/wishlist/viewwishlist";
    }


    /*Quantity setting*/
    /*@PostMapping("/setQuantity")
    public String SetQuantity(@RequestParam("quantity") int quantity,
                              @RequestParam("uuid") UUID wishlistId)
    {      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>cartId"+wishlistId);
        Wishlist wishlist = wishlistService.getWishlistById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println(";;;;;;;;;;;;;;;;;;cartId"+wishlist);
        wishlist.setQuantity(quantity);
//        cartInfo.setQuantity(1);
        System.out.println("new wishlist entity..."+wishlist);
        wishlistService.saveToWishlist(wishlist);
        System.out.println("setQuantity>>>>>>>>>>>>>"+quantity);

        return "redirect:/cart/viewCart";
    }*/

}
