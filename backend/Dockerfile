# Use a lightweight OpenJDK 21 image to run the pre-built JAR
FROM eclipse-temurin:21-jre-alpine-3.21

WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose the backend port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
