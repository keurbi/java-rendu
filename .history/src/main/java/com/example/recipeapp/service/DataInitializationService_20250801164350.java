package com.example.recipeapp.service;

import com.example.recipeapp.model.Category;
import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service pour initialiser les données de démonstration
 */
@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecipeService recipeService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Application démarrée avec succès !");
        System.out.println("📍 Accès web : http://localhost:8080");
        System.out.println("📍 API REST : http://localhost:8080/api/recipes");
        System.out.println("");
        System.out.println("� Initialisation des données d'exemple...");
        
        // Réactiver l'initialisation des données
        try {
            initializeData();
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation : " + e.getMessage());
            System.out.println("⚠️ L'application fonctionne mais sans données d'exemple");
        }
    }

    public void initializeData() throws Exception {
        System.out.println("🚀 Initialisation des données de démonstration...");

        // Créer les catégories par défaut
        categoryService.createDefaultCategories();
        System.out.println("✅ Catégories créées");

        // Créer des utilisateurs de démonstration
        createDemoUsers();
        System.out.println("✅ Utilisateurs de démonstration créés");

        // Créer des recettes de démonstration
        createDemoRecipes();
        System.out.println("✅ Recettes de démonstration créées");

        System.out.println("🎉 Initialisation terminée avec succès !");
    }

    private void createDemoUsers() throws Exception {
        // Utilisateur admin
        if (!userService.findByUsername("admin").isPresent()) {
            User admin = new User("admin", "admin@recettes.fr", "admin123");
            admin.setFirstName("Admin");
            admin.setLastName("Principal");
            admin.setBio("Administrateur de l'application de recettes");
            admin.setRoles(Arrays.asList("USER", "ADMIN"));
            userService.createUser(admin);
        }

        // Chef cuisinier
        if (!userService.findByUsername("chef_marie").isPresent()) {
            User chef = new User("chef_marie", "marie@recettes.fr", "password123");
            chef.setFirstName("Marie");
            chef.setLastName("Dubois");
            chef.setBio("Chef cuisinière passionnée, spécialisée dans la cuisine française traditionnelle");
            chef.setProfileImageUrl("https://images.unsplash.com/photo-1595273670150-bd0c3c392e46?w=150");
            userService.createUser(chef);
        }

        // Pâtissier
        if (!userService.findByUsername("patissier_paul").isPresent()) {
            User patissier = new User("patissier_paul", "paul@recettes.fr", "password123");
            patissier.setFirstName("Paul");
            patissier.setLastName("Martin");
            patissier.setBio("Pâtissier créatif, amateur de desserts innovants et de pâtisseries classiques");
            patissier.setProfileImageUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150");
            userService.createUser(patissier);
        }

        // Utilisateur standard
        if (!userService.findByUsername("sophie_cuisine").isPresent()) {
            User user = new User("sophie_cuisine", "sophie@recettes.fr", "password123");
            user.setFirstName("Sophie");
            user.setLastName("Leroy");
            user.setBio("Passionnée de cuisine du monde, j'aime partager mes découvertes culinaires");
            user.setProfileImageUrl("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150");
            userService.createUser(user);
        }
    }

    private void createDemoRecipes() throws Exception {
        // Récupérer les utilisateurs et catégories
        User chef = userService.findByUsername("chef_marie").orElse(null);
        User patissier = userService.findByUsername("patissier_paul").orElse(null);
        User sophie = userService.findByUsername("sophie_cuisine").orElse(null);

        Category entrees = categoryService.findByName("Entrées").orElse(null);
        Category plats = categoryService.findByName("Plats principaux").orElse(null);
        Category desserts = categoryService.findByName("Desserts").orElse(null);
        Category salades = categoryService.findByName("Salades").orElse(null);

        if (chef != null && plats != null) {
            // Coq au Vin de Chef Marie
            Recipe coqAuVin = new Recipe("Coq au Vin traditionnel", plats.getId(), chef.getId());
            coqAuVin.setDescription("Un grand classique de la cuisine française, le coq au vin est un plat mijoté savoureux et réconfortant, parfait pour les repas en famille.");
            coqAuVin.setServings(6);
            coqAuVin.setPrepTimeMinutes(30);
            coqAuVin.setCookTimeMinutes(90);
            coqAuVin.setDifficulty(Recipe.DifficultyLevel.MOYEN);
            coqAuVin.setPublished(true);
            coqAuVin.setImageUrl("https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800");
            coqAuVin.setTags(Arrays.asList("français", "traditionnel", "vin rouge", "volaille"));

            // Ingrédients
            Recipe.Ingredient[] ingredients = {
                new Recipe.Ingredient("Coq découpé", 1.0, "pièce"),
                new Recipe.Ingredient("Vin rouge", 750.0, "ml"),
                new Recipe.Ingredient("Lardons", 200.0, "g"),
                new Recipe.Ingredient("Champignons de Paris", 300.0, "g"),
                new Recipe.Ingredient("Oignons grelots", 250.0, "g"),
                new Recipe.Ingredient("Carottes", 2.0, "pièces"),
                new Recipe.Ingredient("Bouquet garni", 1.0, "pièce"),
                new Recipe.Ingredient("Beurre", 50.0, "g"),
                new Recipe.Ingredient("Farine", 2.0, "cuillères à soupe"),
                new Recipe.Ingredient("Cognac", 3.0, "cuillères à soupe")
            };
            coqAuVin.setIngredients(Arrays.asList(ingredients));

            // Instructions
            Recipe.Instruction[] instructions = {
                new Recipe.Instruction(1, "Faire revenir les lardons dans une cocotte jusqu'à ce qu'ils soient dorés. Les réserver."),
                new Recipe.Instruction(2, "Dans la même cocotte, faire dorer les morceaux de coq de tous côtés. Flamber au cognac."),
                new Recipe.Instruction(3, "Saupoudrer de farine, mélanger et verser le vin rouge. Ajouter le bouquet garni."),
                new Recipe.Instruction(4, "Couvrir et laisser mijoter 1h à feu doux."),
                new Recipe.Instruction(5, "Pendant ce temps, faire revenir les oignons grelots et les champignons dans le beurre."),
                new Recipe.Instruction(6, "Ajouter les légumes et les lardons dans la cocotte. Poursuivre la cuisson 30 minutes."),
                new Recipe.Instruction(7, "Vérifier l'assaisonnement et servir bien chaud avec des pommes de terre vapeur.")
            };
            coqAuVin.setInstructions(Arrays.asList(instructions));

            recipeService.createRecipe(coqAuVin);
        }

        if (patissier != null && desserts != null) {
            // Tarte aux Fraises de Paul
            Recipe tarteFramboises = new Recipe("Tarte aux fraises et crème pâtissière", desserts.getId(), patissier.getId());
            tarteFramboises.setDescription("Une tarte classique avec une pâte sablée croustillante, une onctueuse crème pâtissière à la vanille et des fraises fraîches de saison.");
            tarteFramboises.setServings(8);
            tarteFramboises.setPrepTimeMinutes(45);
            tarteFramboises.setCookTimeMinutes(35);
            tarteFramboises.setDifficulty(Recipe.DifficultyLevel.MOYEN);
            tarteFramboises.setPublished(true);
            tarteFramboises.setImageUrl("https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=800");
            tarteFramboises.setTags(Arrays.asList("tarte", "fraises", "crème pâtissière", "dessert"));

            // Ingrédients
            Recipe.Ingredient[] ingredients2 = {
                new Recipe.Ingredient("Farine", 250.0, "g"),
                new Recipe.Ingredient("Beurre", 125.0, "g"),
                new Recipe.Ingredient("Sucre", 50.0, "g"),
                new Recipe.Ingredient("Œuf", 1.0, "pièce"),
                new Recipe.Ingredient("Lait", 500.0, "ml"),
                new Recipe.Ingredient("Jaunes d'œufs", 4.0, "pièces"),
                new Recipe.Ingredient("Sucre en poudre", 100.0, "g"),
                new Recipe.Ingredient("Maïzena", 40.0, "g"),
                new Recipe.Ingredient("Vanille", 1.0, "gousse"),
                new Recipe.Ingredient("Fraises", 500.0, "g")
            };
            tarteFramboises.setIngredients(Arrays.asList(ingredients2));

            // Instructions
            Recipe.Instruction[] instructions2 = {
                new Recipe.Instruction(1, "Préparer la pâte sablée en mélangeant farine, beurre froid, sucre et œuf. Former une boule et laisser reposer 1h au frais."),
                new Recipe.Instruction(2, "Étaler la pâte et foncer un moule à tarte. Piquer le fond et cuire à blanc 15 minutes à 180°C."),
                new Recipe.Instruction(3, "Pour la crème pâtissière : faire chauffer le lait avec la vanille fendue."),
                new Recipe.Instruction(4, "Battre les jaunes d'œufs avec le sucre jusqu'à blanchissement, ajouter la maïzena."),
                new Recipe.Instruction(5, "Verser le lait chaud progressivement, remettre sur le feu et faire épaissir en remuant constamment."),
                new Recipe.Instruction(6, "Laisser refroidir la crème en couvrant d'un film plastique au contact."),
                new Recipe.Instruction(7, "Garnir le fond de tarte de crème pâtissière, disposer les fraises équeutées et servir frais.")
            };
            tarteFramboises.setInstructions(Arrays.asList(instructions2));

            recipeService.createRecipe(tarteFramboises);
        }

        if (sophie != null && salades != null) {
            // Salade Buddha Bowl de Sophie
            Recipe buddhaBowl = new Recipe("Buddha Bowl arc-en-ciel", salades.getId(), sophie.getId());
            buddhaBowl.setDescription("Un bowl coloré et nutritif, parfait pour un déjeuner sain et équilibré. Plein de légumes de saison, de protéines végétales et d'une sauce tahini délicieuse.");
            buddhaBowl.setServings(2);
            buddhaBowl.setPrepTimeMinutes(25);
            buddhaBowl.setCookTimeMinutes(20);
            buddhaBowl.setDifficulty(Recipe.DifficultyLevel.FACILE);
            buddhaBowl.setPublished(true);
            buddhaBowl.setImageUrl("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800");
            buddhaBowl.setTags(Arrays.asList("healthy", "végétarien", "bowl", "coloré", "quinoa"));

            // Ingrédients
            Recipe.Ingredient[] ingredients3 = {
                new Recipe.Ingredient("Quinoa", 150.0, "g"),
                new Recipe.Ingredient("Betterave cuite", 2.0, "pièces"),
                new Recipe.Ingredient("Carottes", 2.0, "pièces"),
                new Recipe.Ingredient("Avocat", 1.0, "pièce"),
                new Recipe.Ingredient("Concombre", 1.0, "pièce"),
                new Recipe.Ingredient("Edamame", 100.0, "g"),
                new Recipe.Ingredient("Graines de tournesol", 2.0, "cuillères à soupe"),
                new Recipe.Ingredient("Tahini", 3.0, "cuillères à soupe"),
                new Recipe.Ingredient("Citron", 1.0, "pièce"),
                new Recipe.Ingredient("Huile d'olive", 2.0, "cuillères à soupe"),
                new Recipe.Ingredient("Miel", 1.0, "cuillère à café"),
                new Recipe.Ingredient("Gingembre frais", 1.0, "cm")
            };
            buddhaBowl.setIngredients(Arrays.asList(ingredients3));

            // Instructions
            Recipe.Instruction[] instructions3 = {
                new Recipe.Instruction(1, "Cuire le quinoa dans 300ml d'eau bouillante salée pendant 15 minutes. Laisser refroidir."),
                new Recipe.Instruction(2, "Râper les carottes en julienne, couper les betteraves en lamelles."),
                new Recipe.Instruction(3, "Découper l'avocat et le concombre en dés."),
                new Recipe.Instruction(4, "Préparer la sauce en mélangeant tahini, jus de citron, huile d'olive, miel et gingembre râpé."),
                new Recipe.Instruction(5, "Cuire les edamame 3 minutes dans l'eau bouillante salée."),
                new Recipe.Instruction(6, "Disposer tous les ingrédients harmonieusement dans 2 bols."),
                new Recipe.Instruction(7, "Parsemer de graines de tournesol et servir avec la sauce tahini à côté.")
            };
            buddhaBowl.setInstructions(Arrays.asList(instructions3));

            recipeService.createRecipe(buddhaBowl);
        }

        if (chef != null && entrees != null) {
            // Velouté de potimarron de Chef Marie
            Recipe veloute = new Recipe("Velouté de potimarron aux châtaignes", entrees.getId(), chef.getId());
            veloute.setDescription("Un velouté onctueux et réconfortant aux saveurs automnales. Le mariage du potimarron et des châtaignes crée une harmonie parfaite.");
            veloute.setServings(4);
            veloute.setPrepTimeMinutes(20);
            veloute.setCookTimeMinutes(40);
            veloute.setDifficulty(Recipe.DifficultyLevel.FACILE);
            veloute.setPublished(true);
            veloute.setImageUrl("https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?w=800");
            veloute.setTags(Arrays.asList("automne", "velouté", "potimarron", "châtaignes", "réconfortant"));

            // Ingrédients
            Recipe.Ingredient[] ingredients4 = {
                new Recipe.Ingredient("Potimarron", 800.0, "g"),
                new Recipe.Ingredient("Châtaignes cuites", 150.0, "g"),
                new Recipe.Ingredient("Oignon", 1.0, "pièce"),
                new Recipe.Ingredient("Bouillon de légumes", 800.0, "ml"),
                new Recipe.Ingredient("Crème fraîche", 100.0, "ml"),
                new Recipe.Ingredient("Beurre", 30.0, "g"),
                new Recipe.Ingredient("Muscade", 1.0, "pincée"),
                new Recipe.Ingredient("Sel", 1.0, "au goût"),
                new Recipe.Ingredient("Poivre", 1.0, "au goût"),
                new Recipe.Ingredient("Graines de courge", 2.0, "cuillères à soupe")
            };
            veloute.setIngredients(Arrays.asList(ingredients4));

            // Instructions
            Recipe.Instruction[] instructions4 = {
                new Recipe.Instruction(1, "Éplucher et couper le potimarron en cubes. Émincer l'oignon."),
                new Recipe.Instruction(2, "Faire revenir l'oignon dans le beurre jusqu'à ce qu'il soit translucide."),
                new Recipe.Instruction(3, "Ajouter les cubes de potimarron et les châtaignes émiettées."),
                new Recipe.Instruction(4, "Verser le bouillon de légumes et porter à ébullition."),
                new Recipe.Instruction(5, "Laisser mijoter 30 minutes jusqu'à ce que le potimarron soit tendre."),
                new Recipe.Instruction(6, "Mixer le tout jusqu'à obtenir une texture lisse et onctueuse."),
                new Recipe.Instruction(7, "Ajouter la crème fraîche, assaisonner avec sel, poivre et muscade."),
                new Recipe.Instruction(8, "Servir chaud, décoré de graines de courge grillées.")
            };
            veloute.setInstructions(Arrays.asList(instructions4));

            recipeService.createRecipe(veloute);
        }
    }
}
