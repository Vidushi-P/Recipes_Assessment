package com.example.demo.service;

import com.example.demo.model.RecipeEntity;
import com.example.demo.repository.RecipeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Map;

@Component
public class RecipeService implements CommandLineRunner {

    // Inject the repository so we can save to the database
    @Autowired
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the database is empty
        if (recipeRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            
            // Read the file from the resources folder
            InputStream inputStream = getClass().getResourceAsStream("/recipes.json");
            JsonNode allRecipes = mapper.readTree(inputStream);
            
            // Loop through every recipe in the JSON file
            for (JsonNode node : allRecipes) {
                RecipeEntity recipe = new RecipeEntity();
                
                // 1. Set simple text fields
                recipe.setTitle(node.get("title").asText());
                recipe.setCuisine(node.get("cuisine").asText());
                
                // 2. Set Rating (Check for NaN!)
                String ratingText = node.get("rating").asText();
                if (ratingText.equals("NaN") || ratingText.isEmpty()) {
                    recipe.setRating(null);
                } else {
                    recipe.setRating(Double.parseDouble(ratingText));
                }

                // 3. Set Total Time (Check for NaN!)
                String totalTimeText = node.get("total_time").asText();
                if (totalTimeText.equals("NaN") || totalTimeText.isEmpty()) {
                    recipe.setTotalTime(null);
                } else {
                    recipe.setTotalTime(Integer.parseInt(totalTimeText));
                }
                
                // (You would do the exact same simple if/else for prep_time and cook_time)

                // 4. Save the Nutrients as a Map
                Map<String, Object> nutrientsMap = mapper.convertValue(
                    node.get("nutrients"), 
                    new TypeReference<Map<String, Object>>() {}
                );
                recipe.setNutrients(nutrientsMap);

                // 5. Save to database!
                recipeRepository.save(recipe);
            }
            System.out.println("Recipes are saved to the database successfully.");
        }
    }
}