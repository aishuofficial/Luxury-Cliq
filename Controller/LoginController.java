package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Service.CategoryService;
import com.LUXURYCLIQ.Service.ProductService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.Service.otpservice;
import com.LUXURYCLIQ.entity.Category;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
public class LoginController {
    /*@GetMapping("/login")
    public String showLoginForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {


            model.addAttribute("signup", false);

            return "login";

        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(){

        return "index";
    }*/

    @Autowired
    UserService userInfoService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    otpservice otpService;

    public String getCurrentUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/")
    public String getHomePage(@AuthenticationPrincipal(expression = "username") Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // Print the user's roles
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("User Role: " + authority.getAuthority());
        });

        System.out.println("admin here 1 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+isAdmin);


        if (isAdmin) {
           System.out.println("admin here 2 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
          return "redirect:/adminpanel";
        }


        String currentUsername = String.valueOf(getCurrentUsername());
        User userInfo = userInfoService.findByUsername(currentUsername);


        List<Product> products= productService.findAll();
        List<Category> categories=categoryService.findAll();

        if (!isVerified(currentUsername)) {
            String email = userInfo.getEmail();
            model.addAttribute("email", email);

            //send otp
            otpService.sendOtp(email);

            return "verification";

        }else {

            model.addAttribute("categories",categories);
            model.addAttribute("products", products);

//            return "agency/index";
            return "Base";
        }
    }

    private boolean isVerified(String username) {
        User user = userInfoService.findByUsername(username);
        if (user != null) {
            return user.isVerified();
        } else {
            return true;
        }
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken ) {

//            model.addAttribute("signedUp", false);

            System.out.println("Login check for error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            return "login";

        }
        System.out.println("Login success >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        return "redirect:/";
    }

    @PostMapping("/verify")
    public String verifyUser(@RequestParam(value = "otp") String otp,
                             @RequestParam(value = "email") String email,
                             Model model){

        System.out.println("inside post verify");

        boolean verified =  otpService.verifyOtp(email,otp);
        if(verified){
            return "redirect:/";
        }else {
            model.addAttribute("failed", true);
            return "verification";

        }
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otpNumber = 100_000 + random.nextInt(900_000);
        return String.valueOf(otpNumber);
    }

}









