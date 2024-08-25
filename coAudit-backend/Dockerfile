

FROM eclipse-temurin:21-jdk-alpine as build
ARG JARFILE=target/*.jar
WORKDIR /app
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY .mvn .mvn
COPY src src
RUN chmod +x ./mvnw

# Construire le projet
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/BackendConconsult-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","backend.jar"]

