package com.example.recipeapp.repository;

import com.example.recipeapp.model.Recipe;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Repository pour la gestion des recettes dans Firestore
 */
@Repository
public class RecipeRepository {

    private static final String COLLECTION_NAME = "recipes";

    @Autowired
    private Firestore firestore;

    /**
     * Sauvegarde ou met à jour une recette
     */
    public Recipe save(Recipe recipe) throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        
        if (recipe.getId() == null || recipe.getId().isEmpty()) {
            // Nouvelle recette - génération d'un ID automatique
            DocumentReference docRef = recipes.document();
            recipe.setId(docRef.getId());
        }
        
        ApiFuture<WriteResult> result = recipes.document(recipe.getId()).set(recipe);
        result.get(); // Attendre la completion
        return recipe;
    }

    /**
     * Trouve une recette par son ID
     */
    public Optional<Recipe> findById(String id) throws ExecutionException, InterruptedException {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            Recipe recipe = document.toObject(Recipe.class);
            if (recipe != null) {
                recipe.setId(document.getId());
            }
            return Optional.ofNullable(recipe);
        }
        
        return Optional.empty();
    }

    /**
     * Récupère toutes les recettes
     */
    public List<Recipe> findAll() throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Récupère toutes les recettes publiées
     */
    public List<Recipe> findAllPublished() throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("published", true)
                           .orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes par catégorie
     */
    public List<Recipe> findByCategoryId(String categoryId) throws ExecutionException, InterruptedException {
        if (categoryId == null || categoryId.isEmpty()) {
            return new ArrayList<>();
        }
        
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("categoryId", categoryId)
                           .whereEqualTo("published", true)
                           .orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes par auteur
     */
    public List<Recipe> findByAuthorId(String authorId) throws ExecutionException, InterruptedException {
        if (authorId == null || authorId.isEmpty()) {
            return new ArrayList<>();
        }
        
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("authorId", authorId)
                           .orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes publiées par auteur
     */
    public List<Recipe> findPublishedByAuthorId(String authorId) throws ExecutionException, InterruptedException {
        if (authorId == null || authorId.isEmpty()) {
            return new ArrayList<>();
        }
        
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("authorId", authorId)
                           .whereEqualTo("published", true)
                           .orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes par niveau de difficulté
     */
    public List<Recipe> findByDifficulty(Recipe.DifficultyLevel difficulty) throws ExecutionException, InterruptedException {
        if (difficulty == null) {
            return new ArrayList<>();
        }
        
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("difficulty", difficulty)
                           .whereEqualTo("published", true)
                           .orderBy("createdAt", Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes les mieux notées
     */
    public List<Recipe> findTopRated(int limit) throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("published", true)
                           .orderBy("rating", Query.Direction.DESCENDING)
                           .limit(limit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Trouve les recettes les plus récentes
     */
    public List<Recipe> findLatest(int limit) throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("published", true)
                           .orderBy("createdAt", Query.Direction.DESCENDING)
                           .limit(limit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Recipe> recipeList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Recipe recipe = document.toObject(Recipe.class);
            recipe.setId(document.getId());
            recipeList.add(recipe);
        }
        
        return recipeList;
    }

    /**
     * Recherche des recettes par titre
     */
    public List<Recipe> searchByTitle(String searchTerm) throws ExecutionException, InterruptedException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAllPublished();
        }
        
        String searchLower = searchTerm.toLowerCase().trim();
        List<Recipe> allRecipes = findAllPublished();
        List<Recipe> matchingRecipes = new ArrayList<>();
        
        for (Recipe recipe : allRecipes) {
            if (recipe.getTitle().toLowerCase().contains(searchLower) ||
                (recipe.getDescription() != null && recipe.getDescription().toLowerCase().contains(searchLower)) ||
                (recipe.getTags() != null && recipe.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(searchLower)))) {
                matchingRecipes.add(recipe);
            }
        }
        
        return matchingRecipes;
    }

    /**
     * Supprime une recette par son ID
     */
    public boolean deleteById(String id) throws ExecutionException, InterruptedException {
        if (id == null || id.isEmpty()) {
            return false;
        }
        
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get();
        return true;
    }

    /**
     * Compte le nombre total de recettes
     */
    public long count() throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> querySnapshot = recipes.get();
        return querySnapshot.get().getDocuments().size();
    }

    /**
     * Compte le nombre de recettes publiées
     */
    public long countPublished() throws ExecutionException, InterruptedException {
        CollectionReference recipes = firestore.collection(COLLECTION_NAME);
        Query query = recipes.whereEqualTo("published", true);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return querySnapshot.get().getDocuments().size();
    }

    /**
     * Met à jour le statut de publication d'une recette
     */
    public void updatePublishedStatus(String recipeId, boolean published) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(recipeId);
        ApiFuture<WriteResult> result = docRef.update("published", published);
        result.get();
    }

    /**
     * Met à jour le nombre de vues d'une recette
     */
    public void incrementViewCount(String recipeId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(recipeId);
        ApiFuture<WriteResult> result = docRef.update("viewCount", FieldValue.increment(1));
        result.get();
    }

    /**
     * Met à jour la note d'une recette
     */
    public void updateRating(String recipeId, double newRating, int newRatingCount) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(recipeId);
        ApiFuture<WriteResult> result = docRef.update(
            "rating", newRating,
            "ratingCount", newRatingCount
        );
        result.get();
    }
}
