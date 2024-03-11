package com.LUXURYCLIQ.Controller;
//
import com.LUXURYCLIQ.Repository.CartRepo;
import com.LUXURYCLIQ.Repository.UserRepository;
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

//import java.util.List;
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/api/cart")
//
//public class CartController {
//
//    @Autowired
//    public  CartService cartService;
//
//    @Autowired
//     public CartRepo cartRepository;
//
//    @Autowired
//    public ProductService productService;
//
//    @Autowired
//    public CartController(CartService cartService) {
//        this.cartService = cartService;
//    }
//
//    @Autowired
//    public UserRepository userRepository;
//
//    @Autowired
//    public UserService userService;
//
//
//
//
//    @GetMapping("/show")
//    public String getAllItems(Model model){
////    { System.out.println("cartList");
//        List<Cart> cart=cartService.findAll();
//        System.out.println("cartList"+cart);
//        model.addAttribute("cart",cart);
//        return "/cart";
//    }

  /*  @PostMapping("/add/{productId}")

    public String addToCart(@PathVariable UUID productId, @RequestParam("quantity") int quantity,
                            @AuthenticationPrincipal(expression = "username") String username) {
        Optional<Product> product = productService.getProduct(productId);
        if (product != null) {
            cartService.addToCart(product, quantity,username);
        }

        return "redirect:/cart/show";
    }*/

  /*new method*/
//
//    @GetMapping("/addToCart/{uuid}")
//    public String addToCart(@PathVariable UUID uuid,
//                            @AuthenticationPrincipal(expression = "username") String username) {
//        Product productInfo = productService.getProduct(uuid).orElse(null);
//        User user=userService.findByUsername(username);
//        if (productInfo != null) {
//            Cart cartItem = new Cart();
//            cartItem.setProduct(productInfo);
//            cartItem.setUser(user);
//            cartItem.setQuantity(1);
//            System.out.println("cartlist>>>>>>"+cartItem);
//            cartService.saveCartItem(cartItem);
//        }
//        return "redirect:/cart/viewCart";
//    }
//
//
//    @PostMapping("/update/{cartItemId}")
//    public String updateCart(@PathVariable UUID cartItemId, @RequestParam("quantity") int quantity) {
//        cartService.updateCartItemQuantity(cartItemId, quantity);
//        return "redirect:/cart/show";
//    }
//
//
//    @GetMapping("/remove/{cartItemId}")
//    public String removeCartItem(@PathVariable UUID cartItemId) {
//        cartService.removeCartItem(cartItemId);
//        return "redirect:/cart/show";
//    }
//}

/*new cart controller*/

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

//    @Autowired
//    ImageService imageService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    WishlistService wishListService;
    @PostMapping("/showProductByCategory/{uuid}")
    public String cartView(@PathVariable UUID uuid , Model model) {
        System.out.println("cart view.........................................................................."+uuid);
        Optional<Category> cartInfo= cartService.getCategoryDetails(uuid);
//        List<Image>images= imageService.findAllImage();
        System.out.println("cartInfo>>>>>>>>>>>>>>>>>>>>>>>>"+cartInfo);
        model.addAttribute("cartInfo",cartInfo);
//        model.addAttribute("images",images);
        return "shop/cart";

    }

    /* @get Show cart */
