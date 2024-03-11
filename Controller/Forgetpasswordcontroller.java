package com.LUXURYCLIQ.Controller;


import com.LUXURYCLIQ.Service.UserNotFoundException;
import com.LUXURYCLIQ.Service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Controller
public class Forgetpasswordcontroller {

    @Autowired
    UserService userService;

    @GetMapping("/forgetpassword")
    public String showForgetPasswordForm(Model model)
    {
        model.addAttribute("pageTitle","Forget Password");
        return "forgetPassword";

    }

    @PostMapping("/forgetpassword")
    public String processForgetPasswordForm(HttpServletRequest request,Model model)
    {
        String email=request.getParameter("email");
        String token= RandomString.make(6);
        try
        {
            userService.updateForgetPassword(token,email);

        }

        catch(UsernameNotFoundException ex)
        {
            model.addAttribute("error",ex.getMessage());

        }
        return "forgetpassword";
    }
}
