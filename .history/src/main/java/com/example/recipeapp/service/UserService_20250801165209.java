package com.example.recipeapp.service;

import com.example.recipeapp.model.User;
import com.example.recipeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service pour la gestion des utilisateurs
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur
     */
    public User createUser(User user) throws ExecutionException, InterruptedException {
        // Vérifier que l'email n'existe pas déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Vérifier que le nom d'utilisateur n'existe pas déjà
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Définir les rôles par défaut
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Arrays.asList("USER"));
        }

        // Activer l'utilisateur par défaut
        user.setEnabled(true);

        // Initialiser les dates
        Date now = new Date();
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant
     */
    public User updateUser(User user) throws ExecutionException, InterruptedException {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        User existing = existingUser.get();

        // Vérifier les conflits d'email (si changé)
        if (!existing.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Vérifier les conflits de nom d'utilisateur (si changé)
        if (!existing.getUsername().equals(user.getUsername()) && 
            userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }

        // Si le mot de passe est fourni, l'encoder
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Conserver l'ancien mot de passe
            user.setPassword(existing.getPassword());
        }

        return userRepository.save(user);
    }

    /**
     * Trouve un utilisateur par son ID
     */
    public Optional<User> findById(String id) throws ExecutionException, InterruptedException {
        return userRepository.findById(id);
    }

    /**
     * Trouve un utilisateur par son email
     */
    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        return userRepository.findByEmail(email);
    }

    /**
     * Trouve un utilisateur par son nom d'utilisateur
     */
    public Optional<User> findByUsername(String username) throws ExecutionException, InterruptedException {
        return userRepository.findByUsername(username);
    }

    /**
     * Récupère tous les utilisateurs
     */
    public List<User> findAll() throws ExecutionException, InterruptedException {
        return userRepository.findAll();
    }

    /**
     * Récupère tous les utilisateurs actifs
     */
    public List<User> findAllActive() throws ExecutionException, InterruptedException {
        return userRepository.findAllActive();
    }

    /**
     * Supprime un utilisateur
     */
    public boolean deleteUser(String id) throws ExecutionException, InterruptedException {
        return userRepository.deleteById(id);
    }

    /**
     * Active/désactive un utilisateur
     */
    public void updateUserStatus(String userId, boolean enabled) throws ExecutionException, InterruptedException {
        userRepository.updateUserStatus(userId, enabled);
    }

    /**
     * Vérifie si les credentials de connexion sont valides
     */
    public boolean validateCredentials(String usernameOrEmail, String password) throws ExecutionException, InterruptedException {
        Optional<User> user = findByUsername(usernameOrEmail);
        if (user.isEmpty()) {
            user = findByEmail(usernameOrEmail);
        }

        if (user.isPresent() && user.get().isEnabled()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }

        return false;
    }

    /**
     * Authentifie un utilisateur et retourne ses informations
     */
    public Optional<User> authenticate(String usernameOrEmail, String password) throws ExecutionException, InterruptedException {
        if (validateCredentials(usernameOrEmail, password)) {
            Optional<User> user = findByUsername(usernameOrEmail);
            if (user.isEmpty()) {
                user = findByEmail(usernameOrEmail);
            }
            return user;
        }
        return Optional.empty();
    }

    /**
     * Change le mot de passe d'un utilisateur
     */
    public void changePassword(String userId, String oldPassword, String newPassword) throws ExecutionException, InterruptedException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        User user = userOpt.get();

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Ajoute une recette aux favoris d'un utilisateur
     */
    public void addFavoriteRecipe(String userId, String recipeId) throws ExecutionException, InterruptedException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFavoriteRecipeIds() == null) {
                user.setFavoriteRecipeIds(Arrays.asList(recipeId));
            } else if (!user.getFavoriteRecipeIds().contains(recipeId)) {
                user.getFavoriteRecipeIds().add(recipeId);
            }
            userRepository.save(user);
        }
    }

    /**
     * Retire une recette des favoris d'un utilisateur
     */
    public void removeFavoriteRecipe(String userId, String recipeId) throws ExecutionException, InterruptedException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFavoriteRecipeIds() != null) {
                user.getFavoriteRecipeIds().remove(recipeId);
                userRepository.save(user);
            }
        }
    }

    /**
     * Compte le nombre total d'utilisateurs
     */
    public long count() throws ExecutionException, InterruptedException {
        return userRepository.count();
    }
}
