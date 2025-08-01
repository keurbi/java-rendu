package com.example.recipeapp.controller.api;

import com.example.recipeapp.model.User;
import com.example.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API REST pour les utilisateurs
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserApiController {

    @Autowired
    private UserService userService;

    /**
     * Récupère tous les utilisateurs actifs
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.findAllActive();
            // Nettoyer les mots de passe avant de retourner les données
            users.forEach(user -> user.setPassword(null));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère un utilisateur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        try {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                User userObj = user.get();
                // Nettoyer le mot de passe avant de retourner les données
                userObj.setPassword(null);
                return ResponseEntity.ok(userObj);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère un utilisateur par nom d'utilisateur
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                User userObj = user.get();
                // Nettoyer le mot de passe avant de retourner les données
                userObj.setPassword(null);
                return ResponseEntity.ok(userObj);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crée un nouvel utilisateur
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            // Nettoyer le mot de passe avant de retourner les données
            createdUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour un utilisateur existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            // Nettoyer le mot de passe avant de retourner les données
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprime un utilisateur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            boolean deleted = userService.deleteUser(id);
            return deleted ? ResponseEntity.noContent().build() 
                          : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Active/désactive un utilisateur
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable String id, @RequestParam boolean enabled) {
        try {
            userService.updateUserStatus(id, enabled);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Authentifie un utilisateur
     */
    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            Optional<User> user = userService.authenticate(authRequest.getUsernameOrEmail(), authRequest.getPassword());
            if (user.isPresent()) {
                User userObj = user.get();
                // Nettoyer le mot de passe avant de retourner les données
                userObj.setPassword(null);
                return ResponseEntity.ok(userObj);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Change le mot de passe d'un utilisateur
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable String id, @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Ajoute une recette aux favoris
     */
    @PostMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<Void> addFavoriteRecipe(@PathVariable String userId, @PathVariable String recipeId) {
        try {
            userService.addFavoriteRecipe(userId, recipeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retire une recette des favoris
     */
    @DeleteMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<Void> removeFavoriteRecipe(@PathVariable String userId, @PathVariable String recipeId) {
        try {
            userService.removeFavoriteRecipe(userId, recipeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère le nombre d'utilisateurs
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        try {
            long count = userService.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Classe pour les requêtes d'authentification
     */
    public static class AuthRequest {
        private String usernameOrEmail;
        private String password;

        // Getters et setters
        public String getUsernameOrEmail() { return usernameOrEmail; }
        public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * Classe pour les requêtes de changement de mot de passe
     */
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        // Getters et setters
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
