# Testing Guide - Mind@5C

## Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+
- npm 9+
- Network access for downloading dependencies

## Step 1: Start the Backend (Terminal 1)

```bash
cd /home/user/hackathon
mvn spring-boot:run
```

**Expected output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Started Application in 3.456 seconds
Tomcat started on port(s): 8080 (http)
```

**Backend will be running on:** `http://localhost:8080`

---

## Step 2: Test Backend API Endpoints (Terminal 2)

### Test 1: Get Initial Stats
```bash
curl http://localhost:8080/api/stats
```

**Expected Response:**
```json
{
  "count": 0,
  "avgSleep": null,
  "avgNutrition": null,
  "avgStress": null,
  "avgActivity": null,
  "communityPulse": 50
}
```

### Test 2: Seed Sample Data
```bash
curl -X POST http://localhost:8080/api/seed
```

**Expected Response:**
```json
{
  "ok": true,
  "message": "15 check-ins added"
}
```

### Test 3: Get Stats After Seeding
```bash
curl http://localhost:8080/api/stats
```

**Expected Response:**
```json
{
  "count": 15,
  "avgSleep": 6.3,
  "avgNutrition": 6.3,
  "avgStress": 6.1,
  "avgActivity": 5.3,
  "communityPulse": 62
}
```

### Test 4: Submit a Check-in (High Stress Scenario)
```bash
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{
    "sleep": 3,
    "nutrition": 4,
    "stress": 9,
    "activity": 2
  }'
```

**Expected Response:**
```json
{
  "ok": true,
  "message": "Check-in recorded. Here are some resources for you.",
  "resources": [
    {
      "label": "Student Wellness Center",
      "url": "https://students.dartmouth.edu/wellness-center",
      "priority": true,
      "reason": "High stress detected"
    },
    {
      "label": "Dick's House Counseling",
      "url": "https://students.dartmouth.edu/health-service/counseling",
      "priority": true,
      "reason": null
    },
    {
      "label": "Nutrition Counseling",
      "url": "https://students.dartmouth.edu/health-service/nutrition",
      "priority": true,
      "reason": "Poor nutrition detected"
    },
    ...
  ]
}
```

### Test 5: Submit a Healthy Check-in
```bash
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{
    "sleep": 8,
    "nutrition": 9,
    "stress": 3,
    "activity": 7
  }'
```

**Expected Response:**
```json
{
  "ok": true,
  "message": "Check-in recorded. Here are some resources for you.",
  "resources": [
    {
      "label": "Campus Events (Collis)",
      "url": "https://collis.dartmouth.edu/events",
      "priority": false,
      "reason": null
    }
  ]
}
```

---

## Step 3: Start the Frontend (Terminal 3)

```bash
cd /home/user/hackathon/frontend
npm start
```

**Expected output:**
```
** Angular Live Development Server is listening on localhost:4200 **

✔ Compiled successfully.
```

**Frontend will be running on:** `http://localhost:4200`

---

## Step 4: Test Full Stack Integration

### Manual Testing in Browser

1. **Open the app:**
   - Navigate to `http://localhost:4200`
   - You should see the "🧠 Mind@5C" header with a gradient background

2. **Check initial stats:**
   - Should show "Community Pulse: 50 / 100"
   - Should show "Based on 0 anonymous check-ins"
   - Should say "Be the first to check in!"

3. **Seed sample data (admin mode):**
   - Add `?admin=true` to URL: `http://localhost:4200?admin=true`
   - Click "Seed Sample Data (Demo)" button
   - Stats should update automatically

4. **Submit a check-in:**
   - Move the sliders to set values:
     - Sleep quality: 4
     - Healthy eating: 5
     - Stress level: 8
     - Physical activity: 3
   - Click "Submit Check-in"
   - Should see success message: "✓ Check-in recorded. Here are some resources for you."

5. **Verify personalized resources:**
   - Should see "🎯 Recommended For You" section
   - Priority resources (with ⚠️ icon) should appear with pink background
   - Resources should be clickable and open in new tab

6. **Verify stats update:**
   - Community Pulse should recalculate
   - Check-in count should increment
   - Average values should update

### Expected UI Behavior

**Sliders:**
- Range from 1-10
- Display current value on the right
- Update in real-time as you drag
- Disabled during submission

