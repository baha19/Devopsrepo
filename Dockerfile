FROM openjdk:17-jdk-slim

EXPOSE 8089
COPY target/Foyer-5.0.0.jar Foyer-5.0.0.jar
ENTRYPOINT ["java","-jar","/Foyer-5.0.0.jar"]