#!/bin/bash

echo "======================================="
echo "  EarthGuard - Starting All Services"
echo "======================================="
echo ""

# Build and start everything
echo "[1/2] Building all services (this may take a few minutes on first run)..."
docker compose -f docker-compose.prod.yml build

echo ""
echo "[2/2] Starting all services..."
docker compose -f docker-compose.prod.yml up -d

echo ""
echo "Waiting for services to be ready..."
echo "(This can take 1-2 minutes on first start)"
echo ""

# Wait for earthquake-service health
echo -n "  Earthquake Service: "
for i in $(seq 1 60); do
    if docker inspect --format='{{.State.Health.Status}}' earthguard-earthquake-service 2>/dev/null | grep -q healthy; then
        echo "READY"
        break
    fi
    if [ $i -eq 60 ]; then
        echo "TIMEOUT (check logs with: docker compose -f docker-compose.prod.yml logs earthquake-service)"
    fi
    sleep 3
done

echo ""
echo "======================================="
echo "  EarthGuard is running!"
echo "======================================="
echo ""
echo "  Frontend:    http://localhost"
echo "  API Gateway: http://localhost:8080"
echo "  Swagger UI:  http://localhost:8081/swagger-ui.html"
echo ""
echo "  Default users:"
echo "    admin    / admin123  (ADMIN role)"
echo "    testuser / user123   (USER role)"
echo ""
echo "  Useful commands:"
echo "    Logs:    docker compose -f docker-compose.prod.yml logs -f"
echo "    Stop:    docker compose -f docker-compose.prod.yml down"
echo "    Reset:   docker compose -f docker-compose.prod.yml down -v"
echo "======================================="
