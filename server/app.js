const express = require('express');
const app = express();
const PORT = 3000;

// Middleware to parse JSON data from your mobile app
app.use(express.json());

// Sample data (temporary in-memory storage)
let emfLogs = [];

// 1. Welcome Route
app.get('/', (req, res) => {
    res.send('EMF Detector Pro API is running!');
});

// 2. GET: Retrieve all EMF logs
app.get('/api/logs', (req, res) => {
    res.json(emfLogs);
});

// 3. POST: Save a new EMF reading from the app
app.post('/api/logs', (req, res) => {
    const newReading = {
        id: emfLogs.length + 1,
        value: req.body.value, // e.g., 45.2 uT
        timestamp: new Date().toISOString()
    };
    emfLogs.push(newReading);
    res.status(201).json(newReading);
});

app.listen(PORT, () => {
    console.log(`Server is running at http://localhost:${PORT}`);
});