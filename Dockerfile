# Base image
FROM openjdk:8-jdk-alpine

# built artifact Path as ARG
ARG JAR_FILE=target/*.jar

# copy over the built artifact
COPY ${JAR_FILE} pokedex-app.jar

# Entrypoint to startup
ENTRYPOINT ["java","-jar","pokedex-app.jar"]

# Port
EXPOSE 5000