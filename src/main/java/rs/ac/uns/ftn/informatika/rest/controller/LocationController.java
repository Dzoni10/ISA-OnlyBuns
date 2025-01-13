package rs.ac.uns.ftn.informatika.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.RabbitMQ.RabbitMQPublisher;
import rs.ac.uns.ftn.informatika.rest.domain.Location;

@RestController
@RequestMapping("/api")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @PostMapping("/locations")
    public ResponseEntity<?> addLocation(@RequestBody Location location) {
        // Ovde bi trebali da radite sa podacima, npr. da ih sačuvate u bazi.
        // Vratite 200 status ako je sve u redu
        logger.info("Primljena lokacija: " + location);

        // Konvertovanje objekta u JSON ili string (ako je potrebno)
//        String locationMessage = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
//
//        // Slanje u RabbitMQ red
//        rabbitMQPublisher.sendMessage(locationMessage);
        rabbitMQPublisher.sendMessage(location);

        return ResponseEntity.ok("Lokacija je uspesno primljena.");
    }
}
