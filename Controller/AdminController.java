package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.OrdermanagementRepository;
import com.LUXURYCLIQ.Repository.ProductRepository;
import com.LUXURYCLIQ.Repository.RoleRepository;
import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Service.UserRegistrationService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.entity.Ordermanagement;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.Role;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Order;
import javax.validation.Valid;
import java.lang.invoke.StringConcatFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {
    @Autowired
    UserRepository userInfoRepository;

    @Autowired
    UserService userInfoService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRegistrationService userRegistrationService;

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrdermanagementRepository ordermanagementRepository;

    @GetMapping("/adminpanel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAdminPanel(Model model) {
        List<User> users= userInfoRepository.findAll();
        model.addAttribute("users",users);
        return "Admin/user";
    }

    //To view user.
    @GetMapping("/admin/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewUser(@PathVariable UUID uuid, Model model){

        User user = userInfoService.getUser(uuid);

        model.addAttribute("uuid",uuid);
        model.addAttribute("firstName",user.getFirstName());
        model.addAttribute("lastName",user.getLastName());
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("phone",user.getPhone());
        model.addAttribute("role",user.getRole());
        model.addAttribute("enabled",user.isEnabled());

        return "viewUser";

    }
    //view create user ui
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/createUser")
    public String createUser(Model model) {
        User userInfo = new User();
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("bindingResult", new BeanPropertyBindingResult(userInfo, "userInfo"));
        return "admin/CreateUser";
    }

    //Create user from admin dashboard
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createUser")
    public String createNewUserFromAdminDashboard(@ModelAttribute @Valid User userInfo,
                                                  BindingResult bindingResult,
                                                  Model model){
        if(bindingResult.hasErrors()){
            return "admin/CreateUser";
        }

        //finding the uuid of the role selected
        List<Role> roleUuids = roleRepository.findAll();
        for(Role role : roleUuids){
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+role);
            if(role.getRoleName().equals(userInfo.getRole().getRoleName())){
                userInfo.getRole().setUuid(role.getUuid());
            }
        }

        if(!Objects.equals(userRegistrationService.addUser(userInfo), "signupSuccess")){

            switch (userRegistrationService.addUser(userInfo)) {
                case "username" -> {
                    bindingResult.rejectValue("username", "username", "username already taken");
                }
                case "email" -> {
                    bindingResult.rejectValue("email", "email", "email address already taken");
                }
                case "phone" -> {
                    bindingResult.rejectValue("phone", "phone", "phone number already taken");
                }
                default -> {
                    System.out.println("Unusual error found");
                }
            }

            model.addAttribute("userInfo", userInfoService);
            model.addAttribute("bindingResult", bindingResult);
            return "admin/CreateUser";
        }
        else{
            return "redirect:/Admin/user/"+userInfo.getUuid();
        }

    }

    @GetMapping("/users/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUserById(@PathVariable("id") UUID uuid){

        User user = userInfoService.getUser(uuid);

        user.setEnabled(false);


        System.out.println("Soft deleting user"+user.getUsername());
        userInfoService.save(user);

//        userInfoService.delete(uuid);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute User userInfo){
        User user = userInfoService.getUser(userInfo.getUuid());

        user.setEnabled(false);


        System.out.println("Soft deleting user"+user.getUsername());
        userInfoService.save(user);


        return "redirect:/adminpanel";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/edituser/{uuid}")

    public String editUserForm(@PathVariable UUID uuid, Model model) {

        User user = userService.getUser(uuid);

        if (user != null) {
            model.addAttribute("user", user);
            return "edituser"; // Return the HTML template for editing
        } else {
            // Handle the case when the user is not found
            return "redirect:/adminpanel/"; // Redirect to a user listing page
        }
    }

    // Handle POST request to update user details
    @PostMapping("/us/edit")
    public String updateUser(@ModelAttribute User updatedUser) {
        System.out.println(updatedUser);
        // Validate and update the user details in your service
        userService.updateUser(updatedUser);

        // Redirect to a user listing page or a success page
        return "redirect:/adminpanel";
    }
    @GetMapping("/users/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewUser(@PathVariable UUID uuid, Model model,
                           @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "createdAt") String field,
                           @RequestParam(defaultValue = "ASC") String sort){

        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), field));


        User user = userInfoService.getUser(uuid);

        model.addAttribute("uuid",uuid);
        model.addAttribute("firstName",user.getFirstName());
        model.addAttribute("lastName",user.getLastName());
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("phone",user.getPhone());
        model.addAttribute("role",user.getRole());
        model.addAttribute("enabled",user.isEnabled());

        return "admin/UserDetailView";

    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String searchUsersByUsername(@RequestParam ("searchTerm")String searchTerm,Model model) {

        List<User> users= seacrhUsers(searchTerm);
        	if(users.isEmpty()) {
                model.addAttribute("message");
            }else{
                model.addAttribute("message");
            }

        model.addAttribute("users",users);
        return "Admin/user";
    }

    public List<User> seacrhUsers(String searchTerm){
        String pattern = searchTerm +"%";
        return userInfoRepository.findByUsernameLike(pattern);
    }


//    @DeleteMapping("/deleteByOrder/{orderId}")
//    public void deleteStockItemsByOrder(@PathVariable UUID uuid) {
//        Ordermanagement ordermanagement = ordermanagementRepository.findById(uuid)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + uuid));
//
//        Optional<Product> products = productRepository.findById(uuid);
//        productRepository.deleteAll(products);
//
//        ordermanagementRepository.deleteAll();
//    }


    @GetMapping("/toggleUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String toggleUserById(@PathVariable("uuid") UUID uuid) {
        User user = userInfoService.getUser(uuid);

        // Toggle the deleted status
        user.setEnabled(!user.isEnabled());

        // Save the updated category
        userInfoService.save(user);

        return "redirect:/adminpanel";
    }
}



//}




