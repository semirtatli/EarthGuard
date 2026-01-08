# 🌍 EarthGuard - Earthquake Early Warning System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=for-the-badge&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A comprehensive real-time earthquake monitoring and alert system with microservices architecture**

[Features](#-features) • [Architecture](#-architecture) • [Quick Start](#-quick-start) • [API Documentation](#-api-documentation) • [Tech Stack](#-tech-stack)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
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
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 Overview

EarthGuard is a production-ready earthquake early warning system built with modern microservices architecture. It provides real-time earthquake monitoring, intelligent alerting, and location-based notifications to help communities stay informed and safe.

### Key Highlights

- 🌍 **Real-time Data**: Integrates with USGS API for live earthquake data
- 🔔 **Instant Notifications**: WebSocket-based real-time alerts
- 📧 **Multi-channel Alerts**: Email and push notifications
- 🗺️ **Location-based Filtering**: Personalized alerts based on user preferences
- 🔒 **Secure**: JWT authentication with role-based access control
- 🚀 **Scalable**: Microservices architecture with event-driven communication
- 📊 **Comprehensive API**: RESTful APIs with Swagger documentation
- 🐳 **Production-ready**: Docker containerization with health checks

---

## ✨ Features

### Core Functionality
- ✅ Real-time earthquake data synchronization from USGS
- ✅ Automatic data refresh every 5 minutes
- ✅ Advanced search with pagination, sorting, and filtering
- ✅ Location-based radius search (configurable per user)
- ✅ Magnitude-based alert levels (NONE, LOW, MODERATE, HIGH, CRITICAL)
- ✅ Historical earthquake data storage and analysis

### Notification System
- ✅ Real-time WebSocket notifications
- ✅ Email alerts for critical earthquakes
- ✅ User preference management (location, radius, magnitude threshold)
- ✅ Critical-only alert mode
- ✅ Multi-channel notification support

### Technical Features
- ✅ JWT-based authentication and authorization
- ✅ Role-based access control (USER, ADMIN)
- ✅ Redis caching for performance optimization
- ✅ API Gateway with rate limiting
- ✅ Event-driven architecture with Apache Kafka
- ✅ Comprehensive API documentation (Swagger/OpenAPI)
- ✅ Health checks and monitoring
- ✅ Docker containerization

---

## 🏗️ Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                     Client Layer                            │
│  (React Frontend / Mobile App / Third-party Services)       │
└─────────────────┬───────────────────────────────────────────┘
                  │ REST API + WebSocket
                  ▼
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8080)                  │
│  • Rate Limiting • CORS • Routing • Load Balancing          │
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
│ • USGS Sync      │  │ • WebSocket      │
│ • Scheduling     │  │ • Event Consumer │
│ • JWT Auth       │  │                  │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
         │    ┌────────────────┘
         │    │
         ▼    ▼
┌─────────────────────────────────────────────────────────────┐
│                    Apache Kafka                             │
│  Topics: earthquake-events, critical-alerts                 │
└─────────────────────────────────────────────────────────────┘
         │
    ┌────┴────┬────────┐
    ▼         ▼        ▼
┌─────────┐ ┌────┐ ┌──────┐
│PostgreSQL Redis  USGS API│
│         │ │    │ │External│
└─────────┘ └────┘ └──────┘
```

### Microservices Overview

#### 1. **API Gateway** (Port 8080)
- Central entry point for all client requests
- Rate limiting (10 req/sec per IP)
- Request routing and load balancing
- CORS configuration

#### 2. **Earthquake Service** (Port 8081)
- Core business logic
- CRUD operations for earthquake data
- USGS API integration with scheduled sync
- JWT authentication and authorization
- User preference management
- Redis caching

#### 3. **Notification Service** (Port 8082)
- Kafka event consumer
- Email notification service
- WebSocket real-time notifications
- Alert logic based on user preferences

---

## 🛠️ Tech Stack

### Backend
- **Java 21** - Latest LTS version
- **Spring Boot 3.2.1** - Application framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database access
- **Spring Cloud Gateway** - API Gateway
- **Apache Kafka** - Event streaming
- **PostgreSQL** - Primary database
- **Redis** - Caching layer
- **JWT (JJWT)** - Token-based authentication
- **Swagger/OpenAPI** - API documentation
- **Lombok** - Boilerplate reduction
- **JUnit 5 + Mockito** - Testing

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Maven** - Build automation
- **GitHub Actions** - CI/CD (coming soon)

### External APIs
- **USGS Earthquake API** - Real-time earthquake data

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

### Option 1: Docker (Recommended for Production)
```bash
# 1. Clone the repository
git clone https://github.com/yourusername/earthguard.git
cd earthguard

