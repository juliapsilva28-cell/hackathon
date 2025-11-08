#!/bin/bash

# Mind@5C Backend API Test Script
# Run this after starting the backend with: mvn spring-boot:run

BASE_URL="http://localhost:8080/api"

echo "========================================="
echo "Mind@5C Backend API Test Suite"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

test_api() {
    local test_name=$1
    local method=$2
    local endpoint=$3
    local data=$4

    echo -e "${BLUE}Testing: $test_name${NC}"

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" = "200" ]; then
        echo -e "${GREEN}✓ PASS${NC} (HTTP $http_code)"
        echo "Response: $body"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAIL${NC} (HTTP $http_code)"
        echo "Response: $body"
        ((TESTS_FAILED++))
    fi
    echo ""
}

# Check if backend is running
echo "Checking if backend is running..."
if ! curl -s http://localhost:8080/api/stats > /dev/null; then
    echo -e "${RED}ERROR: Backend is not running!${NC}"
    echo "Please start it with: mvn spring-boot:run"
    exit 1
fi
echo -e "${GREEN}Backend is running!${NC}"
echo ""

# Test 1: Get initial stats
test_api "Get Initial Stats" "GET" "/stats"

# Test 2: Seed sample data
test_api "Seed Sample Data" "POST" "/seed" '{}'

# Test 3: Get stats after seeding
test_api "Get Stats After Seeding" "GET" "/stats"

# Test 4: Submit high stress check-in
test_api "Submit High Stress Check-in" "POST" "/checkin" '{
  "sleep": 3,
  "nutrition": 4,
  "stress": 9,
  "activity": 2
}'

# Test 5: Submit healthy check-in
test_api "Submit Healthy Check-in" "POST" "/checkin" '{
  "sleep": 8,
  "nutrition": 9,
  "stress": 3,
  "activity": 7
}'

# Test 6: Submit poor sleep check-in
test_api "Submit Poor Sleep Check-in" "POST" "/checkin" '{
  "sleep": 3,
  "nutrition": 7,
  "stress": 5,
  "activity": 6
}'

# Test 7: Submit poor nutrition check-in
test_api "Submit Poor Nutrition Check-in" "POST" "/checkin" '{
  "sleep": 7,
  "nutrition": 4,
  "stress": 5,
  "activity": 6
}'

# Test 8: Submit low activity check-in
test_api "Submit Low Activity Check-in" "POST" "/checkin" '{
  "sleep": 7,
  "nutrition": 7,
  "stress": 5,
  "activity": 3
}'

# Test 9: Get final stats
test_api "Get Final Stats" "GET" "/stats"

# Summary
echo "========================================="
echo "Test Summary"
echo "========================================="
echo -e "Tests Passed: ${GREEN}$TESTS_PASSED${NC}"
echo -e "Tests Failed: ${RED}$TESTS_FAILED${NC}"
echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}All tests passed! 🎉${NC}"
    exit 0
else
    echo -e "${RED}Some tests failed. Please check the output above.${NC}"
    exit 1
fi
