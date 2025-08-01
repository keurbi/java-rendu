package com.example.recipeapp.service;

import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service pour la gestion des recettes
 */
@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    /**
     * Crée une nouvelle recette
     */
    public Recipe createRecipe(Recipe recipe) throws ExecutionException, InterruptedException {
        // Vérifier que la catégorie existe
        if (categoryService.findById(recipe.getCategoryId()).isEmpty()) {
            throw new RuntimeException("Catégorie non trouvée");
        }

        // Vérifier que l'auteur existe
        if (userService.findById(recipe.getAuthorId()).isEmpty()) {
            throw new RuntimeException("Auteur non trouvé");
        }

        // Initialiser les compteurs
        recipe.setRating(0.0);
        recipe.setRatingCount(0);
        recipe.setFavoriteCount(0);
        recipe.setViewCount(0);

        return recipeRepository.save(recipe);
    }

    /**
     * Met à jour une recette existante
     */
    public Recipe updateRecipe(Recipe recipe) throws ExecutionException, InterruptedException {
        Optional<Recipe> existingRecipe = recipeRepository.findById(recipe.getId());
        if (existingRecipe.isEmpty()) {
            throw new RuntimeException("Recette non trouvée");
        }

        // Vérifier que la catégorie existe (si changée)
        if (categoryService.findById(recipe.getCategoryId()).isEmpty()) {
            throw new RuntimeException("Catégorie non trouvée");
        }

        // Conserver les statistiques existantes
        Recipe existing = existingRecipe.get();
        recipe.setRating(existing.getRating());
        recipe.setRatingCount(existing.getRatingCount());
        recipe.setFavoriteCount(existing.getFavoriteCount());
        recipe.setViewCount(existing.getViewCount());
        recipe.setCreatedAt(existing.getCreatedAt());

        return recipeRepository.save(recipe);
    }

    /**
     * Trouve une recette par son ID et incrémente le compteur de vues
     */
    public Optional<Recipe> findByIdAndIncrementViews(String id) throws ExecutionException, InterruptedException {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.incrementViewCount(id);
            recipe.get().incrementViewCount();
        }
        return recipe;
    }

    /**
     * Trouve une recette par son ID sans incrémenter les vues
     */
    public Optional<Recipe> findById(String id) throws ExecutionException, InterruptedException {
        return recipeRepository.findById(id);
    }

    /**
     * Récupère toutes les recettes
     */
    public List<Recipe> findAll() throws ExecutionException, InterruptedException {
        return recipeRepository.findAll();
    }

    /**
     * Récupère toutes les recettes publiées
     */
    public List<Recipe> findAllPublished() throws ExecutionException, InterruptedException {
        return recipeRepository.findAllPublished();
    }

    /**
     * Trouve les recettes par catégorie
     */
    public List<Recipe> findByCategoryId(String categoryId) throws ExecutionException, InterruptedException {
        return recipeRepository.findByCategoryId(categoryId);
    }

    /**
     * Trouve les recettes par auteur
     */
    public List<Recipe> findByAuthorId(String authorId) throws ExecutionException, InterruptedException {
        return recipeRepository.findByAuthorId(authorId);
    }

    /**
     * Trouve les recettes publiées par auteur
     */
    public List<Recipe> findPublishedByAuthorId(String authorId) throws ExecutionException, InterruptedException {
        return recipeRepository.findPublishedByAuthorId(authorId);
    }

    /**
     * Trouve les recettes par niveau de difficulté
     */
    public List<Recipe> findByDifficulty(Recipe.DifficultyLevel difficulty) throws ExecutionException, InterruptedException {
        return recipeRepository.findByDifficulty(difficulty);
    }

    /**
     * Trouve les recettes les mieux notées
     */
    public List<Recipe> findTopRated(int limit) throws ExecutionException, InterruptedException {
        return recipeRepository.findTopRated(limit);
    }

    /**
     * Trouve les recettes les plus récentes
     */
    public List<Recipe> findLatest(int limit) throws ExecutionException, InterruptedException {
        return recipeRepository.findLatest(limit);
    }

    /**
     * Recherche des recettes par titre
     */
    public List<Recipe> searchByTitle(String searchTerm) throws ExecutionException, InterruptedException {
        return recipeRepository.searchByTitle(searchTerm);
    }

    /**
     * Supprime une recette
     */
    public boolean deleteRecipe(String id) throws ExecutionException, InterruptedException {
        return recipeRepository.deleteById(id);
    }

    /**
     * Met à jour le statut de publication d'une recette
     */
    public void updatePublishedStatus(String recipeId, boolean published) throws ExecutionException, InterruptedException {
        recipeRepository.updatePublishedStatus(recipeId, published);
    }

    /**
     * Note une recette
     */
    public void rateRecipe(String recipeId, double rating) throws ExecutionException, InterruptedException {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isPresent()) {
            Recipe recipe = recipeOpt.get();
            
            // Calculer la nouvelle moyenne
            double currentRating = recipe.getRating() != null ? recipe.getRating() : 0.0;
            int currentCount = recipe.getRatingCount() != null ? recipe.getRatingCount() : 0;
            
            double totalRating = currentRating * currentCount + rating;
            int newCount = currentCount + 1;
            double newRating = totalRating / newCount;
            
            // Mettre à jour dans Firestore
            recipeRepository.updateRating(recipeId, newRating, newCount);
        }
    }

    /**
     * Compte le nombre total de recettes
     */
    public long count() throws ExecutionException, InterruptedException {
        return recipeRepository.count();
    }

    /**
     * Compte le nombre de recettes publiées
     */
    public long countPublished() throws ExecutionException, InterruptedException {
        return recipeRepository.countPublished();
    }

    /**
     * Vérifie si un utilisateur peut modifier une recette
     */
    public boolean canUserEditRecipe(String userId, String recipeId) throws ExecutionException, InterruptedException {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            // L'auteur peut toujours modifier sa recette
            if (recipe.get().getAuthorId().equals(userId)) {
                return true;
            }
            
            // Vérifier si l'utilisateur est admin
            Optional<Recipe.User> user = userService.findById(userId);
            return user.isPresent() && user.get().isAdmin();
        }
        return false;
    }

    /**
     * Récupère les statistiques globales des recettes
     */
    public RecipeStats getGlobalStats() throws ExecutionException, InterruptedException {
        long totalRecipes = count();
        long publishedRecipes = countPublished();
        List<Recipe> topRated = findTopRated(10);
        
        return new RecipeStats(totalRecipes, publishedRecipes, topRated);
    }

    /**
     * Classe pour les statistiques des recettes
     */
    public static class RecipeStats {
        private final long totalRecipes;
        private final long publishedRecipes;
        private final List<Recipe> topRatedRecipes;

        public RecipeStats(long totalRecipes, long publishedRecipes, List<Recipe> topRatedRecipes) {
            this.totalRecipes = totalRecipes;
            this.publishedRecipes = publishedRecipes;
            this.topRatedRecipes = topRatedRecipes;
        }

        public long getTotalRecipes() { return totalRecipes; }
        public long getPublishedRecipes() { return publishedRecipes; }
        public List<Recipe> getTopRatedRecipes() { return topRatedRecipes; }
    }
}
