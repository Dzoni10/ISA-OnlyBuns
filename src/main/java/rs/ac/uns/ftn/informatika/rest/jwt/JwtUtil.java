package rs.ac.uns.ftn.informatika.rest.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mili_djole_vijuk";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10)) //traje 10 sati XD
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
}
