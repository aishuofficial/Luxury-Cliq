package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
    List<Category> findByNameLike(String pattern);
}






