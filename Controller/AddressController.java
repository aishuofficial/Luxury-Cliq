package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.AddressRepository;
import com.LUXURYCLIQ.Service.AddressService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.entity.Address;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/address")
public class AddressController {


    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserService userService;





    @GetMapping("/createaddress")
    public String addressCreating(Model model)
    {
        model.addAttribute("address", new Address());
        return "addaddress";
    }

     @PostMapping("/createaddress")
    public String createAddressofUser(@ModelAttribute("address") Address address,String Username)
     {
         String username= SecurityContextHolder.getContext().getAuthentication().getName();
         System.out.println(username);
         System.out.println("hi"+address);


         User user = userService.getUserdata(username).orElseThrow();
         if (user == null) {
             return "redirect:/login" ;
         }

         addressService.createAddress(user, address);
         return "redirect:/profile/viewprofile/";
     }
     }

