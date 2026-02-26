package com.example.demo.controller;

import com.example.demo.model.RecipeEntity;
import com.example.demo.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Enpoint 1 - get all the recipes paginated
    @GetMapping
    public Page<RecipeEntity> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        // Spring Boot pages start at 0, so we do (page - 1)
        // We also tell it to sort by "rating" in descending order (highest first)
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "rating"));
        
        return recipeRepository.findAll(pageable);
    }

    // Endpoint 2 - searching recipes
    @GetMapping("/search")
    public Page<RecipeEntity> searchRecipes(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String cuisine,
        @RequestParam(required = false) Double rating,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int limit) {

        // Create pagination rule
        Pageable pageable = PageRequest.of(page - 1, limit);

        // Just call the custom query
        return recipeRepository.searchRecipes(title, cuisine, rating, pageable);
    }

}