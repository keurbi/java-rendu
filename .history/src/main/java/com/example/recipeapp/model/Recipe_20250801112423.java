package com.example.recipeapp.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Modèle représentant une recette de cuisine
 */
public class Recipe {
    
    @DocumentId
    private String id;
    
    @NotBlank(message = "Le titre de la recette est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    @PropertyName("title")
    private String title;
    
    @PropertyName("description")
    private String description;
    
    @NotNull(message = "Les ingrédients sont obligatoires")
    @PropertyName("ingredients")
    private List<Ingredient> ingredients;
    
    @NotNull(message = "Les instructions sont obligatoires")
    @PropertyName("instructions")
    private List<Instruction> instructions;
    
    @NotBlank(message = "La catégorie est obligatoire")
    @PropertyName("categoryId")
    private String categoryId;
    
    @NotBlank(message = "L'auteur est obligatoire")
    @PropertyName("authorId")
    private String authorId;
    
    @PropertyName("imageUrl")
    private String imageUrl;
    
    @Min(value = 1, message = "Le nombre de portions doit être au moins 1")
    @Max(value = 50, message = "Le nombre de portions ne peut pas dépasser 50")
    @PropertyName("servings")
    private Integer servings;
    
    @Min(value = 1, message = "Le temps de préparation doit être au moins 1 minute")
    @PropertyName("prepTimeMinutes")
    private Integer prepTimeMinutes;
    
    @Min(value = 0, message = "Le temps de cuisson ne peut pas être négatif")
    @PropertyName("cookTimeMinutes")
    private Integer cookTimeMinutes;
    
    @PropertyName("difficulty")
    private DifficultyLevel difficulty;
    
    @PropertyName("tags")
    private List<String> tags;
    
    @PropertyName("nutritionInfo")
    private NutritionInfo nutritionInfo;
    
    @PropertyName("rating")
    private Double rating = 0.0;
    
    @PropertyName("ratingCount")
    private Integer ratingCount = 0;
    
    @PropertyName("favoriteCount")
    private Integer favoriteCount = 0;
    
    @PropertyName("viewCount")
    private Integer viewCount = 0;
    
    @PropertyName("published")
    private boolean published = false;
    
    @PropertyName("createdAt")
    private LocalDateTime createdAt;
    
    @PropertyName("updatedAt")
    private LocalDateTime updatedAt;

    // Enum pour le niveau de difficulté
    public enum DifficultyLevel {
        FACILE("Facile"),
        MOYEN("Moyen"),
        DIFFICILE("Difficile");
        
        private final String displayName;
        
        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }

    // Classe interne pour les ingrédients
    public static class Ingredient {
        @PropertyName("name")
        private String name;
        
        @PropertyName("quantity")
        private Double quantity;
        
        @PropertyName("unit")
        private String unit;
        
        @PropertyName("optional")
        private boolean optional = false;

        public Ingredient() {}

        public Ingredient(String name, Double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }

        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public boolean isOptional() { return optional; }
        public void setOptional(boolean optional) { this.optional = optional; }

        @Override
        public String toString() {
            String result = "";
            if (quantity != null && quantity > 0) {
                result += quantity + " ";
            }
            if (unit != null && !unit.trim().isEmpty()) {
                result += unit + " ";
            }
            result += name;
            if (optional) {
                result += " (optionnel)";
            }
            return result;
        }
    }

    // Classe interne pour les instructions
    public static class Instruction {
        @PropertyName("stepNumber")
        private Integer stepNumber;
        
        @PropertyName("description")
        private String description;
        
        @PropertyName("imageUrl")
        private String imageUrl;
        
        @PropertyName("timeMinutes")
        private Integer timeMinutes;

        public Instruction() {}

        public Instruction(Integer stepNumber, String description) {
            this.stepNumber = stepNumber;
            this.description = description;
        }

        // Getters et Setters
        public Integer getStepNumber() { return stepNumber; }
        public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public Integer getTimeMinutes() { return timeMinutes; }
        public void setTimeMinutes(Integer timeMinutes) { this.timeMinutes = timeMinutes; }
    }

    // Classe interne pour les informations nutritionnelles
    public static class NutritionInfo {
        @PropertyName("calories")
        private Integer calories;
        
        @PropertyName("protein")
        private Double protein;
        
        @PropertyName("carbohydrates")
        private Double carbohydrates;
        
        @PropertyName("fat")
        private Double fat;
        
        @PropertyName("fiber")
        private Double fiber;
        
        @PropertyName("sugar")
        private Double sugar;

        public NutritionInfo() {}

        // Getters et Setters
        public Integer getCalories() { return calories; }
        public void setCalories(Integer calories) { this.calories = calories; }
        
        public Double getProtein() { return protein; }
        public void setProtein(Double protein) { this.protein = protein; }
        
        public Double getCarbohydrates() { return carbohydrates; }
        public void setCarbohydrates(Double carbohydrates) { this.carbohydrates = carbohydrates; }
        
        public Double getFat() { return fat; }
        public void setFat(Double fat) { this.fat = fat; }
        
        public Double getFiber() { return fiber; }
        public void setFiber(Double fiber) { this.fiber = fiber; }
        
        public Double getSugar() { return sugar; }
        public void setSugar(Double sugar) { this.sugar = sugar; }
    }

    // Constructeurs
    public Recipe() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Recipe(String title, String categoryId, String authorId) {
        this();
        this.title = title;
        this.categoryId = categoryId;
        this.authorId = authorId;
    }

    // Getters et Setters principaux
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title; 
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description; 
        this.updatedAt = LocalDateTime.now();
    }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { 
        this.ingredients = ingredients; 
        this.updatedAt = LocalDateTime.now();
    }

