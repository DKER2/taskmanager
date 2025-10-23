#!/bin/bash

set -e

echo "Building and starting all services..."

docker compose up --build -d

echo "-----------------------------------------------"
echo "Services are up and running."
echo "API will be available at http://localhost:8080"
echo "Following logs (press Ctrl+C to stop following):"
echo "-----------------------------------------------"

docker compose logs -f