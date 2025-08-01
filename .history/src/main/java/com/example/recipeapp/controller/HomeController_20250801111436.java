package com.example.recipeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenue sur Recipe App!");
        return "index";
    }

    @GetMapping("/recipes")
    public String recipes(Model model) {
        model.addAttribute("title", "Liste des Recettes");
        return "recipes/list";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("title", "Liste des Catégories");
        return "categories/list";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("title", "Liste des Utilisateurs");
        return "users/list";
    }
}
