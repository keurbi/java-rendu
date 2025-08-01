package com.example.recipeapp.controller;

import com.example.recipeapp.service.CategoryService;
import com.example.recipeapp.service.RecipeService;
import com.example.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenue sur Recipe App!");
        
        try {
            // Ajouter quelques statistiques pour la page d'accueil
            model.addAttribute("totalRecipes", recipeService.countPublished());
            model.addAttribute("totalCategories", categoryService.countActive());
            model.addAttribute("totalUsers", userService.count());
            
            // Ajouter les recettes les plus récentes
            model.addAttribute("latestRecipes", recipeService.findLatest(6));
            model.addAttribute("topRatedRecipes", recipeService.findTopRated(3));
        } catch (Exception e) {
            // En cas d'erreur Firestore, on continue avec des valeurs par défaut
            model.addAttribute("totalRecipes", 0);
            model.addAttribute("totalCategories", 0);
            model.addAttribute("totalUsers", 0);
            model.addAttribute("firestoreError", true);
        }
        
        return "index";
    }

    @GetMapping("/recipes")
    public String recipes(Model model) {
        model.addAttribute("title", "Liste des Recettes");
        
        try {
            model.addAttribute("recipes", recipeService.findAllPublished());
            model.addAttribute("categories", categoryService.findAllActive());
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des recettes: " + e.getMessage());
        }
        
        return "recipes/list";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("title", "Liste des Catégories");
        
        try {
            model.addAttribute("categories", categoryService.findAllActive());
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des catégories: " + e.getMessage());
        }
        
        return "categories/list";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("title", "Liste des Utilisateurs");
        
        try {
            model.addAttribute("users", userService.findAllActive());
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
        
        return "users/list";
    }
}
