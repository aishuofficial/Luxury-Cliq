package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.RoleRepository;
import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Service.UserRegistrationService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.entity.Role;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class SignUpController {
    @Autowired
    UserService userInfoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRegistrationService userRegistrationService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userInfoRepository;

    public ResponseEntity<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(username);
    }

    //landing page
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("/signup")
    public String signin(Model model){

        model.addAttribute("signupError", "false");

        return "signup";
    }
//    @GetMapping("/signins")
//    public String signin(Model model){
//
//        model.addAttribute("signupError", "false");
//
//        return "signin";
//    }
///*newwww*/
//    @Controller
//    public class AuthController {
//
//        @GetMapping("/signin")
//        public String showSigninPage() {
//            return "signin"; // Return the name of the template (without the extension)
//        }
//    }
//




    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User userInfo, Model model) {
//        System.out.println("signup2");
        Optional<Role> userRoleOptional = roleRepository.findRoleByName("ROLE_USER");
        Role userRole = userRoleOptional.orElseGet(() -> {
            Role newRole = new Role();
            newRole.setRoleName("ROLE_USER");
            return roleRepository.save(newRole);
        });

        userInfo.setRole(userRole);
//        userInfoRepository.save(userInfo);

        String res = userRegistrationService.addUser(userInfo);

        if (res.equals("signupSuccess")) {
            // Redirect to the login page after successful registration
            model.addAttribute("signup", true);
            return "login";
        }

        switch (res) {
            case "phone":
                model.addAttribute("signupError", "phone");
                break;
            case "email":
                model.addAttribute("signupError", "email");
                break;
            case "username":
                model.addAttribute("signupError", "username");
                break;
            default:
                model.addAttribute("signupError", "");
                break;
        }

        return "signup";
    }

}


