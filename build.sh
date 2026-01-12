#!/bin/bash

echo "🚀 Building EarthGuard services..."

# Build all services
docker-compose -f docker-compose.prod.yml build --no-cache

echo "✅ Build completed!"
echo ""
echo "To start services, run:"
echo "  docker-compose -f docker-compose.prod.yml up -d"