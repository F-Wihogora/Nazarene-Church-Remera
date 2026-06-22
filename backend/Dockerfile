# Build Stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies in a separate layer for faster rebuild caching
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
VOLUME /app/uploads
COPY --from=build /app/target/church-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Run app with production profile active
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
