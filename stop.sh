#!/bin/bash

echo "🛑 Stopping EarthGuard services..."

docker-compose -f docker-compose.prod.yml down

echo "✅ Services stopped!"