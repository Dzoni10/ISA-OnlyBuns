package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.informatika.rest.service.LoadBalancerService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ForwardingController {

    private final LoadBalancerService loadBalancerService;
    private final RestTemplate restTemplate;

    public ForwardingController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/proxy/**")
    public ResponseEntity<?> proxyGet(HttpServletRequest request) {
        String fullPath = request.getRequestURI(); // npr. "/api/proxy/api/posts/followed/mika123"

        // Ukloni prefiks "/api/proxy/"
        String dynamicPath = fullPath.replaceFirst("/api/proxy/", "");

        int retries = 2;
        for (int i = 0; i < retries; i++) {
            String target = loadBalancerService.getNextInstance();
            String url = target + "/" + dynamicPath;
            try {
                return restTemplate.getForEntity(url, String.class);
            } catch (Exception ex) {
                System.out.println("Instanca " + target + " nije dostupna. Pokušaj sledeće...");
            }
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Sve instance su nedostupne.");
    }

    @PostMapping("/proxy/{path}")
    public ResponseEntity<?> proxyPost(@PathVariable String path, @RequestBody Object body) {
        int retries = 2;
        for (int i = 0; i < retries; i++) {
            String target = loadBalancerService.getNextInstance();
            String url = target + "/" + path;
            try {
                return restTemplate.postForEntity(url, body, String.class);
            } catch (Exception ex) {
                System.out.println("Instanca " + target + " nije dostupna. Pokušaj sledeće...");
            }
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Sve instance su nedostupne.");
    }
}

