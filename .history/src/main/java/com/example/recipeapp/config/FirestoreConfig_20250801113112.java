package com.example.recipeapp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Configuration pour Firestore
 */
@Configuration
public class FirestoreConfig {

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${firebase.service-account-key-path}")
    private String serviceAccountKeyPath;

    @Bean
    public Firestore firestore() throws IOException {
        // Initialisation des credentials Google
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new FileInputStream(serviceAccountKeyPath)
        );

        // Configuration des options Firestore
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();

        return firestoreOptions.getService();
    }
}
