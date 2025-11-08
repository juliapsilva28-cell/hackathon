# Mind@5C - Wellness Check-in Platform

An anonymous wellness check-in application for 5C (Claremont Colleges) students.

## Project Structure

```
/
├── backend/              # Spring Boot REST API
│   ├── src/
│   └── pom.xml
└── frontend/             # Angular 17 Web Application
    ├── src/
    ├── package.json
    └── angular.json
```

## Backend (Spring Boot)

### Prerequisites
- Java 17+
- Maven 3.6+

### Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Test

### Running the Backend
```bash
cd backend
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### API Endpoints
- `GET /api/stats` - Get wellness statistics
- `POST /api/checkin` - Submit a wellness check-in
- `POST /api/seed` - Seed sample data (demo)

## Frontend (Angular)

### Prerequisites
- Node.js 18+
- npm 9+

### Dependencies
- Angular 17
- RxJS 7.8
- TypeScript 5.2

### Running the Frontend
```bash
cd frontend
npm install
npm start
```

The app will be available at `http://localhost:4200`

### Key Features
- Anonymous wellness check-ins
- Real-time community statistics
- Personalized resource recommendations
- Responsive design
- Admin mode for testing (`?admin=true`)

## API Fix Applied

**Issue Fixed**: The frontend was calling `/api/checkins` but the backend endpoint is `/api/checkin`
**Solution**: Updated `app.component.ts:78` to use the correct endpoint

## Running the Full Stack

1. **Start the Backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Start the Frontend** (in a new terminal):
   ```bash
   cd frontend
   npm install
   npm start
   ```

3. **Access the App**: Open `http://localhost:4200` in your browser

## Testing

- **Seed Data**: Add `?admin=true` to the URL and click "Seed Sample Data"
- **Check-in**: Use the sliders to rate sleep, nutrition, stress, and activity
- **View Stats**: See community averages and pulse score

## License

MIT