    public List<Instruction> getInstructions() { return instructions; }
    public void setInstructions(List<Instruction> instructions) { 
        this.instructions = instructions; 
        this.updatedAt = LocalDateTime.now();
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { 
        this.categoryId = categoryId; 
        this.updatedAt = LocalDateTime.now();
    }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { 
        this.authorId = authorId; 
        this.updatedAt = LocalDateTime.now();
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl; 
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { 
        this.servings = servings; 
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getPrepTimeMinutes() { return prepTimeMinutes; }
    public void setPrepTimeMinutes(Integer prepTimeMinutes) { 
        this.prepTimeMinutes = prepTimeMinutes; 
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getCookTimeMinutes() { return cookTimeMinutes; }
    public void setCookTimeMinutes(Integer cookTimeMinutes) { 
        this.cookTimeMinutes = cookTimeMinutes; 
        this.updatedAt = LocalDateTime.now();
    }

    public DifficultyLevel getDifficulty() { return difficulty; }
    public void setDifficulty(DifficultyLevel difficulty) { 
        this.difficulty = difficulty; 
        this.updatedAt = LocalDateTime.now();
    }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { 
        this.tags = tags; 
        this.updatedAt = LocalDateTime.now();
    }

    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public void setNutritionInfo(NutritionInfo nutritionInfo) { 
        this.nutritionInfo = nutritionInfo; 
        this.updatedAt = LocalDateTime.now();
    }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getRatingCount() { return ratingCount; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

    public Integer getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { 
        this.published = published; 
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Méthodes utilitaires
    public Integer getTotalTimeMinutes() {
        int total = 0;
        if (prepTimeMinutes != null) total += prepTimeMinutes;
        if (cookTimeMinutes != null) total += cookTimeMinutes;
        return total;
    }

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }

    public void incrementFavoriteCount() {
        this.favoriteCount = (this.favoriteCount == null) ? 1 : this.favoriteCount + 1;
    }

    public void decrementFavoriteCount() {
        if (this.favoriteCount != null && this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", difficulty=" + difficulty +
                ", published=" + published +
                '}';
    }
}
