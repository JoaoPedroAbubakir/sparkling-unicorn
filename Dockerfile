# Use an official Maven image to build the application
FROM maven:3.9.8-amazoncorretto-21-al2023 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -Dmaven.test.skip=true

# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/app.jar .

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]