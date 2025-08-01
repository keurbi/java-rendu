package com.example.recipeapp.repository;

import com.example.recipeapp.model.Category;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Repository pour la gestion des catégories dans Firestore
 */
@Repository
public class CategoryRepository {

    private static final String COLLECTION_NAME = "categories";

    @Autowired
    private Firestore firestore;

    /**
     * Sauvegarde ou met à jour une catégorie
     */
    public Category save(Category category) throws ExecutionException, InterruptedException {
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        
        if (category.getId() == null || category.getId().isEmpty()) {
            // Nouvelle catégorie - génération d'un ID automatique
            DocumentReference docRef = categories.document();
            category.setId(docRef.getId());
        }
        
        ApiFuture<WriteResult> result = categories.document(category.getId()).set(category);
        result.get(); // Attendre la completion
        return category;
    }

    /**
     * Trouve une catégorie par son ID
     */
    public Optional<Category> findById(String id) throws ExecutionException, InterruptedException {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            Category category = document.toObject(Category.class);
            if (category != null) {
                category.setId(document.getId());
            }
            return Optional.ofNullable(category);
        }
        
        return Optional.empty();
    }

    /**
     * Trouve une catégorie par son nom
     */
    public Optional<Category> findByName(String name) throws ExecutionException, InterruptedException {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        Query query = categories.whereEqualTo("name", name.trim());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            Category category = document.toObject(Category.class);
            category.setId(document.getId());
            return Optional.of(category);
        }
        
        return Optional.empty();
    }

    /**
     * Trouve une catégorie par son slug
     */
    public Optional<Category> findBySlug(String slug) throws ExecutionException, InterruptedException {
        if (slug == null || slug.trim().isEmpty()) {
            return Optional.empty();
        }
        
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        Query query = categories.whereEqualTo("slug", slug.trim());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            Category category = document.toObject(Category.class);
            category.setId(document.getId());
            return Optional.of(category);
        }
        
        return Optional.empty();
    }

    /**
     * Récupère toutes les catégories
     */
    public List<Category> findAll() throws ExecutionException, InterruptedException {
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        Query query = categories.orderBy("name");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Category> categoryList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Category category = document.toObject(Category.class);
            category.setId(document.getId());
            categoryList.add(category);
        }
        
        return categoryList;
    }

    /**
     * Récupère toutes les catégories actives
     */
    public List<Category> findAllActive() throws ExecutionException, InterruptedException {
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        Query query = categories.whereEqualTo("active", true).orderBy("name");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<Category> categoryList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Category category = document.toObject(Category.class);
            category.setId(document.getId());
            categoryList.add(category);
        }
        
        return categoryList;
    }

    /**
     * Supprime une catégorie par son ID
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
     * Vérifie si une catégorie existe avec le nom donné
     */
    public boolean existsByName(String name) throws ExecutionException, InterruptedException {
        return findByName(name).isPresent();
    }

    /**
     * Vérifie si une catégorie existe avec le slug donné
     */
    public boolean existsBySlug(String slug) throws ExecutionException, InterruptedException {
        return findBySlug(slug).isPresent();
    }

    /**
     * Compte le nombre total de catégories
     */
    public long count() throws ExecutionException, InterruptedException {
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> querySnapshot = categories.get();
        return querySnapshot.get().getDocuments().size();
    }

    /**
     * Compte le nombre de catégories actives
     */
    public long countActive() throws ExecutionException, InterruptedException {
        CollectionReference categories = firestore.collection(COLLECTION_NAME);
        Query query = categories.whereEqualTo("active", true);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return querySnapshot.get().getDocuments().size();
    }

    /**
     * Active/désactive une catégorie
     */
    public void updateCategoryStatus(String categoryId, boolean active) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(categoryId);
        ApiFuture<WriteResult> result = docRef.update("active", active);
        result.get();
    }

    /**
     * Recherche des catégories par nom (recherche partielle)
     */
    public List<Category> searchByName(String searchTerm) throws ExecutionException, InterruptedException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAllActive();
        }
        
        String searchLower = searchTerm.toLowerCase().trim();
        List<Category> allCategories = findAllActive();
        List<Category> matchingCategories = new ArrayList<>();
        
        for (Category category : allCategories) {
            if (category.getName().toLowerCase().contains(searchLower) ||
                (category.getDescription() != null && category.getDescription().toLowerCase().contains(searchLower))) {
                matchingCategories.add(category);
            }
        }
        
        return matchingCategories;
    }
}
