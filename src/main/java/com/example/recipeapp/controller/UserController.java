package com.example.recipeapp.controller;

import com.example.recipeapp.model.User;
import com.example.recipeapp.service.UserService;
import com.example.recipeapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Controller pour les pages d'utilisateurs
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    /**
     * Détail d'un utilisateur
     */
    @GetMapping("/{id}")
    public String userDetail(@PathVariable String id, Model model) {
        try {
            Optional<User> user = userService.findById(id);
            
            if (user.isPresent()) {
                User userObj = user.get();
                model.addAttribute("user", userObj);
                model.addAttribute("title", "Profil de " + userObj.getFullName());
                
                // Charger les recettes de cet utilisateur
                model.addAttribute("userRecipes", recipeService.findPublishedByAuthorId(id));
                
                // Compter le nombre total de recettes (y compris non publiées)
                model.addAttribute("totalRecipes", recipeService.findByAuthorId(id).size());
                
                return "users/detail";
            } else {
                model.addAttribute("error", "Utilisateur non trouvé");
                return "error/404";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du profil utilisateur: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * Utilisateur par nom d'utilisateur
     */
    @GetMapping("/username/{username}")
    public String userByUsername(@PathVariable String username, Model model) {
        try {
            Optional<User> user = userService.findByUsername(username);
            
            if (user.isPresent()) {
                return "redirect:/users/" + user.get().getId();
            } else {
                model.addAttribute("error", "Utilisateur non trouvé");
                return "error/404";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du profil utilisateur: " + e.getMessage());
            return "error/500";
        }
    }
}
