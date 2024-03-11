package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.ProductRepository;
import com.LUXURYCLIQ.entity.Image;
import com.LUXURYCLIQ.entity.Product;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ImageService imageService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryService categoryService;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAllByEnabledTrue(pageable);
    }

/*
    public Product findProducts(UUID ProductId) { return productRepository.getProduct(ProductId);}
*/

//    public List <Product> findAll(UUID uuid) {
//
//        return productRepository.findById(uuid);
//    }

    //cannot delete a product because it has dependencies;
    public void delete(UUID uuid) {
        List<Image> images = imageService.findImagesByProductId(uuid);
        if(!images.isEmpty()){
            for (Image image : images) {
                imageService.delete(image.getUuid());
            }
        }
        productRepository.deleteById(uuid);
    }

//    public List<Product> findByCategory() {
//        return productRepository.findAllByEnabledTrue();
//    }

    public List<Product> findByName(String keyword) {
        return productRepository.findByNameLike("%" + keyword + "%");
    }

    public Page<Product> findByCategory(String filter, Pageable pageable) {
        try {
            UUID uuid = UUID.fromString(filter); // Check if the string is a valid UUID
            return productRepository.findByCategory(categoryService.getCategory(uuid), pageable);
        } catch (IllegalArgumentException e) {

            return Page.empty();
        }
    }
    public Page<Product> findByNameLikePaged(String keyword, Pageable pageable) {
        return productRepository.findByNameLike("%"+keyword+"%", pageable);
    }
    public Page<Product> findAllPaged(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProduct(UUID productId) {
        return   productRepository.findById(productId);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);

    }




//     public Page<Product> findByCategory(String filter, Pageable pageable) {
//     return productRepository.findAllByEnabledTrue(pageable); }
//     *
//     * public Page<Product> findByNameLikePaged(String keyword, Pageable pageable) {
//     * return productRepository.findByNameLike("%"+keyword+"%", pageable); }
//     *
//     * public Page<Product> findAllPaged(Pageable pageable) { return
//     * productRepository.findAll(pageable); }
//     */

}

