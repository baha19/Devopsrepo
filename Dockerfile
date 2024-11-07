# Dockerfile
FROM maven:3.8.7-openjdk-17 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY . .

# Construire l'application
RUN mvn package -DskipTests

# Utiliser une image JDK légère pour exécuter l'application
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/mon-app.jar /app.jar

# Définir le point d'entrée
ENTRYPOINT ["java", "-jar", "/app.jar"]
