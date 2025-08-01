package com.example.recipeapp.controller.api;

import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API REST pour les recettes
 */
@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeApiController {

    @Autowired
    private RecipeService recipeService;

    /**
     * Récupère toutes les recettes publiées
     */
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        try {
            List<Recipe> recipes = recipeService.findAllPublished();
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère une recette par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        try {
            Optional<Recipe> recipe = recipeService.findByIdAndIncrementViews(id);
            return recipe.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crée une nouvelle recette
     */
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        try {
            Recipe createdRecipe = recipeService.createRecipe(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour une recette existante
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {
        try {
            recipe.setId(id);
            Recipe updatedRecipe = recipeService.updateRecipe(recipe);
            return ResponseEntity.ok(updatedRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprime une recette
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        try {
            boolean deleted = recipeService.deleteRecipe(id);
            return deleted ? ResponseEntity.noContent().build() 
                          : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recherche de recettes par titre
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String q) {
        try {
            List<Recipe> recipes = recipeService.searchByTitle(q);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les recettes par catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Recipe>> getRecipesByCategory(@PathVariable String categoryId) {
        try {
            List<Recipe> recipes = recipeService.findByCategoryId(categoryId);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les recettes par auteur
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Recipe>> getRecipesByAuthor(@PathVariable String authorId) {
        try {
            List<Recipe> recipes = recipeService.findPublishedByAuthorId(authorId);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les recettes par difficulté
     */
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Recipe>> getRecipesByDifficulty(@PathVariable String difficulty) {
        try {
            Recipe.DifficultyLevel level = Recipe.DifficultyLevel.valueOf(difficulty.toUpperCase());
            List<Recipe> recipes = recipeService.findByDifficulty(level);
            return ResponseEntity.ok(recipes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les recettes les mieux notées
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<Recipe>> getTopRatedRecipes(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Recipe> recipes = recipeService.findTopRated(limit);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les recettes les plus récentes
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Recipe>> getLatestRecipes(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Recipe> recipes = recipeService.findLatest(limit);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Note une recette
     */
    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> rateRecipe(@PathVariable String id, @RequestParam double rating) {
        try {
            if (rating < 0 || rating > 5) {
                return ResponseEntity.badRequest().build();
            }
            recipeService.rateRecipe(id, rating);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour le statut de publication d'une recette
     */
    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> updatePublishStatus(@PathVariable String id, @RequestParam boolean published) {
        try {
            recipeService.updatePublishedStatus(id, published);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les statistiques des recettes
     */
    @GetMapping("/stats")
    public ResponseEntity<RecipeService.RecipeStats> getRecipeStats() {
        try {
            RecipeService.RecipeStats stats = recipeService.getGlobalStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
