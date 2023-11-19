package com.myio.myincomeoutcome.repository;

import com.myio.myincomeoutcome.constant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<com.myio.myincomeoutcome.entity.Category, String> {
//    @Query(value = "SELECT * FROM m_category WHERE category_name = :categoryName", nativeQuery = true)
//    Optional<Category> findByName(@Param("categoryName") com.mandiri.mandiritest.constant.Category categoryName);
//
//    @Modifying
//    @Query(value = "INSERT INTO m_category (id, category_name) VALUES (:categoryId, :categoryName)", nativeQuery = true)
//    void saveCategory(@Param("categoryId") String categoryId, @Param("categoryName") String categoryName);

    Optional<com.myio.myincomeoutcome.entity.Category> findByName(Category category);
}
