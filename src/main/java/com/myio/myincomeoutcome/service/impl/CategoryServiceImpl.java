package com.myio.myincomeoutcome.service.impl;


import com.myio.myincomeoutcome.entity.Category;
import com.myio.myincomeoutcome.repository.CategoryRepository;
import com.myio.myincomeoutcome.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public Category getOrSave(Category category){
//        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        if (!optionalCategory.isEmpty()) {
            return optionalCategory.get();
        }

        return categoryRepository.save(category);

    }
}
