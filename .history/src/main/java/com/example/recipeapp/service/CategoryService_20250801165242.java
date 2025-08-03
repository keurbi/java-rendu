package com.example.recipeapp.service;

import com.example.recipeapp.model.Category;
import com.example.recipeapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service pour la gestion des catégories
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Crée une nouvelle catégorie
     */
    public Category createCategory(Category category) throws ExecutionException, InterruptedException {
        // Vérifier que le nom n'existe pas déjà
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        // Vérifier que le slug n'existe pas déjà (le slug est généré automatiquement dans le modèle)
        if (category.getSlug() != null && categoryRepository.existsBySlug(category.getSlug())) {
            throw new RuntimeException("Une catégorie avec ce slug existe déjà");
        }

        // Activer la catégorie par défaut
        category.setActive(true);

        // Initialiser les dates
        Date now = new Date();
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(now);
        }
        category.setUpdatedAt(now);

        return categoryRepository.save(category);
    }

    /**
     * Met à jour une catégorie existante
     */
    public Category updateCategory(Category category) throws ExecutionException, InterruptedException {
        Optional<Category> existingCategory = categoryRepository.findById(category.getId());
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Catégorie non trouvée");
        }

        Category existing = existingCategory.get();

        // Vérifier les conflits de nom (si changé)
        if (!existing.getName().equals(category.getName()) && 
            categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        // Vérifier les conflits de slug (si changé)
        if (!existing.getSlug().equals(category.getSlug()) && 
            categoryRepository.existsBySlug(category.getSlug())) {
            throw new RuntimeException("Une catégorie avec ce slug existe déjà");
        }

        return categoryRepository.save(category);
    }

    /**
     * Trouve une catégorie par son ID
     */
    public Optional<Category> findById(String id) throws ExecutionException, InterruptedException {
        return categoryRepository.findById(id);
    }

    /**
     * Trouve une catégorie par son nom
     */
    public Optional<Category> findByName(String name) throws ExecutionException, InterruptedException {
        return categoryRepository.findByName(name);
    }

    /**
     * Trouve une catégorie par son slug
     */
    public Optional<Category> findBySlug(String slug) throws ExecutionException, InterruptedException {
        return categoryRepository.findBySlug(slug);
    }

    /**
     * Récupère toutes les catégories
     */
    public List<Category> findAll() throws ExecutionException, InterruptedException {
        return categoryRepository.findAll();
    }

    /**
     * Récupère toutes les catégories actives
     */
    public List<Category> findAllActive() throws ExecutionException, InterruptedException {
        return categoryRepository.findAllActive();
    }

    /**
     * Supprime une catégorie
     * Note: Vérifier qu'aucune recette n'utilise cette catégorie avant la suppression
     */
    public boolean deleteCategory(String id) throws ExecutionException, InterruptedException {
        // TODO: Ajouter une vérification pour s'assurer qu'aucune recette n'utilise cette catégorie
        return categoryRepository.deleteById(id);
    }

    /**
     * Active/désactive une catégorie
     */
    public void updateCategoryStatus(String categoryId, boolean active) throws ExecutionException, InterruptedException {
        categoryRepository.updateCategoryStatus(categoryId, active);
    }

    /**
     * Recherche des catégories par nom
     */
    public List<Category> searchByName(String searchTerm) throws ExecutionException, InterruptedException {
        return categoryRepository.searchByName(searchTerm);
    }

    /**
     * Compte le nombre total de catégories
     */
    public long count() throws ExecutionException, InterruptedException {
        return categoryRepository.count();
    }

    /**
     * Compte le nombre de catégories actives
     */
    public long countActive() throws ExecutionException, InterruptedException {
        return categoryRepository.countActive();
    }

    /**
     * Vérifie si une catégorie peut être supprimée
     * (aucune recette ne l'utilise)
     */
    public boolean canBeDeleted(String categoryId) throws ExecutionException, InterruptedException {
        // TODO: Implémenter la vérification avec le RecipeService
        // Pour l'instant, on autorise la suppression
        return true;
    }

    /**
     * Crée les catégories par défaut si elles n'existent pas
     */
    public void createDefaultCategories() throws ExecutionException, InterruptedException {
        String[][] defaultCategories = {
            {"Entrées", "Plats pour commencer le repas", "#e74c3c"},
            {"Plats principaux", "Plats de résistance", "#3498db"},
            {"Desserts", "Sucreries et douceurs", "#f39c12"},
            {"Boissons", "Cocktails, smoothies et autres boissons", "#2ecc71"},
            {"Apéritifs", "Petites bouchées pour l'apéritif", "#9b59b6"},
            {"Salades", "Salades fraîches et composées", "#1abc9c"},
            {"Soupes", "Soupes chaudes et froides", "#e67e22"},
            {"Pâtisseries", "Gâteaux, tartes et pâtisseries", "#f1c40f"}
        };

        for (String[] categoryData : defaultCategories) {
            String name = categoryData[0];
            String description = categoryData[1];
            String color = categoryData[2];

            if (!categoryRepository.existsByName(name)) {
                Category category = new Category(name, description, color);
                categoryRepository.save(category);
            }
        }
    }
}
