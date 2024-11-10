package rs.ac.uns.ftn.informatika.rest.authentification;

import org.springframework.stereotype.Component;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket(){

        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));

        return Bucket.builder()
                .addLimit(limit)
                .build();

    }

    public Bucket resolveBucket(String ip){
        return buckets.computeIfAbsent(ip, key-> createNewBucket());
    }

    public void clearBucket(String ip){
        buckets.remove(ip);
    }
}
