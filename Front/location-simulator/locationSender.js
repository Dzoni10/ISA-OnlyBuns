const path = require('path');
const fs = require('fs');
const axios = require('axios');

// Konfiguracija
const jsonFilePath =  path.join(__dirname, 'locations.json');; // Putanja do JSON fajla sa lokacijama
const backendEndpoint = 'http://localhost:8080/api/locations'; // Endpoint backend aplikacije
const intervalInMinutes = 2; // Interval u minutima za slanje lokacija

// Učitavanje lokacija iz JSON fajla
let locations = [];
try {
    const data = fs.readFileSync(jsonFilePath, 'utf-8');
    locations = JSON.parse(data);
} catch (error) {
    console.error('Greska pri ucitavanju JSON fajla sa lokacijama:', error.message);
    process.exit(1); // Izlazak iz programa u slučaju greške
}

let currentIndex = 0;

// Funkcija za slanje jedne lokacije
const sendLocation = async () => {
    if (locations.length === 0) {
        console.error('Nema dostupnih lokacija za slanje.');
        return;
    }

    const location = locations[currentIndex];
    try {
        await axios.post(backendEndpoint, location);
        console.log(`Poslata lokacija: ${JSON.stringify(location)}`);
    } catch (error) {
        console.error('Greska pri slanju lokacije:', error.message);
    }

    // Pomeranje indeksa na sledeću lokaciju
    currentIndex = (currentIndex + 1) % locations.length;
};

// Pokretanje cron posla
const interval = intervalInMinutes * 60 * 1000; // Prebacivanje minuta u milisekunde
console.log(`Pokrecem cron job. Lokacije ce se slati svakih ${intervalInMinutes} minuta.`);

sendLocation(); // Odmah po pokretanju šaljemo prvu lokaciju
setInterval(sendLocation, interval);