**Submit Button:**
- Changes to "Submitting..." during API call
- Disabled during submission
- Shows success/error messages below

**Stats Panel:**
- Updates automatically after check-in
- Shows averages rounded to 1 decimal place
- Community Pulse calculates: `(sleep + nutrition + (10-stress) + activity) / 4 * 10`
- Empty state shows "Be the first to check in!" when count is 0

**Resources:**
- Priority resources have pink background and ⚠️ icon
- Show reason in italics (e.g., "High stress detected")
- Clickable to open in new tab

---

## Step 5: Advanced Testing Scenarios

### Scenario 1: Test Stress Detection
```bash
# Submit high stress check-in
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{"sleep": 7, "nutrition": 7, "stress": 9, "activity": 6}'
```
**Expected:** Should receive Student Wellness Center and Dick's House resources marked as priority

### Scenario 2: Test Poor Sleep Detection
```bash
# Submit poor sleep check-in
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{"sleep": 3, "nutrition": 7, "stress": 5, "activity": 6}'
```
**Expected:** Should receive Student Wellness Center with reason "Poor sleep detected"

### Scenario 3: Test Poor Nutrition Detection
```bash
# Submit poor nutrition check-in
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{"sleep": 7, "nutrition": 4, "stress": 5, "activity": 6}'
```
**Expected:** Should receive Nutrition Counseling and FOCO/DDS resources

### Scenario 4: Test Low Activity Detection
```bash
# Submit low activity check-in
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{"sleep": 7, "nutrition": 7, "stress": 5, "activity": 3}'
```
**Expected:** Should receive Alumni Gym and DOC Outdoor Trips resources

### Scenario 5: Test Perfect Health
```bash
# Submit perfect check-in
curl -X POST http://localhost:8080/api/checkin \
  -H "Content-Type: application/json" \
  -d '{"sleep": 10, "nutrition": 10, "stress": 1, "activity": 10}'
```
**Expected:** Should only receive general Campus Events resource (no priority resources)

---

## Step 6: Test Mobile Responsive Design

1. Open browser DevTools (F12)
2. Toggle device toolbar (Ctrl+Shift+M or Cmd+Shift+M)
3. Select mobile device (e.g., iPhone 12)
4. Verify:
   - Header resizes appropriately
   - Sliders stack vertically
   - Stats grid becomes single column
   - All elements remain accessible

---

## Troubleshooting

### Backend won't start
```bash
# Check if port 8080 is already in use
lsof -i :8080

# Kill process using port 8080
kill -9 <PID>

# Check Java version
java -version  # Should be 17+

# Clean and rebuild
mvn clean install
```

### Frontend won't start
```bash
# Check if port 4200 is already in use
lsof -i :4200

# Kill process using port 4200
kill -9 <PID>

# Check Node version
node --version  # Should be 18+

# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### CORS errors in browser
The backend is configured with `@CrossOrigin(origins = "*")` so this shouldn't happen. If it does:
- Verify backend is running on port 8080
- Check browser console for exact error
- Ensure frontend is calling `http://localhost:8080/api/*`

### API connection errors
- Verify backend URL in `app.component.ts` is `http://localhost:8080/api`
- Check browser console Network tab for failed requests
- Verify both servers are running
- Try the curl commands to test backend directly

---

## Expected Timeline for Full Test

- Backend startup: ~10-20 seconds
- Frontend startup: ~5-10 seconds
- Seed data: <1 second
- Submit check-in: <1 second
- Stats update: <1 second

Total time to fully test: **~5 minutes**

---

## Success Criteria

✅ Backend starts without errors on port 8080
✅ All 5 API endpoints respond correctly
✅ Frontend starts without errors on port 4200
✅ Initial stats load (count: 0, pulse: 50)
✅ Seed data creates 15 check-ins
✅ Stats update to show averages
✅ Check-in form submits successfully
✅ Personalized resources appear based on thresholds
✅ Priority resources are highlighted
✅ Success message displays and auto-fades
✅ Stats refresh automatically after check-in
✅ Mobile responsive design works
✅ All resource links are clickable

---

## Next Steps

Once all tests pass:
1. Deploy backend to production server
2. Update frontend API URL to production endpoint
3. Build frontend for production: `npm run build`
4. Deploy frontend to hosting service
5. Set up monitoring and analytics
