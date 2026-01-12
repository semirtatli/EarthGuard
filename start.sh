#!/bin/bash

echo "🚀 Starting EarthGuard services..."

# Check if .env exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Creating from template..."
    cp .env.example .env
    echo "📝 Please edit .env file with your configuration"
    exit 1
fi

# Start services
docker-compose -f docker-compose.prod.yml up -d

echo ""
echo "✅ Services started!"
echo ""
echo "📊 Service URLs:"
echo "  - API Gateway:        http://localhost:8080"
echo "  - Earthquake Service: http://localhost:8081"
echo "  - Notification Service: http://localhost:8082"
echo "  - Swagger UI:         http://localhost:8081/swagger-ui.html"
echo ""
echo "📋 View logs:"
echo "  docker-compose -f docker-compose.prod.yml logs -f"
echo ""
echo "🛑 Stop services:"
echo "  docker-compose -f docker-compose.prod.yml down"