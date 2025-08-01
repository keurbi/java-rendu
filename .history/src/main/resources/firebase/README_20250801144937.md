# Configuration Firebase

## Instructions pour configurer Firebase

### 1. Créer un projet Firebase

1. Allez sur [https://console.firebase.google.com/](https://console.firebase.google.com/)
2. Cliquez sur "Créer un projet"
3. Donnez un nom à votre projet (ex: "recipe-app")
4. Activez Google Analytics si souhaité
5. Créez le projet

### 2. Activer Firestore

1. Dans le menu de gauche, cliquez sur "Firestore Database"
2. Cliquez sur "Créer une base de données"
3. Choisissez "Commencer en mode test" pour le développement
4. Sélectionnez une localisation (ex: europe-west1)

### 3. Créer un compte de service

1. Allez dans "Paramètres du projet" (icône engrenage)
2. Onglet "Comptes de service"
3. Cliquez sur "Générer une nouvelle clé privée"
4. Téléchargez le fichier JSON généré
5. **Renommez le fichier en `service-account-key.json`**
6. **Placez ce fichier dans ce répertoire (`src/main/resources/firebase/`)**

### 4. Configurer l'application

1. Copiez l'ID du projet depuis la console Firebase
2. Modifiez le fichier `application.yml` :
   ```yaml
   firebase:
     project-id: VOTRE_PROJECT_ID_ICI
   ```

### 5. Sécurité

⚠️ **IMPORTANT** : Le fichier `service-account-key.json` contient des informations sensibles.

- Il est déjà ajouté au `.gitignore` pour éviter qu'il soit versionné
- Ne jamais partager ce fichier publiquement
- Utilisez des variables d'environnement en production

### Structure attendue

```
src/main/resources/firebase/
├── README.md (ce fichier)
└── service-account-key.json (à créer)
```

### Exemple de service-account-key.json

Le fichier aura cette structure (ne pas copier, utiliser le vrai fichier téléchargé) :

```json
{
  "type": "service_account",
  "project_id": "votre-project-id",
  "private_key_id": "...",
  "private_key": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-xxxxx@votre-project-id.iam.gserviceaccount.com",
  "client_id": "...",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "..."
}
```
