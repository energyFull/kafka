# Use an official Gradle image to build the application
FROM gradle:8-jdk17 AS build

ARG SUBPROJECT=deploy-api

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and settings
COPY gradle gradle
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Copy the necessary modules
COPY . .

# Build the specific module
WORKDIR /app/${SUBPROJECT}
RUN ../gradlew build --no-daemon

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

ARG SUBPROJECT=deploy-api
# Copy the built application from the build stage
COPY --from=build /app/${SUBPROJECT}/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]