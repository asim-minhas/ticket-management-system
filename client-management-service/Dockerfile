# Use lightweight OpenJDK base image
FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy built JAR into container
COPY target/client-management-service.jar app.jar

# Expose application port (matches Compose port mapping)
EXPOSE 9005

# Run JAR on container start
ENTRYPOINT ["java", "-jar", "app.jar"]
