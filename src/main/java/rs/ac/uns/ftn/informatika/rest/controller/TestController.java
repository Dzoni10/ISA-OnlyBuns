package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.service.ImageCompressionService;

import java.io.File;
import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private ImageCompressionService imageCompressionService;

    @GetMapping("/test/compress")
    public ResponseEntity<String> testImageCompression() {
        try {
            // Specify an image path or get it dynamically
            File testImage = new File(System.getProperty("user.dir") + "/uploads/images/1731265043657_putinKoala.jpg");
            if (testImage.exists()) {
                imageCompressionService.compressImage(testImage);
                return ResponseEntity.ok("Image compressed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error compressing image: " + e.getMessage());
        }
    }
}
