package com.example.recipeapp.controller;

import com.example.recipeapp.model.Category;
import com.example.recipeapp.service.CategoryService;
import com.example.recipeapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Controller pour les pages de catégories
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecipeService recipeService;

    /**
     * Détail d'une catégorie
     */
    @GetMapping("/{id}")
    public String categoryDetail(@PathVariable String id, Model model) {
        try {
            Optional<Category> category = categoryService.findById(id);
            
            if (category.isPresent()) {
                model.addAttribute("category", category.get());
                model.addAttribute("title", category.get().getName());
                
                // Charger les recettes de cette catégorie
                model.addAttribute("recipes", recipeService.findByCategoryId(id));
                
                return "categories/detail";
            } else {
                model.addAttribute("error", "Catégorie non trouvée");
                return "error/404";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de la catégorie: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * Catégorie par slug
     */
    @GetMapping("/slug/{slug}")
    public String categoryBySlug(@PathVariable String slug, Model model) {
        try {
            Optional<Category> category = categoryService.findBySlug(slug);
            
            if (category.isPresent()) {
                return "redirect:/categories/" + category.get().getId();
            } else {
                model.addAttribute("error", "Catégorie non trouvée");
                return "error/404";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de la catégorie: " + e.getMessage());
            return "error/500";
        }
    }
}
