# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy parent pom and all module poms for dependency resolution
COPY pom.xml .
COPY earthguard-common/pom.xml earthguard-common/
COPY earthguard-earthquake-service/pom.xml earthguard-earthquake-service/
COPY earthguard-notification-service/pom.xml earthguard-notification-service/
COPY earthguard-api-gateway/pom.xml earthguard-api-gateway/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B -pl earthguard-earthquake-service,earthguard-common -am

# Copy source code
COPY earthguard-common/src earthguard-common/src
COPY earthguard-earthquake-service/src earthguard-earthquake-service/src

# Build application
RUN mvn clean package -DskipTests -pl earthguard-earthquake-service -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from builder stage
COPY --from=builder /app/earthguard-earthquake-service/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/api/earthquakes/count || exit 1

# Expose port
EXPOSE 8081

# JVM optimization for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
