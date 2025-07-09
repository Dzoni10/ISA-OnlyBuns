package rs.ac.uns.ftn.informatika.rest.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
@Service
public class LoadBalancerService {

    private final List<String> instances = Arrays.asList(
            "http://localhost:8081",
            "http://localhost:8082"
    );

    private int currentIndex = 0;

    public synchronized String getNextInstance() {
        String instance = instances.get(currentIndex);
        currentIndex = (currentIndex + 1) % instances.size();
        return instance;
    }
}

