package com.LUXURYCLIQ.Controller;


import com.LUXURYCLIQ.Repository.ProductRepository;
import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Service.AddressService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.SuccessHandler.UsernameProvider;
import com.LUXURYCLIQ.entity.Address;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.Expression;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UsernameProvider usernameProvider;

    @Autowired
    AddressService addressService;

    @Autowired
    ProductRepository productRepository;
    @GetMapping("/viewprofile")
    public String viewUserProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        if (username == null) {
            return "redirect:/";
        } else {
            User user = userService.getUserdata(username).orElseThrow();
            model.addAttribute("user", user);
        }
        model.addAttribute("username", username);

        System.out.println(username);
        List<Address> address = addressService.getAddressesByUsername(username);
        System.out.println(address);
        model.addAttribute("address", address);


        return "profile";
    }


    @GetMapping("/editaddress/{uuid}")
    public String showEditAddressForm(@PathVariable UUID uuid, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null) {
            return "redirect:/";
        }

        User user = userService.getUserdata(username).orElse(null);

        if (user == null) {
            return "redirect:/";
        }

        Address address = addressService.getAddressById(uuid);

        if (address == null) {
            return "redirect:/profile/viewprofile";

        }

        model.addAttribute("address", address);
//
        return "editaddress";


    }

    @PostMapping("/editaddress")
    public String saveEditedAddress(
            @ModelAttribute("address") Address updatedAddress,
            Model model) {
     UUID uuid =  updatedAddress.getUuid();
        System.out.println("sssssssssssssssssssssssssss"+uuid);
        // Assuming you have validation and security checks here

        Address existingAddress = addressService.getAddressById(uuid);
        System.out.println(existingAddress);
        if (existingAddress != null) {
            // The address with the given UUID exists, update it
            existingAddress.setStreet(updatedAddress.getStreet());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setPostalCode(updatedAddress.getPostalCode());
            existingAddress.setCountry(updatedAddress.getCountry());
            System.out.println("exsisiting address"+existingAddress);
            // Save the updated address
            addressService.saveAddress(existingAddress);
        } else {
            // The address with the given UUID doesn't exist, create a new one
            // You may want to set other properties and validation checks here
            System.out.println("elseeeeeeeeeeee");
            addressService.saveAddress(updatedAddress);
        }

        System.out.println("Address updated successfully");

        // Redirect to a success page or show a confirmation message
        return "redirect:/profile/viewprofile";
    }






    @GetMapping("/deleteAddress/{uuid}")
    public String removeAddress(@PathVariable UUID uuid) {
        Address address= addressService.getAddressById(uuid);
         addressService.deleteAddress (address);

        return "redirect:/profile/viewprofile";

    }


//    @GetMapping("/search")
//    public String searchProductByName(@RequestParam("searchterm") String searchterm , Model model)
//    {
//        List<Product> products= productRepository.findByUsernameLike(pattern);
//        model.addAttribute("products",products);
//        return "redirect:/profile";
//        }
//    }

}







