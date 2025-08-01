package com.example.recipeapp.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modèle représentant une catégorie de recettes
 */
public class Category {
    
    @DocumentId
    private String id;
    
    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @PropertyName("name")
    private String name;
    
    @PropertyName("description")
    private String description;
    
    @PropertyName("color")
    private String color; // Code couleur hexadécimal pour l'affichage
    
    @PropertyName("iconUrl")
    private String iconUrl; // URL de l'icône représentant la catégorie
    
    @PropertyName("slug")
    private String slug; // Version URL-friendly du nom
    
    @PropertyName("createdAt")
    private LocalDateTime createdAt;
    
    @PropertyName("updatedAt")
    private LocalDateTime updatedAt;
    
    @PropertyName("active")
    private boolean active = true;

    // Constructeurs
    public Category() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Category(String name, String description) {
        this();
        this.name = name;
        this.description = description;
        this.slug = generateSlug(name);
    }

    public Category(String name, String description, String color) {
        this(name, description);
        this.color = color;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.slug = generateSlug(name);
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.updatedAt = LocalDateTime.now();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    // Méthodes utilitaires
    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                  .replaceAll("[àáâãäå]", "a")
                  .replaceAll("[èéêë]", "e")
                  .replaceAll("[ìíîï]", "i")
                  .replaceAll("[òóôõö]", "o")
                  .replaceAll("[ùúûü]", "u")
                  .replaceAll("[ýÿ]", "y")
                  .replaceAll("[ç]", "c")
                  .replaceAll("[^a-z0-9\\s-]", "")
                  .replaceAll("\\s+", "-")
                  .replaceAll("-+", "-")
                  .replaceAll("^-|-$", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", active=" + active +
                '}';
    }
}
