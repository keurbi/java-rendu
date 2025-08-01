#!/bin/bash

# Script de lancement de l'application Recipe App
# Résout les problèmes de compatibilité Java avec Firestore

echo "🚀 Démarrage de Recipe App..."

mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="--add-opens java.base/java.time.chrono=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED"

echo "✅ Application démarrée sur http://localhost:8080"
