package com.LUXURYCLIQ.Controller;

import com.LUXURYCLIQ.Repository.CategoryRepository;
import com.LUXURYCLIQ.Service.CategoryService;
import com.LUXURYCLIQ.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllCategory(Model model)
    {
        List<Category> categories=categoryService.findAll();
        model.addAttribute("categories",categories);
        return "Category/CategoryList";
    }


      @GetMapping("/category/create")
      public String createCategory()
      {
          return "CreateCategory";
      }

      @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category)
      {
          System.out.print("inside.");
          categoryService.save(category);
          return "redirect:/category";
      }

    @GetMapping("/category/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCategory(@PathVariable UUID uuid, Model model){
        Category category = categoryService.getCategory(uuid);

        model.addAttribute("uuid",uuid);
        model.addAttribute("name",category.getName());
        model.addAttribute("description",category.getDescription());

        return "Category/CategoryDetailView";
    }

    @GetMapping("/deleteCategory/{uuid}")

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeCategory(@PathVariable("uuid") UUID uuid)
    {
        Category category= categoryService.getCategory(uuid);
        if(!category.isDeleted())
        {
            categoryService.delete(uuid);
            System.out.println("hard deleted");
        }
        return "redirect:/category";
    }


    @GetMapping("/toggleCategory/{uuid}")
    @PreAuthorize("hasRole('Role_Admin)")
    public String toggleCategoryById(@PathVariable("id")UUID uuid)
    {
        Category category=categoryService.getCategory(uuid);
        categoryService.save(category);
        return "redirect:/category";
    }

    @PostMapping("/updateCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute Category category ,RedirectAttributes redirectAttributes){
        if (categoryService.existsByName(category.getName())) {
            redirectAttributes.addFlashAttribute("message", "Category with this name already exists.");
        } else {
            categoryService.updateCategory(category);
            redirectAttributes.addFlashAttribute("message", "Category is updated.");
        }
        return "redirect:/category";

    }
   }






