package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.ProductRepository;
import com.LUXURYCLIQ.Repository.UserRepository;
import com.LUXURYCLIQ.Service.CategoryService;
import com.LUXURYCLIQ.Service.ImageService;
import com.LUXURYCLIQ.Service.ProductService;
import com.LUXURYCLIQ.Service.UserService;
import com.LUXURYCLIQ.SuccessHandler.UsernameProvider;
import com.LUXURYCLIQ.entity.Category;
import com.LUXURYCLIQ.entity.Image;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ProductController {


    @Autowired
    ProductService productService;

    @Autowired
    ImageService imageService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    UsernameProvider usernameProvider;

    @Autowired
    ProductRepository productRepository;

    String uploadDir = "C:\\Users\\HP\\Downloads\\LUXURYCLIQ (3)\\LUXURYCLIQ\\src\\main\\resources\\Static\\uploads";



    @GetMapping("/product")

    @PreAuthorize("hasRole('ROLE_ADMIN')") public String all

            (@RequestParam(required = false,defaultValue = "") String filter,

             @RequestParam(required = false, defaultValue = "") String keyword,

             @RequestParam(required = false, defaultValue = "ASC") String sort,

             @RequestParam(required = false, defaultValue = "name") String field,

             @RequestParam(required = false, defaultValue = "0") int page,

             @RequestParam(required = false, defaultValue = "100") int size, Model model){

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sort), field));

        Page<Product> products = Page.empty();

        if(!filter.equals("")){
            products = productService.findByCategory(filter, pageable);
        } else if (!keyword.equals("")) {
            products = productService.findByNameLikePaged(keyword, pageable);
        } else{
            products = productService.findAllPaged(pageable); }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products",products);

        //Pagination Values model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword); model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("field", field); model.addAttribute("sort", sort);
        model.addAttribute("pageSize", size); int startPage = Math.max(0, page - 1);
        int endPage = Math.min(page + 1, products.getTotalPages() - 1);
        model.addAttribute("startPage", startPage); model.addAttribute("endPage",endPage);

        model.addAttribute("empty", products.getTotalElements() == 0);
        return "product/ProductList";
    }



//	@GetMapping("/product")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public String product(@RequestParam(required = false, defaultValue = "") String filter,
//	                  @RequestParam(required = false, defaultValue = "") String keyword,
//	                  @RequestParam(required = false, defaultValue = "ASC") String sort,
//	                  @RequestParam(required = false, defaultValue = "name") String field,
//	                  Model model) {
//
//	    List<Product> products;
//
//	    if (!filter.equals("")) {
//	        products = productService.findByCategory(filter);
//	    } else if (!keyword.equals("")) {
//	        products = productService.findByName(keyword);
//	    } else {
//	        products = productService.findAll();
//	    }
//
//	    model.addAttribute("categories", categoryService.findAll());
//	    model.addAttribute("products", products);
//
//	    // Other attributes you might want to add
//	    model.addAttribute("filter", filter);
//	    model.addAttribute("keyword", keyword);
//	    model.addAttribute("field", field);
//	    model.addAttribute("sort", sort);
//	    model.addAttribute("empty", products.isEmpty());
//
//	    return "product/ProductList";
//	}



    @GetMapping("/product/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewProduct(@PathVariable UUID uuid, Model model){
        Optional<Product> product = productService.getProduct(uuid);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "product/ProductDetailView";
    }




    @GetMapping("/CreateProduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createProduct(Model model) throws JsonProcessingException {

        Product product = new Product();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);

        return "product/CreateProduct";
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveProduct(
            @RequestParam("images") List<MultipartFile> imageFiles,
            @RequestParam("name") String name,
            @RequestParam("categoryUuid") String categoryUuid,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") BigDecimal price) throws IOException {

        Product product = new Product();
        product.setName(name);
        System.out.println("kjvsdkj");
        Category category=categoryService.getCategory(UUID.fromString(categoryUuid));
        product.setCategory(category);
        product.setDescription(description);
        System.out.println("ksjbkjvsdbv");

      product = productService.save(product);
        product.setPrice(price);


        List<Image> images = new ArrayList<>();

            for (MultipartFile image : imageFiles) {

                Image image1=new Image();
                String imageId=image.getOriginalFilename();
                Path filenameAndPath=Paths.get(uploadDir,imageId);
                Files.write(filenameAndPath,image.getBytes());


                image1.setFileName(imageId);

                image1.setProduct(product);
                images.add(image1);


                System.out.println(image1.getProduct().getName());
                System.out.println("jsdbfj");


                System.out.println("hello");

//                String fileLocation = handleFileUpload(image); // Save the image and get its file location
////                Image imageEntity = new Image(fileLocation,product); // Create an Image entity with the file location
//                imageEntity = imageService.save(imageEntity);
//                images.add(imageEntity); // Add the Image entity to the Product's list of images
            }
            product.setImages(images);
            product.setQuantity(quantity);
           productRepository.save(product);

//        if(!imageFiles.get(0).getOriginalFilename().equals("")){
//            product.setImages(images);
//        }

        System.out.println("kjabskjbvg");


        return "redirect:/product";
//			 return product.getUuid();
    }

    private String handleFileUpload(MultipartFile file) throws IOException {
//        String uploadDir = "C:\\Users\\HP\\Downloads\\LUXURYCLIQ (3)\\LUXURYCLIQ\\src\\main\\resources\\Static\\uploads";
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdirs();
        }



        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        System.out.print("file name: "+fileName);
        Path path = Paths.get(filePath);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/editproduct/{uuid}")

    public String editProduct(@PathVariable UUID uuid, Model model) {

        Product product = productService.getProduct(uuid).orElseThrow();
        List<Category> category=categoryService.findAll();
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories",category);
            return "product/editproduct"; // Return the HTML template for editing
        } else {
            // Handle the case when the user is not found
            return "redirect:/product"; // Redirect to a user listing page
        }
    }

    // Handle POST request to update user details
    @PostMapping("/pro/edit")
    public String updateProduct(@ModelAttribute Product updatedProduct) {
        System.out.println(updatedProduct);
        // Validate and update the user details in your service
        productService.updateProduct(updatedProduct);

        // Redirect to a user listing page or a success page
        return "redirect:/product";
    }














