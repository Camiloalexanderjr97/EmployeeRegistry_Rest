# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk-jammy as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Use a smaller runtime image
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/user_management
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV JWT_SECRET=your_jwt_secret_key_here
ENV JWT_EXPIRATION_MS=86400000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
