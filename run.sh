#!/bin/bash
set -e

# Function to clean up containers on exit
cleanup() {
    echo -e "\nStopping all services..."
    docker compose down
}
# --- CHANGE THIS LINE ---
trap cleanup EXIT
# ------------------------

echo "Building and starting all services..."
docker compose up