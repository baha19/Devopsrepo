# Étape 1: Utiliser une image de base Java (OpenJDK)
FROM openjdk:17-jdk-slim as builder

# Étape 2: Définir le répertoire de travail
WORKDIR /app

# Étape 3: Copier le fichier pom.xml (ou build.gradle si vous utilisez Gradle)
COPY pom.xml .

# Étape 4: Télécharger les dépendances Maven (cela permet de profiter du cache de Docker pour accélérer les builds)
RUN mvn dependency:go-offline

# Étape 5: Copier le code source de l'application
COPY src /app/src

# Étape 6: Compiler l'application et générer le fichier JAR dans le dossier target
RUN mvn clean package -DskipTests

# Étape 7: Créer l'image finale pour exécuter l'application
FROM openjdk:17-jdk-slim

# Étape 8: Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 9: Copier le fichier JAR généré depuis l'étape précédente
COPY --from=builder /app/target/*.jar /app/app.jar

# Étape 10: Exposer le port de l'application Spring Boot
EXPOSE 8080

# Étape 11: Commande pour démarrer l'application
CMD ["java", "-jar", "/app/app.jar"]
