# Étape 1: Utiliser une image officielle de Maven avec OpenJDK 17 pour construire l'application
FROM maven:3.8.6-openjdk-17 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src /app/src

# Compiler l'application et générer le fichier JAR sans exécuter les tests
RUN mvn clean package -DskipTests

# Étape 2: Utiliser une image OpenJDK légère pour exécuter l'application
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré depuis l'étape de construction
COPY --from=builder /app/target/*.jar /app/app.jar

# Exposer le port de l'application Spring Boot
EXPOSE 8080

# Commande pour démarrer l'application
CMD ["java", "-jar", "/app/app.jar"]