# 2. Configure environment variables
cp .env.example .env
nano .env  # Edit with your settings

# 3. Build Docker images
./build.sh

# 4. Start all services
./start.sh

# 5. Access the application
# API Gateway: http://localhost:8080
# Swagger UI: http://localhost:8081/swagger-ui.html
```

### Option 2: Local Development
```bash
# 1. Clone the repository
git clone https://github.com/yourusername/earthguard.git
cd earthguard

# 2. Start infrastructure services
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

1. **Register a new account**
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

2. **Access Swagger UI** for interactive API testing
```
http://localhost:8081/swagger-ui.html
```

3. **View recent earthquakes** (public endpoint)
```bash
curl http://localhost:8080/api/earthquakes/recent
```

---

## 📁 Project Structure
```
earthguard/
├── earthguard-common/              # Shared entities and DTOs
│   └── src/main/java/
│       └── com/earthguard/common/
│           ├── entity/             # JPA entities (User, Earthquake, etc.)
│           └── enums/              # Shared enums
│
├── earthguard-earthquake-service/  # Main service
│   ├── src/main/java/
│   │   └── com/earthguard/earthquake/
│   │       ├── config/             # Configuration classes
│   │       ├── controller/         # REST controllers
│   │       ├── dto/                # Data Transfer Objects
│   │       ├── exception/          # Custom exceptions
│   │       ├── repository/         # JPA repositories
│   │       ├── security/           # JWT & Security config
│   │       ├── service/            # Business logic
│   │       ├── scheduler/          # Scheduled tasks
│   │       ├── messaging/          # Kafka producers
│   │       └── client/             # External API clients
│   ├── src/main/resources/
│   │   └── application.yml         # Application config
│   └── Dockerfile
│
├── earthguard-notification-service/  # Notification service
│   ├── src/main/java/
│   │   └── com/earthguard/notification/
│   │       ├── config/             # WebSocket & Kafka config
│   │       ├── dto/                # Event DTOs
│   │       ├── messaging/          # Kafka consumers
│   │       └── service/            # Email & WebSocket services
│   └── Dockerfile
│
├── earthguard-api-gateway/         # API Gateway
│   ├── src/main/java/
│   │   └── com/earthguard/gateway/
│   │       ├── config/             # Gateway routes & filters
│   │       └── controller/         # Fallback controllers
│   └── Dockerfile
│
├── docker-compose.yml              # Development setup
├── docker-compose.prod.yml         # Production setup
├── build.sh                        # Build script
├── start.sh                        # Start script
├── stop.sh                         # Stop script
├── .env.example                    # Environment variables template
├── .dockerignore
├── .gitignore
├── pom.xml                         # Parent POM
└── README.md                       # This file
```

---

## 📖 API Documentation

### Swagger UI (Interactive)

Access interactive API documentation:
```
http://localhost:8081/swagger-ui.html
```

### OpenAPI Specification

Download OpenAPI JSON:
```bash
curl http://localhost:8081/api-docs > earthguard-openapi.json
```

### Key Endpoints

#### Authentication
```http
POST /api/auth/register   # Register new user
POST /api/auth/login      # Login and get JWT token
```

#### Earthquakes
```http
GET  /api/earthquakes/recent              # Get recent earthquakes (public)
GET  /api/earthquakes/count               # Get total count (public)
GET  /api/earthquakes/paginated           # Paginated list (auth required)
GET  /api/earthquakes/search              # Advanced search with filters
GET  /api/earthquakes/{id}                # Get by ID
POST /api/earthquakes                     # Create (admin only)
POST /api/earthquakes/sync                # Sync from USGS (admin only)
DELETE /api/earthquakes/{id}              # Delete (admin only)
```

#### User Preferences
```http
GET  /api/preferences     # Get user preferences (auth required)
PUT  /api/preferences     # Update preferences (auth required)
DELETE /api/preferences   # Delete preferences (auth required)
```

#### WebSocket
```
ws://localhost:8080/ws    # WebSocket connection
/topic/earthquakes        # Subscribe to general alerts
/topic/critical-alerts    # Subscribe to critical alerts
```

---

## ⚙️ Configuration

