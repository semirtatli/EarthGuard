# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy parent pom and all module poms for dependency resolution
COPY pom.xml .
COPY earthguard-common/pom.xml earthguard-common/
COPY earthguard-earthquake-service/pom.xml earthguard-earthquake-service/
COPY earthguard-notification-service/pom.xml earthguard-notification-service/
COPY earthguard-api-gateway/pom.xml earthguard-api-gateway/

RUN mvn dependency:go-offline -B -pl earthguard-api-gateway -am

COPY earthguard-api-gateway/src earthguard-api-gateway/src

RUN mvn clean package -DskipTests -pl earthguard-api-gateway -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/earthguard-api-gateway/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/earthquakes/count || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
