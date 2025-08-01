package com.example.recipeapp.repository;

import com.example.recipeapp.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Repository pour la gestion des utilisateurs dans Firestore
 */
@Repository
public class UserRepository {

    private static final String COLLECTION_NAME = "users";

    @Autowired
    private Firestore firestore;

    /**
     * Sauvegarde ou met à jour un utilisateur
     */
    public User save(User user) throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        
        if (user.getId() == null || user.getId().isEmpty()) {
            // Nouvel utilisateur - génération d'un ID automatique
            DocumentReference docRef = users.document();
            user.setId(docRef.getId());
        }
        
        ApiFuture<WriteResult> result = users.document(user.getId()).set(user);
        result.get(); // Attendre la completion
        return user;
    }

    /**
     * Trouve un utilisateur par son ID
     */
    public Optional<User> findById(String id) throws ExecutionException, InterruptedException {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            User user = document.toObject(User.class);
            if (user != null) {
                user.setId(document.getId());
            }
            return Optional.ofNullable(user);
        }
        
        return Optional.empty();
    }

    /**
     * Trouve un utilisateur par son email
     */
    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("email", email.toLowerCase().trim());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            User user = document.toObject(User.class);
            user.setId(document.getId());
            return Optional.of(user);
        }
        
        return Optional.empty();
    }

    /**
     * Trouve un utilisateur par son nom d'utilisateur
     */
    public Optional<User> findByUsername(String username) throws ExecutionException, InterruptedException {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("username", username.trim());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            User user = document.toObject(User.class);
            user.setId(document.getId());
            return Optional.of(user);
        }
        
        return Optional.empty();
    }

    /**
     * Récupère tous les utilisateurs
     */
    public List<User> findAll() throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> querySnapshot = users.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<User> userList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            User user = document.toObject(User.class);
            user.setId(document.getId());
            userList.add(user);
        }
        
        return userList;
    }

    /**
     * Récupère tous les utilisateurs actifs
     */
    public List<User> findAllActive() throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("enabled", true);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        List<User> userList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            User user = document.toObject(User.class);
            user.setId(document.getId());
            userList.add(user);
        }
        
        return userList;
    }

    /**
     * Supprime un utilisateur par son ID
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
     * Vérifie si un utilisateur existe avec l'email donné
     */
    public boolean existsByEmail(String email) throws ExecutionException, InterruptedException {
        return findByEmail(email).isPresent();
    }

    /**
     * Vérifie si un utilisateur existe avec le nom d'utilisateur donné
     */
    public boolean existsByUsername(String username) throws ExecutionException, InterruptedException {
        return findByUsername(username).isPresent();
    }

    /**
     * Compte le nombre total d'utilisateurs
     */
    public long count() throws ExecutionException, InterruptedException {
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> querySnapshot = users.get();
        return querySnapshot.get().getDocuments().size();
    }

    /**
     * Active/désactive un utilisateur
     */
    public void updateUserStatus(String userId, boolean enabled) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(userId);
        ApiFuture<WriteResult> result = docRef.update("enabled", enabled);
        result.get();
    }
}
