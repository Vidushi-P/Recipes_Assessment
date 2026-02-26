package com.example.demo.repository;

import com.example.demo.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {
    @Query("SELECT r FROM RecipeEntity r WHERE " +
           "(:title IS NULL OR r.title LIKE %:title%) AND " +
           "(:cuisine IS NULL OR r.cuisine = :cuisine) AND " +
           "(:rating IS NULL OR r.rating >= :rating)")
    Page<RecipeEntity> searchRecipes(
            @Param("title") String title, 
            @Param("cuisine") String cuisine, 
            @Param("rating") Double rating, 
            Pageable pageable);
}