//    @PostMapping("/editproduct")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String updateProduct(@RequestParam("productUuid") UUID productUuid,
//                                @RequestParam("name") String name,
//                                @RequestParam("categoryUuid") UUID categoryUuid,
//                                @RequestParam("description") String description,
//                                @RequestParam("price") BigDecimal price,
//                                @RequestParam("newImages") List<MultipartFile> newImages,
//                                @RequestParam("imagesToRemove") String[] imagesToRemove
//    ) throws IOException {
//
//        for(String image : imagesToRemove){
//            if(!image.equals("")){
//                Image deletedImage = imageService.findFileNameById(UUID.fromString(image));
//                handleDelete(deletedImage.getFileName());
//                imageService.delete(UUID.fromString(image));
//            }
//        }
//        Product updatedProduct = new Product();
//        updatedProduct.setUuid(productUuid);
//        updatedProduct.setName(name);
//        updatedProduct.setCategory(categoryService.getCategory(categoryUuid));
//        updatedProduct.setDescription(description);
//        updatedProduct.setPrice(price);
//        productService.save(updatedProduct);
//        //save new images
//        for (MultipartFile image : newImages) {
//            if(image.getOriginalFilename()!="")  {
//                String fileLocation = handleFileUpload(image); // Save the image and get its file location
////                Image imageEntity = new Image(fileLocation, productService.getProduct(productUuid)); // Create an Image entity with the file location
////                imageService.save(imageEntity);
//            }
//        }
//        return "redirect:/product";
//
//    }

    private void handleDelete(String fileName) throws IOException {
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads";

        // Get the file path
        String filePath = uploadDir + "/" + fileName;

        // Create a file object for the file to be deleted
        File file = new File(filePath);

        // Check if the file exists
        if (file.exists()) {
            // Delete the file
            file.delete();
            System.out.println("File deleted successfully!");
        } else {
            System.out.println("File not found!");
        }
    }


//    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile imageFile,
//                                              @RequestPart("Uuid") String Uuid) throws IOException {
//
//        Product product = productService.getProduct(UUID.fromString(Uuid));
//        Image image = new Image();
//        image.setProduct(product);
//        image.setFileName(handleFileUpload(imageFile));
//        image = imageService.save(image);
//
//        return ResponseEntity.ok("Image uploaded successfully");
//    }


//    @GetMapping("/delete/{uuid}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String delete(@PathVariable UUID uuid){
//
//        Optional<Product> product = productService.getProduct(uuid);
//        product.setDeleted(true);
//        product.setEnabled(false);
//
//        System.out.println("SOft deleting product "+product.getName());
//        productService.save(product);
//
//        return "redirect:/product";
//    }
//
//        if (product.isPresent()) {
//            Product products = product.get();
//
//
//            products.setDeleted(true);
//            products.setEnabled(false);
//
//            System.out.println("Soft deleting product " + product.getName());
//            productService.save(products);
//        } else {
//
//            System.out.println("Product with UUID " + uuid + " not found.");
//        }
//
//        return "redirect:/product";
//    }




//    @GetMapping("/productDetail")
//    public String productView(@RequestParam(value = "uuid",required = false)String uuid,Model model)
//    {
//
//        Product selectedProduct=new Product();
//        selectedProduct=productService.getProduct(UUID.fromString(uuid));
//        model.addAttribute("product",selectedProduct);
//        return "shop/single-product";
//
//
//    }
//
//
@GetMapping("/products")
public String listProducts(Pageable pageable, Model model) {
    List<Product> productPage = productRepository.findAll(pageable)
            .getContent()  // Assuming pageable returns a Page<Product>
            .stream()
            .filter(product -> !product.isDeleted())
            .collect(Collectors.toList());





    model.addAttribute("productPage", productPage);

    return "productlist";
}

     public String getCurrentUsername()
     {

         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         return authentication.getName();
     }


    //Toggle for enable and disable
   /* @GetMapping("/toggleStatus")
    public String toggleStatus(@RequestParam("uuid") String uuid){
        Product product = productService.getProduct(UUID.fromString(uuid));
        System.out.println("inside toggle");
        product.setEnabled(!product.isEnabled());
        productService.save(product);
        return "redirect:/product";
    }
*/


    @GetMapping("/delete/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable() UUID uuid) {
        // Fetch the product by its UUID
        Optional<Product> productOptional = productService.getProduct(uuid);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Perform the soft delete operation
            product.setDeleted(!product.isDeleted());
//            product.setEnabled(false);


            // Save the updated product (soft delete)
            productService.save(product);

            // Redirect to a page or URL of your choice after the delete operation
            return "redirect:/product";
        } else {
            // Product with the given UUID not found
            System.out.println("Product with UUID " + uuid + " not found.");

            return "redirect:/product"; // You can customize this URL
        }
    }


}
