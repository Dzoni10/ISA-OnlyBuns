package rs.ac.uns.ftn.informatika.rest.authentification;

import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginRateLimiter  rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().equals("api/users/login")){
            String ip = getClient(request);
            Bucket bucket= rateLimiter.resolveBucket(ip);

            if(bucket.tryConsume(1)){
                return true;
            }
            else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Prekoracili ste dozvoljen broj pokusaja za login, pokusajte ponovo za 5 min");
                return false;
            }
        }
        return true;
    }

    private String getClient(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");

        if(xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
