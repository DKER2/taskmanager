#!/bin/bash
set -e

cleanup() {
    echo -e "\nStopping all services..."
    docker compose down
}
trap cleanup EXIT

echo "Building and starting all services..."
docker compose up
