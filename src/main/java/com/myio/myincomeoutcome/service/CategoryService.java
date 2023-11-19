package com.myio.myincomeoutcome.service;

import com.myio.myincomeoutcome.entity.Category;

public interface CategoryService {
    Category getOrSave(Category category);
}