//    @GetMapping("/addToCart/{uuid}")
//    public String showSingleProduct(@PathVariable UUID uuid,Model model) {
////        System.out.println("Single product ID:cart]]]] " + uuid);
//
////        cartService.saveToCart(uuid);
//        Optional<ProductInfo>productInfo=productService.getProduct(uuid);
////       System.out.println("Single product ID: >>>>>>" + productInfo);
//
////        List<Image>images=imageService.findAllImage();
////        model.addAttribute("images",images);
//        model.addAttribute("productInfo",productInfo.orElse(null));
//
////        System.out.println("Image ID: -----------------" + images);
////        List<Cart> cartInfo=cartService.getCartInfo();
////        System.out.println("Cart info======================================="+cartInfo);
////        model.addAttribute("images",images);
////        model.addAttribute("cartInfo",cartInfo);
////        model.addAttribute("productInfo",productInfo.orElse(null));
//        return "shop/cart";
//    }



    @GetMapping("/addToCart/{uuid}")
    public String addToCart(@PathVariable UUID uuid)
    {
    String username= SecurityContextHolder.getContext().getAuthentication().getName();
                           /* @AuthenticationPrincipal(expression = "username")String username) */

        System.out.println("product>>>>>>>>>>>>>>>>>"+uuid);
        Product productInfo = productService.getProduct(uuid).orElse(null);
        User user=userService.findByUsernames(username);
        System.out.println("username"+user);
        if (productInfo != null) {
            Cart cartItem = new Cart();
            cartItem.setProductInfo(productInfo);
            cartItem.setUserEntity(user);
            cartItem.setQuantity(1);
            System.out.println("cartlist>>>>>>"+cartItem);
            cartService.saveToCart(cartItem);
        }
        return "redirect:/cart/viewCart";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model,@AuthenticationPrincipal(expression = "username")String username) {
        List<Cart> cartItems = cartService.findByUserEntity_Usernames(username);
        System.out.println("view cart "+username);

            System.out.println("cart after delete[[[[[" + cartItems);
            Optional<User> user = userService.getUserdata(username);
            System.out.println("user/////"+user);
            model.addAttribute("total", cartService.findTotal(cartItems));
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user.orElse(null));
        return "/cart";
    }

    /*@Remove Product from Cart or delete product*/
    @GetMapping("/removeFromCart/{uuid}")
    public String removeProduct(@PathVariable UUID uuid)
    {
        cartService.deleteProduct(uuid);
        System.out.println("product uuid///////"+uuid);

        return "redirect:/cart/viewCart";
    }


    /*Quantity setting*/
    @PostMapping("/setQuantity")
    public String SetQuantity(@RequestParam("quantity") int quantity,
                              @RequestParam("uuid") UUID cartId)
    {      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>cartId"+cartId);
        Cart cartInfo = cartService.getCartById(cartId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println(";;;;;;;;;;;;;;;;;;cartId"+cartInfo);
        cartInfo.setQuantity(quantity);
//        cartInfo.setQuantity(1);
        System.out.println("new cart entity..."+cartInfo);
        cartService.saveToCart(cartInfo);
        System.out.println("setQuantity>>>>>>>>>>>>>"+quantity);

        return "redirect:/cart/viewCart";
    }

    /*addToWishlistRemoveFromCart*/
    @GetMapping("/addToWishlistRemoveFromCart/{uuid}")
    public String addRemove(@PathVariable UUID uuid,
                            @AuthenticationPrincipal(expression = "username") String username) {
        Product productInfo = cartService.getProduct(uuid).orElse(null);
        System.out.println("product info''''''" + productInfo);
        if (productInfo != null) {
            Wishlist wishList = new Wishlist();
            wishList.setProductInfo(productInfo);
            wishList.setUserEntity(userService.getUserdata(username).orElse(null));

            wishListService.saveToWishlist(wishList);
            cartService.deleteProduct(uuid);
            System.out.println("Product with UUID " + uuid + " moved to wishlist and removed from cart.");
        } else {

            System.out.println("Product with UUID " + uuid + " not found.");
        }


            return "redirect:/cart/viewCart";
        }


    }




























































    /*@Autowired
    public CartController(ProductRepository productRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }
*/

   /* @GetMapping("/show")
    public String getAllItems(Model model) {
        List<Cart> cart = cartService.findAll();
        model.addAttribute("cart", cart);
        return "cart";


    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartItemById(@PathVariable UUID id) {
        Optional<Cart> cartItem = cartService.getCartItemById(id);
        if (cartItem.isPresent()) {
            return new ResponseEntity<>(cartItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

       /* @PostMapping
        public ResponseEntity<Cart> saveCartItem(@RequestBody Cart cartItem) {
            Cart savedCartItem = cartService.saveCartItem(cartItem);
            return new ResponseEntity<>(savedCartItem, HttpStatus.CREATED);
        }*/

   /* @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable UUID productId) {
        // Add logic to add a product to the cart
        Product product = productRepository.findById(productId).orElseThrow();
        // You can implement logic to handle quantity and duplicate items as needed
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1); // Example: Set the quantity to 1 for simplicity
        cartItemRepository.save(cartItem);
        return "redirect:/cart/";
    }*/

  /*  @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable UUID uuid, @RequestParam("quantity") int quantity) {
        // Add logic to add a product to the cart
        Product product = ProductRepository.findById(uuid).orElseThrow();

        // Validate the quantity (e.g., ensure it's greater than 0)
        if (quantity <= 0) {
            // Handle invalid quantity input (you can redirect to an error page or show a message)
            return "redirect:/error";
        }

        // Check if a cart item with the same product already exists in the cart
        Cart existingCartItem = CartRepository.findByProduct(product);

        if (existingCartItem != null) {
            // If the item already exists in the cart, update its quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            CartRepository.save(existingCartItem);
        } else {
            // Otherwise, create a new cart item
            Cart cartItem = new Cart();

            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            CartRepository.save(cartItem);
        }

        return "redirect:/cart/";
    }

*/

    /*@DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCartItem(@PathVariable UUID id) {
            cartService.deleteCartItem(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }*/





