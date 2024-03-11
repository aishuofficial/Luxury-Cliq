package com.LUXURYCLIQ.Service;

import com.LUXURYCLIQ.Repository.CategoryRepository;
import com.LUXURYCLIQ.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService
{
    @Autowired
    CategoryRepository categoryRepository;

    public void addCategory(Category category) {

        categoryRepository.save(category);
    }

    public void delete(UUID uuid) {

        categoryRepository.deleteById(uuid);
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }


    public void save(Category category) {

        categoryRepository.save(category);
    }

    public List<Category> findAll() {

        return categoryRepository.findAll();
    }

    public Category getCategory(UUID uuid) {

        return categoryRepository.findById(uuid).orElse(null);
    }
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}


