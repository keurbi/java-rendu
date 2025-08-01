package com.example.recipeapp.controller;

import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.service.RecipeService;
import com.example.recipeapp.service.CategoryService;
import com.example.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controller pour les pages de recettes
 */
@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    /**
     * Détail d'une recette
     */
    @GetMapping("/{id}")
    public String recipeDetail(@PathVariable String id, Model model) {
        try {
            Optional<Recipe> recipe = recipeService.findByIdAndIncrementViews(id);
            
            if (recipe.isPresent()) {
                model.addAttribute("recipe", recipe.get());
                
                // Charger les informations de la catégorie
                categoryService.findById(recipe.get().getCategoryId())
                    .ifPresent(category -> model.addAttribute("category", category));
                
                // Charger les informations de l'auteur
                userService.findById(recipe.get().getAuthorId())
                    .ifPresent(author -> model.addAttribute("author", author));
                
                // Charger des recettes similaires (même catégorie)
                model.addAttribute("similarRecipes", 
                    recipeService.findByCategoryId(recipe.get().getCategoryId())
                        .stream()
                        .filter(r -> !r.getId().equals(id))
                        .limit(3)
                        .toList());
                
                return "recipes/detail";
            } else {
                model.addAttribute("error", "Recette non trouvée");
                return "error/404";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de la recette: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * Recettes par catégorie
     */
    @GetMapping("/category/{categoryId}")
    public String recipesByCategory(@PathVariable String categoryId, Model model) {
        try {
            model.addAttribute("recipes", recipeService.findByCategoryId(categoryId));
            
            categoryService.findById(categoryId)
                .ifPresent(category -> {
                    model.addAttribute("category", category);
                    model.addAttribute("title", "Recettes - " + category.getName());
                });
            
            return "recipes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
            return "recipes/list";
        }
    }

    /**
     * Recettes par auteur
     */
    @GetMapping("/author/{authorId}")
    public String recipesByAuthor(@PathVariable String authorId, Model model) {
        try {
            model.addAttribute("recipes", recipeService.findPublishedByAuthorId(authorId));
            
            userService.findById(authorId)
                .ifPresent(author -> {
                    model.addAttribute("author", author);
                    model.addAttribute("title", "Recettes de " + author.getFullName());
                });
            
            return "recipes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
            return "recipes/list";
        }
    }

    /**
     * Recherche de recettes
     */
    @GetMapping("/search")
    public String searchRecipes(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("title", "Recherche de recettes");
        model.addAttribute("searchQuery", q);
        
        if (q != null && !q.trim().isEmpty()) {
            try {
                model.addAttribute("recipes", recipeService.searchByTitle(q));
            } catch (Exception e) {
                model.addAttribute("error", "Erreur lors de la recherche: " + e.getMessage());
            }
        }
        
        return "recipes/search";
    }

    /**
     * Recettes par difficulté
     */
    @GetMapping("/difficulty/{difficulty}")
    public String recipesByDifficulty(@PathVariable String difficulty, Model model) {
        try {
            Recipe.DifficultyLevel level = Recipe.DifficultyLevel.valueOf(difficulty.toUpperCase());
            model.addAttribute("recipes", recipeService.findByDifficulty(level));
            model.addAttribute("title", "Recettes " + level.getDisplayName());
            model.addAttribute("difficulty", level);
            
            return "recipes/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Niveau de difficulté non reconnu");
            return "recipes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
            return "recipes/list";
        }
    }

    /**
     * Top des recettes les mieux notées
     */
    @GetMapping("/top-rated")
    public String topRatedRecipes(Model model) {
        try {
            model.addAttribute("recipes", recipeService.findTopRated(20));
            model.addAttribute("title", "Top des recettes les mieux notées");
            return "recipes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
            return "recipes/list";
        }
    }

    /**
     * Recettes les plus récentes
     */
    @GetMapping("/latest")
    public String latestRecipes(Model model) {
        try {
            model.addAttribute("recipes", recipeService.findLatest(20));
            model.addAttribute("title", "Recettes les plus récentes");
            return "recipes/list";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
            return "recipes/list";
        }
    }
}
