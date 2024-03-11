package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Category;
import com.LUXURYCLIQ.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

//    Product save(Product product);

//    List<Product> findAll();

//    public Optional<Product> findById(UUID uuid);

//    void deleteById(UUID uuid);

    Optional<Product> findById(UUID uuid);

    List<Product> findByNameLike(String key);

    Page<Product> findByNameLike(String key, Pageable pageable);

    Page<Product> findByNameContainingAndEnabledIsTrue(String key, Pageable pageable);

    Page<Product> findAllByEnabledTrue(Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);

//    List<Product> getProducts(Product product);

}