### Environment Variables

Create a `.env` file (copy from `.env.example`):
```bash
# Database
POSTGRES_DB=earthguard
POSTGRES_USER=earthguard
POSTGRES_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your-256-bit-secret-key-change-in-production
JWT_EXPIRATION=86400000  # 24 hours

# Email (Optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
EMAIL_ENABLED=false

# Application
SPRING_PROFILES_ACTIVE=prod
```

### Application Properties

Key configurations in `application.yml`:
```yaml
# Database
spring.datasource.url: jdbc:postgresql://localhost:5432/earthguard

# Redis Cache
spring.data.redis.host: localhost
spring.data.redis.port: 6379

# Kafka
spring.kafka.bootstrap-servers: localhost:9092

# JWT
jwt.secret: your-secret-key
jwt.expiration: 86400000

# Swagger
springdoc.swagger-ui.path: /swagger-ui.html
```

---

## 💻 Development

### Running Tests
```bash
# Run all tests
mvn test

# Run tests for specific module
cd earthguard-earthquake-service
mvn test

# Run with coverage
mvn test jacoco:report
```

### Code Quality
```bash
# Format code
mvn spring-javaformat:apply

# Check style
mvn checkstyle:check
```

### Hot Reload (Spring Boot DevTools)

Add to your `pom.xml`:
```xml

    org.springframework.boot
    spring-boot-devtools
    true

```

---

## 🧪 Testing

### Test Coverage

Current test coverage: **~75%**
```bash
# Generate coverage report
mvn clean test jacoco:report

# View report
open earthguard-earthquake-service/target/site/jacoco/index.html
```

### Test Structure
```
src/test/java/
├── repository/         # Integration tests (@DataJpaTest)
├── service/           # Unit tests (Mockito)
└── controller/        # API tests (MockMvc)
```

### Example Test
```java
@Test
@DisplayName("Should save earthquake and publish to Kafka")
void testSaveEarthquake() {
    // Given
    Earthquake earthquake = createTestEarthquake();
    
    // When
    Earthquake saved = earthquakeService.save(earthquake);
    
    // Then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getAlertLevel()).isEqualTo(AlertLevel.MODERATE);
    verify(eventProducer, times(1)).sendEarthquakeEvent(any(), eq("CREATED"));
}
```

---

## 🚀 Deployment

### Docker Production Deployment
```bash
# 1. Build images
./build.sh

# 2. Configure production environment
nano .env

# 3. Start services
docker-compose -f docker-compose.prod.yml up -d

# 4. Check health
docker-compose -f docker-compose.prod.yml ps

# 5. View logs
docker-compose -f docker-compose.prod.yml logs -f

# 6. Scale services (if needed)
docker-compose -f docker-compose.prod.yml up -d --scale earthquake-service=3
```

### Health Checks

All services include health check endpoints:
```bash
# Earthquake Service
curl http://localhost:8081/actuator/health

# Notification Service
curl http://localhost:8082/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health
```

### Monitoring

View service metrics:
```bash
# Container stats
docker stats

# Service logs
docker-compose -f docker-compose.prod.yml logs -f earthquake-service
```

---

## 📊 Performance

### Caching Strategy

- **Redis TTL**:
    - Recent earthquakes: 2 minutes
    - Individual earthquake: 15 minutes
    - All earthquakes: 10 minutes

### Rate Limiting

- **Public endpoints**: 10 requests/second per IP
- **Authenticated endpoints**: 20 requests/second per user
- **Admin endpoints**: 50 requests/second

### Database Indexing

Optimized queries with indexes on:
- `magnitude`
- `timestamp`
- `latitude, longitude` (composite)
- `username` (unique)
- `email` (unique)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

- **Your Name** - *Initial work* - [GitHub](https://github.com/yourusername)

---

## 🙏 Acknowledgments

- [USGS Earthquake API](https://earthquake.usgs.gov/fdsnws/event/1/) - Real-time earthquake data
- Spring Boot Team - Excellent framework
- Apache Kafka - Reliable event streaming
- Docker - Containerization made easy

---

## 📞 Contact & Support

- **Email**: support@earthguard.com
- **Issues**: [GitHub Issues](https://github.com/yourusername/earthguard/issues)
- **Documentation**: [Wiki](https://github.com/yourusername/earthguard/wiki)

---

<div align="center">

**Built with ❤️ for safer communities**

⭐ Star this repo if you find it helpful!

</div>