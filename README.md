# Earthquake Monitoring System

A **microservice-based earthquake monitoring system** developed as a learning project to understand modern backend architectures such as **microservices**, **event-driven communication**, and **real-time data delivery**.

The system periodically consumes real earthquake data, processes it asynchronously, and delivers live notifications to clients via WebSocket.

---

## Project Status

🚧 **Under active development**
Core backend architecture, data ingestion, persistence, and inter-service communication are completed. Notification delivery mechanisms (email/SMS) are being finalized as part of the learning process.

---

## High-Level Architecture

```
React Frontend
   |
   | REST API / WebSocket
   v
API Gateway (Spring Boot)
(Authentication, Rate Limiting, Routing)
   |
   |-------------------------------|
   |                               |
Earthquake Service         Notification Service
(Spring Boot)              (Spring Boot)
   |                               |
   |----------- Kafka -------------|
               (Event Broker)

PostgreSQL   Redis   Email(Mock/SMTP)
```

The architecture follows a **learning-scale distributed system design**, focusing on clear separation of responsibilities and asynchronous communication between services.

---

## Core Components

### API Gateway

* Single entry point for frontend clients
* JWT-based authentication
* Rate limiting using Bucket4j
* Request routing to backend services

### Earthquake Service

* Periodically consumes data from **USGS Earthquake API**
* Persists earthquake data using **Spring Data JPA & PostgreSQL**
* Publishes earthquake events to Kafka
* Exposes REST endpoints for querying earthquake data

### Notification Service

* Consumes earthquake events from Kafka
* Applies alert logic (e.g., magnitude threshold)
* Sends real-time notifications via WebSocket
* Email delivery supported via Mock or SMTP (in progress)

---

## Event-Driven Communication

The system uses **Apache Kafka** to decouple services:

* Earthquake Service acts as a **producer**
* Notification Service acts as a **consumer**
* Enables asynchronous processing and loose coupling

This design allows services to evolve independently and improves system resilience.

---

## Caching & Performance

* **Redis** is used to cache frequently accessed earthquake queries
* Cache TTL configured via Spring Cache abstraction
* Reduces database load for read-heavy endpoints

---

## Technology Stack

### Backend

* Java 17+
* Spring Boot 3.x
* Spring Data JPA
* Spring Cloud Stream (Kafka)
* Spring WebSocket
* Spring Cache (Redis)
* Apache Kafka
* PostgreSQL
* Redis
* Bucket4j
* Swagger / OpenAPI
* JUnit 5, Mockito

### Frontend

* React 18
* Axios
* WebSocket Client
* Leaflet / Charts (planned)

### DevOps

* Docker
* Docker Compose

---

## Local Development Setup

### Prerequisites

* Java 17+
* Docker & Docker Compose
* Node.js (for frontend)

### Running the System

```bash
docker-compose up -d
```

* Kafka, PostgreSQL, and Redis will be started automatically
* Backend services can be started locally or containerized

---

## API Documentation

Swagger UI is available after startup:

```
http://localhost:8080/swagger-ui.html
```

---

## Learning Goals

This project was built with the following objectives:

* Understand **microservice-based architecture**
* Learn **event-driven communication** using Kafka
* Explore **real-time communication** with WebSocket
* Apply **API Gateway** patterns
* Gain hands-on experience with **Dockerized distributed systems**

---

## Roadmap

* [x] Core microservice architecture
* [x] Kafka-based event communication
* [x] WebSocket notifications
* [ ] Email/SMS alert delivery
* [ ] Frontend dashboard (maps & charts)

---

## Disclaimer

This project is a **learning-oriented implementation** and is not intended as a production-ready system. Design decisions prioritize clarity and educational value over enterprise-scale optimization.
