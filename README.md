# 🌍 EarthGuard - Earthquake Early Warning System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=for-the-badge&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A learning-focused earthquake monitoring system built with modern microservices architecture**

[Features](#-features) • [Architecture](#-architecture) • [Quick Start](#-quick-start) • [API Documentation](#-api-documentation) • [Tech Stack](#-tech-stack)

</div>

---

## 📋 Table of Contents

- [About This Project](#-about-this-project)
- [Features](#-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Configuration](#-configuration)
- [Development](#-development)
- [Testing](#-testing)
- [Deployment](#-deployment)

---

## 🎓 About This Project

EarthGuard is a **learning project** developed to understand and implement advanced backend concepts that are commonly used in modern enterprise applications. This project serves as a hands-on learning experience for:

### 🎯 Learning Objectives

- **Microservices Architecture**: Understanding service decomposition, inter-service communication, and independent deployment
- **API Gateway Pattern**: Centralized routing, rate limiting, and cross-cutting concerns
- **Event-Driven Architecture**: Asynchronous communication using Apache Kafka
- **Caching Strategies**: Redis implementation for performance optimization
- **Authentication & Authorization**: JWT-based security with role-based access control
- **Real-time Communication**: WebSocket implementation for live updates
- **Docker & Containerization**: Multi-stage builds, docker-compose orchestration
- **RESTful API Design**: Pagination, filtering, sorting, and comprehensive documentation
- **Database Design**: JPA/Hibernate, entity relationships, and query optimization
- **External API Integration**: Working with third-party APIs (USGS)
- **Scheduled Tasks**: Background jobs and data synchronization

### 💡 Purpose

This project was built to **bridge the knowledge gap** between theoretical understanding and practical implementation of concepts like:
- Microservices
- API Gateway
- Rate Limiting
- Apache Kafka
- Redis Caching
- JWT Authentication
- WebSocket
- Docker Multi-stage Builds

Rather than just reading about these technologies, this project implements them in a real-world scenario to gain practical experience.

---

## ✨ Features

### Core Functionality
- ✅ Real-time earthquake data synchronization from USGS API
- ✅ Automatic data refresh every 5 minutes via scheduled tasks
- ✅ Advanced search with pagination, sorting, and dynamic filtering
- ✅ Magnitude-based alert levels (NONE, LOW, MODERATE, HIGH, CRITICAL)
- ✅ Historical earthquake data storage and analysis
- ✅ User preference management (location coordinates, radius, magnitude threshold)

### Notification System
- ✅ Real-time WebSocket notifications
- ✅ Email alerts for critical earthquakes (configurable)
- ✅ Kafka-based event-driven alert system
- ✅ Customizable notification preferences per user

### Technical Features
- ✅ JWT-based authentication and authorization
- ✅ Role-based access control (USER, ADMIN)
- ✅ Redis caching with configurable TTL for performance optimization
- ✅ API Gateway with centralized routing and rate limiting
- ✅ Event-driven architecture with Apache Kafka
- ✅ Comprehensive API documentation (Swagger/OpenAPI 3.0)
- ✅ Health checks and container monitoring
- ✅ Docker multi-stage builds for optimized images
- ✅ Multi-module Maven project structure
- ✅ Exception handling with global error responses
- ✅ Input validation with Bean Validation API

---

## 🏗️ Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                     Client Layer                            │
│         (React Frontend / Swagger UI / cURL)                │
└─────────────────┬───────────────────────────────────────────┘
                  │ REST API + WebSocket
                  ▼
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8080)                  │
│  • Centralized Routing  • Rate Limiting  • CORS             │
└─────────────────┬───────────────────────────────────────────┘
                  │
        ┌─────────┴─────────┐
        │                   │
        ▼                   ▼
┌──────────────────┐  ┌──────────────────┐
│ Earthquake       │  │ Notification     │
│ Service          │  │ Service          │
│ (Port 8081)      │  │ (Port 8082)      │
│                  │  │                  │
│ • CRUD Ops       │  │ • Email Service  │
│ • USGS API Sync  │  │ • WebSocket      │
│ • Scheduling     │  │ • Kafka Consumer │
│ • JWT Auth       │  │                  │
│ • Redis Cache    │  │                  │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
         │    ┌────────────────┘
         │    │ Event Publishing
         ▼    ▼
┌─────────────────────────────────────────────────────────────┐
│                    Apache Kafka                             │
│  Topics: earthquake-events, critical-alerts                 │
└─────────────────────────────────────────────────────────────┘
         │
    ┌────┴────┬────────┐
    ▼         ▼        ▼
┌─────────┐ ┌────┐ ┌──────────┐
│PostgreSQL Redis  USGS API   │
│         │ │    │ │ (External)│
└─────────┘ └────┘ └──────────┘
```

### Microservices Overview

#### 1. **API Gateway** (Port 8080)
- **Purpose**: Central entry point for all client requests
- **Features**:
    - Request routing to appropriate microservices
    - Rate limiting (10 req/sec for public, 20 req/sec for authenticated users)
    - CORS configuration
    - Circuit breaker pattern (optional)

#### 2. **Earthquake Service** (Port 8081)
- **Purpose**: Core business logic and data management
- **Features**:
    - RESTful CRUD operations for earthquake data
    - Integration with USGS API for real-time data
    - Scheduled task for automatic synchronization (every 5 minutes)
    - JWT authentication and authorization
    - User and preference management
    - Redis caching for frequently accessed data
    - Kafka event producer for earthquake alerts
    - Swagger/OpenAPI documentation

#### 3. **Notification Service** (Port 8082)
- **Purpose**: Handle all notification channels
- **Features**:
    - Kafka event consumer
    - Email notification service
    - WebSocket server for real-time browser notifications
    - User preference-based alert filtering

---

## 🛠️ Tech Stack

### Backend Technologies
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.2.1** - Application framework
- **Spring Security + JWT** - Authentication & Authorization
- **Spring Data JPA + Hibernate** - ORM and database access
- **Spring Cloud Gateway** - API Gateway implementation
- **Spring Kafka** - Event streaming integration
- **Spring WebSocket** - Real-time communication
- **Spring Cache + Redis** - Caching layer
- **PostgreSQL 16** - Relational database
- **Redis 7** - In-memory data store
- **Apache Kafka 7.5** - Event streaming platform
- **Swagger/OpenAPI 3.0** - API documentation
- **Lombok** - Reduce boilerplate code
- **Bean Validation API** - Input validation
- **JUnit 5 + Mockito** - Unit and integration testing

### DevOps & Tools
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Maven 3.9** - Build automation and dependency management
- **Multi-stage Dockerfiles** - Optimized container images

### External APIs
- **USGS Earthquake API** - Real-time earthquake data source

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **Docker** & **Docker Compose** ([Download](https://www.docker.com/products/docker-desktop))
- **Git** ([Download](https://git-scm.com/downloads))

### Verify Installation
```bash
java -version   # Should show Java 21
mvn -version    # Should show Maven 3.9+
docker --version
docker-compose --version
```

---

## 🚀 Quick Start

### Option 1: Docker (Recommended)
```bash
# 1. Clone the repository
git clone https://github.com/yourusername/earthguard.git
cd earthguard

# 2. Configure environment variables
cp .env.example .env
nano .env  # Edit with your settings (optional for development)

# 3. Build Docker images
./build.sh

# 4. Start all services
./start.sh

# 5. Access the application
# API Gateway:    http://localhost:8080
# Swagger UI:     http://localhost:8081/swagger-ui.html
# WebSocket Test: ws://localhost:8082/ws
```

### Option 2: Local Development
```bash
# 1. Clone the repository
git clone https://github.com/yourusername/earthguard.git
cd earthguard

# 2. Start infrastructure services only
docker-compose up -d postgres redis kafka zookeeper

# 3. Build the project
mvn clean install

# 4. Start Earthquake Service (Terminal 1)
cd earthguard-earthquake-service
mvn spring-boot:run

# 5. Start Notification Service (Terminal 2)
cd earthguard-notification-service
mvn spring-boot:run

# 6. Start API Gateway (Terminal 3)
cd earthguard-api-gateway
mvn spring-boot:run
```

### First Steps

#### 1. Register a new account
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

#### 2. Access Swagger UI

Open interactive API documentation:
```
http://localhost:8081/swagger-ui.html
```

Click **"Authorize"** button and enter your JWT token.

#### 3. View recent earthquakes (public endpoint)
```bash
curl http://localhost:8080/api/earthquakes/recent
```

---

## 📁 Project Structure
```
earthguard/
├── earthguard-common/                    # Shared module
│   └── src/main/java/
│       └── com/earthguard/common/
│           ├── entity/                   # JPA Entities
│           │   ├── User.java
│           │   ├── UserPreference.java
│           │   └── Earthquake.java
│           └── enums/
│               ├── Role.java
│               └── AlertLevel.java
│
├── earthguard-earthquake-service/        # Main business service
│   ├── src/main/java/
│   │   └── com/earthguard/earthquake/
│   │       ├── config/                   # Configurations
│   │       │   ├── SecurityConfig.java
│   │       │   ├── RedisConfig.java
│   │       │   ├── OpenApiConfig.java
│   │       │   └── CorsConfig.java
│   │       ├── controller/               # REST Controllers
│   │       │   ├── EarthquakeController.java
│   │       │   ├── UserPreferenceController.java
│   │       │   └── auth/
│   │       │       └── AuthController.java
│   │       ├── dto/                      # Data Transfer Objects
│   │       │   ├── EarthquakeRequest.java
│   │       │   ├── EarthquakeResponse.java
│   │       │   ├── PageResponse.java
│   │       │   ├── auth/
│   │       │   ├── filter/
│   │       │   └── preference/
│   │       ├── exception/                # Exception Handling
│   │       │   ├── ResourceNotFoundException.java
│   │       │   ├── InvalidRequestException.java
│   │       │   ├── ErrorResponse.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── repository/               # JPA Repositories
│   │       │   ├── EarthquakeRepository.java
│   │       │   ├── UserRepository.java
│   │       │   └── UserPreferenceRepository.java
│   │       ├── security/                 # Security Components
│   │       │   ├── JwtUtil.java
│   │       │   ├── JwtAuthenticationFilter.java
│   │       │   └── CustomUserDetailsService.java
│   │       ├── service/                  # Business Logic
│   │       │   ├── EarthquakeService.java
│   │       │   ├── EarthquakeServiceImpl.java
│   │       │   ├── EarthquakeSyncService.java
│   │       │   ├── UserPreferenceService.java
│   │       │   └── auth/
│   │       │       └── AuthService.java
│   │       ├── scheduler/                # Scheduled Tasks
│   │       │   └── EarthquakeScheduler.java
│   │       ├── messaging/                # Kafka Producers
│   │       │   └── EarthquakeEventProducer.java
│   │       ├── specification/            # JPA Specifications
│   │       │   └── EarthquakeSpecification.java
│   │       └── client/                   # External API Clients
│   │           ├── UsgsEarthquakeClient.java
│   │           ├── UsgsEarthquakeResponse.java
│   │           └── UsgsMapper.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── src/test/java/                    # Tests
│   │   └── com/earthguard/earthquake/
│   │       ├── repository/
│   │       └── service/
│   ├── Dockerfile
│   └── pom.xml
│
├── earthguard-notification-service/      # Notification service
│   ├── src/main/java/
│   │   └── com/earthguard/notification/
│   │       ├── config/
│   │       │   ├── KafkaConsumerConfig.java
│   │       │   └── WebSocketConfig.java
│   │       ├── controller/
│   │       │   └── NotificationTestController.java
│   │       ├── dto/
│   │       │   └── EarthquakeEvent.java
│   │       ├── messaging/                # Kafka Consumers
│   │       │   └── EarthquakeEventListener.java
│   │       └── service/
│   │           ├── NotificationService.java
│   │           ├── NotificationServiceImpl.java
│   │           ├── email/
│   │           │   ├── EmailService.java
│   │           │   ├── EmailServiceImpl.java
│   │           │   └── EmailTemplate.java
│   │           └── websocket/
│   │               ├── WebSocketService.java
│   │               └── WebSocketMessage.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
│
├── earthguard-api-gateway/               # API Gateway
│   ├── src/main/java/
│   │   └── com/earthguard/gateway/
│   │       ├── config/
│   │       │   ├── RateLimitConfig.java
│   │       │   └── GatewayConfig.java
│   │       └── controller/
│   │           └── FallbackController.java
│   ├── src/main/resources/
│   │   └── application.yml               # Routes & Filters
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml                    # Development setup
├── docker-compose.prod.yml               # Production setup
├── build.sh                              # Build Docker images
├── start.sh                              # Start all services
├── stop.sh                               # Stop all services
├── .env.example                          # Environment template
├── .dockerignore
├── .gitignore
├── pom.xml                               # Parent POM
└── README.md
```

---

## 📖 API Documentation

### Swagger UI (Interactive)

Access the interactive API documentation and test endpoints:
```
http://localhost:8081/swagger-ui.html
```

**Features:**
- Browse all available endpoints
- View request/response schemas
- Test APIs directly from browser
- Authenticate with JWT token

### OpenAPI Specification

Download the OpenAPI 3.0 JSON specification:
```bash
curl http://localhost:8081/api-docs > earthguard-openapi.json
```

### Key API Endpoints

#### 🔐 Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |

#### 🌍 Earthquakes

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/earthquakes/recent` | Get 10 most recent earthquakes | No |
| GET | `/api/earthquakes/count` | Get total earthquake count | No |
| GET | `/api/earthquakes/paginated` | Get paginated list with sorting | Yes |
| GET | `/api/earthquakes/search` | Advanced search with filters | Yes |
| GET | `/api/earthquakes/{id}` | Get earthquake by ID | No |
| GET | `/api/earthquakes/magnitude/{min}` | Filter by minimum magnitude | No |
| GET | `/api/earthquakes/alert-level/{level}` | Filter by alert level | No |
| POST | `/api/earthquakes` | Create earthquake (admin) | Yes (Admin) |
| POST | `/api/earthquakes/sync` | Sync from USGS API (admin) | Yes (Admin) |
| DELETE | `/api/earthquakes/{id}` | Delete earthquake (admin) | Yes (Admin) |

#### ⚙️ User Preferences

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/preferences` | Get current user preferences | Yes |
| PUT | `/api/preferences` | Update preferences | Yes |
| DELETE | `/api/preferences` | Reset preferences | Yes |

#### 🔌 WebSocket

Connect to WebSocket for real-time notifications:
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    // Subscribe to earthquake alerts
    stompClient.subscribe('/topic/earthquakes', function(message) {
        console.log('New earthquake:', JSON.parse(message.body));
    });
    
    // Subscribe to critical alerts
    stompClient.subscribe('/topic/critical-alerts', function(message) {
        console.log('Critical alert:', JSON.parse(message.body));
    });
});
```

---

## ⚙️ Configuration

### Environment Variables

Create a `.env` file from the template:
```bash
cp .env.example .env
```

**Required Variables:**
```bash
# Database Configuration
POSTGRES_DB=earthguard
POSTGRES_USER=earthguard
POSTGRES_PASSWORD=your_secure_password_here

# JWT Configuration
JWT_SECRET=your-256-bit-secret-key-change-in-production-must-be-long-enough
JWT_EXPIRATION=86400000  # 24 hours in milliseconds

# Email Configuration (Optional - for notifications)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
EMAIL_ENABLED=false  # Set to true to enable email notifications

# Application Profile
SPRING_PROFILES_ACTIVE=prod
```

### Application Configuration

Key settings in `application.yml`:

#### Earthquake Service (`earthguard-earthquake-service/src/main/resources/application.yml`)
```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/earthguard
    username: earthguard
    password: earthguard123
  
  jpa:
    hibernate:
      ddl-auto: update  # Creates/updates tables automatically
    show-sql: true
  
  data:
    redis:
      host: localhost
      port: 6379
  
  kafka:
    bootstrap-servers: localhost:9092
  
  cache:
    type: redis

jwt:
  secret: your-secret-key
  expiration: 86400000

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

#### API Gateway (`earthguard-api-gateway/src/main/resources/application.yml`)
```yaml
server:
  port: 8080

spring:
  cloud:
    gateway:
      routes:
        - id: earthquake-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/earthquakes/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter:
                  replenishRate: 10    # Requests per second
                  burstCapacity: 20    # Max burst size
```

---

## 💻 Development

### Local Development Setup
```bash
# 1. Start infrastructure
docker-compose up -d postgres redis kafka zookeeper

# 2. Verify services are running
docker-compose ps

# 3. Build project
mvn clean install

# 4. Run services individually
cd earthguard-earthquake-service && mvn spring-boot:run
cd earthguard-notification-service && mvn spring-boot:run
cd earthguard-api-gateway && mvn spring-boot:run
```

### Hot Reload Development

For faster development iteration, add Spring Boot DevTools:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 🧪 Testing

### Run All Tests
```bash
# Run all tests
mvn test

# Run tests for specific module
cd earthguard-earthquake-service
mvn test

# Run with coverage report
mvn clean test jacoco:report
```

### Test Coverage

Current test coverage: **~75%**

View coverage report:
```bash
open earthguard-earthquake-service/target/site/jacoco/index.html
```

### Test Structure
```
src/test/java/
├── repository/           # Integration tests (@DataJpaTest)
│   └── EarthquakeRepositoryTest.java
└── service/             # Unit tests (Mockito)
    └── EarthquakeServiceImplTest.java
```

### Example Test
```java
@Test
@DisplayName("Should find earthquakes by magnitude greater than or equal")
void testFindByMagnitudeGreaterThanEqual() {
    // Given
    repository.save(createEarthquake(5.5));
    repository.save(createEarthquake(6.5));
    repository.save(createEarthquake(3.5));
    
    // When
    List<Earthquake> result = repository.findByMagnitudeGreaterThanEqual(5.0);
    
    // Then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(Earthquake::getMagnitude)
        .containsExactlyInAnyOrder(5.5, 6.5);
}
```

---

## 🚀 Deployment

### Production Deployment with Docker
```bash
# 1. Configure production environment
cp .env.example .env
nano .env  # Set production values

# 2. Build images
./build.sh

# 3. Start all services
docker-compose -f docker-compose.prod.yml up -d

# 4. Verify all services are healthy
docker-compose -f docker-compose.prod.yml ps

# 5. View logs
docker-compose -f docker-compose.prod.yml logs -f

# 6. Stop services
docker-compose -f docker-compose.prod.yml down
```

### Health Checks

All services include health check endpoints:
```bash
# Earthquake Service
curl http://localhost:8081/api/earthquakes/count

# Notification Service  
curl http://localhost:8082/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health
```

### Service Management
```bash
# View running containers
docker-compose -f docker-compose.prod.yml ps

# View logs for specific service
docker-compose -f docker-compose.prod.yml logs -f earthquake-service

# Restart a service
docker-compose -f docker-compose.prod.yml restart earthquake-service

# View resource usage
docker stats
```

---

## 📊 Performance Optimization

### Caching Strategy (Redis)

| Cache Name | TTL | Purpose |
|------------|-----|---------|
| `earthquakeById` | 15 min | Individual earthquake lookups |
| `earthquakes` | 10 min | Full earthquake list |
| `recentEarthquakes` | 2 min | Recent earthquake queries |

### Rate Limiting

| Endpoint Type | Limit | Scope |
|--------------|-------|-------|
| Public endpoints | 10 req/sec | Per IP |
| Authenticated endpoints | 20 req/sec | Per user |
| Admin endpoints | Unlimited | - |

### Database Indexing

Optimized queries with indexes on:
- `magnitude` - For filtering by earthquake strength
- `timestamp` - For chronological sorting
- `latitude, longitude` (composite) - For location-based queries
- `username` (unique) - For user lookups
- `email` (unique) - For authentication

### JVM Optimization

Container-optimized JVM settings:
```bash
JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

---

## 🎓 Key Learning Concepts Implemented

This project demonstrates practical implementation of:

### 1. **Microservices Architecture**
- Service decomposition (Earthquake Service, Notification Service, API Gateway)
- Independent deployment and scaling
- Service-to-service communication

### 2. **API Gateway Pattern**
- Centralized routing
- Rate limiting per IP/user
- CORS configuration
- Single entry point for clients

### 3. **Event-Driven Architecture**
- Kafka producers and consumers
- Asynchronous event processing
- Topic-based message routing
- Event sourcing pattern

### 4. **Caching Strategy**
- Redis integration
- TTL-based cache invalidation
- Cache-aside pattern
- Performance optimization

### 5. **Authentication & Authorization**
- JWT token generation and validation
- Role-based access control (RBAC)
- Stateless authentication
- Security filter chains

### 6. **Real-time Communication**
- WebSocket implementation
- STOMP protocol
- Topic-based broadcasting
- Real-time event notifications

### 7. **External API Integration**
- REST client implementation
- Scheduled data synchronization
- Error handling and retry logic
- Data transformation and mapping

### 8. **Database Design**
- Entity relationship modeling
- JPA specifications for dynamic queries
- Database indexing for performance
- Transaction management

### 9. **Docker & Containerization**
- Multi-stage Dockerfiles
- Docker Compose orchestration
- Volume persistence
- Health checks and monitoring

### 10. **API Design Best Practices**
- RESTful conventions
- Pagination and sorting
- Filtering with specifications
- OpenAPI/Swagger documentation
- Versioning strategy

---

<div align="center">

**Built as a learning project to master modern backend technologies** 🎓


</div>