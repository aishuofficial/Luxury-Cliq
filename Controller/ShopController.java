package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.CategoryRepository;
import com.LUXURYCLIQ.Repository.ProductRepository;
//import com.LUXURYCLIQ.Repository.ShopRepository;
import com.LUXURYCLIQ.Service.ProductService;
import com.LUXURYCLIQ.Service.ShopService;
import com.LUXURYCLIQ.entity.Image;
import com.LUXURYCLIQ.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ShopController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

//    @Autowired
//    ShopRepository shopRepository;

    @Autowired
    ShopService shopService;



    @GetMapping("/productView/{productId}")
    public String productDetailView(@PathVariable UUID productId, Model model) {
        Optional<Product> product=  productService.getProduct(productId);
            Product productImage=product.get();
            List<Image>  images= productImage.getImages();

        System.out.println(images);
               model.addAttribute("images",images);
            model.addAttribute("product",product);


        return "/SingleProduct";
    }






}
