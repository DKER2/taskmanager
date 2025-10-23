set -e

# Function to clean up containers on exit
cleanup() {
    echo -e "\nStopping all services..."
    docker compose down
}
trap cleanup SIGINT

echo "Building and starting all services..."

docker compose up --build -d

echo "-----------------------------------------------"
echo "Services are up and running."
echo "API will be available at http://localhost:8080"
echo "Following logs (press Ctrl+C to stop following and shut down services):"
echo "-----------------------------------------------"

docker compose logs -f
