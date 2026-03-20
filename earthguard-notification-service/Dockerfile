# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy parent pom and all module poms for dependency resolution
COPY pom.xml .
COPY earthguard-common/pom.xml earthguard-common/
COPY earthguard-earthquake-service/pom.xml earthguard-earthquake-service/
COPY earthguard-notification-service/pom.xml earthguard-notification-service/
COPY earthguard-api-gateway/pom.xml earthguard-api-gateway/

# Download dependencies
RUN mvn dependency:go-offline -B -pl earthguard-notification-service,earthguard-common -am

# Copy source code
COPY earthguard-common/src earthguard-common/src
COPY earthguard-notification-service/src earthguard-notification-service/src

# Build
RUN mvn clean package -DskipTests -pl earthguard-notification-service -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/earthguard-notification-service/target/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/api/notifications/health || exit 1

EXPOSE 8082

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
