# 🌍 EarthGuard — Earthquake Early Warning System

<div align="center">

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**Gerçek zamanlı deprem izleme ve erken uyarı sistemi — modern microservices mimarisiyle inşa edildi**

[Canlı Demo](#-canlı-demo) · [Özellikler](#-özellikler) · [Mimari](#-mimari) · [Teknolojiler](#-teknolojiler) · [Kurulum](#-kurulum)

</div>

---

## 🚀 Canlı Demo

| Katman | Platform | Durum |
|--------|----------|-------|
| Frontend | Vercel | 🟢 Canlı |
| Backend API | Render | 🟢 Canlı |

> Frontend Vercel, backend microservice'ler Render üzerinde deploy edilmiştir.

---

## 📌 Proje Hakkında

EarthGuard, USGS (ABD Jeoloji Kurumu) API'sinden gerçek zamanlı deprem verisi çeken, kullanıcılara anlık bildirim gönderen ve interaktif harita üzerinde görselleştiren tam yığın bir uygulamadır.

Bu proje; microservices, event-driven architecture ve real-time communication gibi enterprise-level backend konseptlerini pratikte uygulamak amacıyla geliştirilmiştir.

---

## ✨ Özellikler

### 🗺️ Gerçek Zamanlı Harita
- Depremleri interaktif Leaflet haritasında gösterme
- Büyüklüğe göre renk kodlu marker'lar (yeşil → sarı → turuncu → kırmızı)
- Marker'a tıklayarak detay görüntüleme

### 🔔 Anlık Bildirim Sistemi
- WebSocket üzerinden canlı deprem uyarıları (sayfa yenilemeye gerek yok)
- Kafka event-driven mimari ile servisler arası asenkron mesajlaşma
- Kritik depremler için e-posta bildirimi (yapılandırılabilir)

### 🔍 Gelişmiş Arama & Filtreleme
- Büyüklük, tarih aralığı, uyarı seviyesi gibi kriterlere göre filtreleme
- Sayfalama ve sıralama desteği
- USGS API ile 5 dakikada bir otomatik veri senkronizasyonu

### 🔐 Kimlik Doğrulama
- JWT tabanlı kayıt / giriş sistemi
- Role-based access control (USER / ADMIN)
- API Gateway üzerinden merkezi kimlik yönetimi

### ⚙️ Kullanıcı Tercihleri
- Konum, yarıçap ve minimum büyüklük eşiği ayarı
- Bildirim kanalı seçimi (e-posta, push, yalnızca kritik)

---

## 🏗️ Mimari

```
┌─────────────────────────────────────────────┐
│              Web Frontend (Vercel)           │
│     Vanilla JS · Leaflet.js · WebSocket      │
└──────────────────────┬──────────────────────┘
                       │ REST + WebSocket
                       ▼
┌─────────────────────────────────────────────┐
│           API Gateway  :8080                 │
│  Routing · Rate Limiting · Circuit Breaker   │
└────────────┬────────────────────────────────┘
             │
     ┌───────┴──────────┐
     ▼                  ▼
┌──────────────┐  ┌──────────────────┐
│  Earthquake  │  │  Notification    │
│  Service     │  │  Service         │
│  :8081       │  │  :8082           │
│              │  │                  │
│ USGS Sync    │  │ Kafka Consumer   │
│ JWT Auth     │  │ WebSocket Server │
│ Redis Cache  │  │ Email Service    │
│ Swagger UI   │  │                  │
└──────┬───────┘  └────────┬─────────┘
       │                   │
       └─────────┬─────────┘
                 ▼
┌─────────────────────────────────────────────┐
│              Apache Kafka                    │
│    Topics: earthquake-events · alerts        │
└──────────┬──────────────────────────────────┘
           │
     ┌─────┴──────┐
     ▼            ▼
┌──────────┐  ┌───────┐
│PostgreSQL│  │ Redis │
└──────────┘  └───────┘
```

---

## 🛠️ Teknolojiler

### Backend
| Teknoloji | Amaç |
|-----------|------|
| Java 21 + Spring Boot 3.2 | Uygulama çatısı |
| Spring Cloud Gateway | API Gateway, rate limiting, circuit breaker |
| Spring Security + JWT | Kimlik doğrulama ve yetkilendirme |
| Spring Data JPA + PostgreSQL | Veri yönetimi ve ORM |
| Apache Kafka | Asenkron event-driven mesajlaşma |
| Redis | Önbellekleme ve rate limiter |
| Spring WebSocket + STOMP | Gerçek zamanlı bildirimler |
| Swagger / OpenAPI 3.0 | API dokümantasyonu |
| USGS Earthquake API | Harici deprem veri kaynağı |

### Frontend
| Teknoloji | Amaç |
|-----------|------|
| Vanilla JavaScript (ES6+) | SPA mantığı, API iletişimi |
| Leaflet.js | İnteraktif harita |
| SockJS + STOMP | WebSocket bağlantısı |
| CSS3 | Responsive tasarım |

### DevOps
| Teknoloji | Amaç |
|-----------|------|
| Docker + Docker Compose | Konteyner orkestrasyonu |
| Multi-stage Dockerfile | Optimize imaj boyutu |
| Render | Backend cloud deployment |
| Vercel | Frontend CDN deployment |

---

## 🎯 Uygulanan Konseptler

Bu projeyle aşağıdaki enterprise-level kavramlar pratik olarak uygulanmıştır:

- **Microservices Architecture** — Bağımsız deploy edilebilen, birbirinden izole servisler
- **API Gateway Pattern** — Merkezi routing, rate limiting, tek giriş noktası
- **Event-Driven Architecture** — Kafka ile asenkron, gevşek bağlı servis iletişimi
- **CQRS / Specification Pattern** — Dinamik sorgu oluşturma
- **JWT Authentication** — Stateless, ölçeklenebilir kimlik doğrulama
- **Redis Caching** — Cache-aside pattern, TTL yönetimi
- **WebSocket / Real-time** — STOMP protokolü ile anlık mesajlaşma
- **Circuit Breaker** — Resilience4j ile hata toleransı
- **External API Integration** — USGS API ile scheduled senkronizasyon
- **Docker Multi-stage Build** — Production-ready konteyner imajları

---

## 📁 Proje Yapısı

```
earthguard/
├── earthguard-frontend/          # Web arayüzü (Vercel)
│   ├── index.html
│   ├── css/style.css
│   └── js/
│       ├── api.js                # REST istemcisi
│       ├── auth.js               # Giriş/Kayıt
│       ├── dashboard.js          # Harita & liste
│       ├── websocket.js          # Gerçek zamanlı bağlantı
│       └── preferences.js        # Kullanıcı tercihleri
│
├── earthguard-api-gateway/       # API Gateway (port 8080)
├── earthguard-earthquake-service/ # Ana iş servisi (port 8081)
├── earthguard-notification-service/ # Bildirim servisi (port 8082)
├── earthguard-common/            # Paylaşılan entity ve enum'lar
│
├── docker-compose.yml            # Geliştirme ortamı
├── docker-compose.prod.yml       # Production ortamı
└── pom.xml                       # Maven parent POM
```

---

## 📖 API Dokümantasyonu

Swagger UI üzerinden tüm endpoint'leri interaktif olarak inceleyebilirsiniz:

```
http://localhost:8081/swagger-ui.html
```

### Temel Endpoint'ler

#### Kimlik Doğrulama
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/auth/register` | Yeni kullanıcı kaydı |
| POST | `/api/auth/login` | Giriş ve JWT token alma |

#### Depremler
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/earthquakes/recent` | Son 10 deprem | Hayır |
| GET | `/api/earthquakes/paginated` | Sayfalı liste | Evet |
| GET | `/api/earthquakes/search` | Gelişmiş filtreleme | Evet |
| POST | `/api/earthquakes/sync` | USGS senkronizasyonu | Admin |

#### Kullanıcı Tercihleri
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/preferences` | Tercihleri getir | Evet |
| PUT | `/api/preferences` | Tercihleri güncelle | Evet |

---

## 💻 Yerel Kurulum

### Gereksinimler
- Docker ve Docker Compose
- Java 21
- Maven 3.9+

### Docker ile başlatma (Önerilen)

```bash
# 1. Repo'yu klonla
git clone https://github.com/semirtatli/EarthGuard.git
cd EarthGuard

# 2. Servisleri başlat
docker-compose up -d

# 3. Uygulamaya eriş
# Frontend:   http://localhost
# Swagger UI: http://localhost:8081/swagger-ui.html
# API:        http://localhost:8080
```

---

<div align="center">

**Modern backend teknolojilerini pratikte öğrenmek amacıyla geliştirilmiştir** 🎓

</div>
