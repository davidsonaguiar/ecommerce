package davidson.com.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import davidson.com.ecommerce.resources.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.jwt.secret}")
    private String SECRET;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(getExpiresAt())
                    .sign(algorithm);
        }
        catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token");
        }
    }


    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String email = JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("email")
                    .asString();
            return email;
        }
        catch (Exception exception) {
            return null;
        }
    }


    private Instant getExpiresAt() {
        Integer expirationTime = 60 * 60 * 24;
        return Instant.now().plusSeconds(expirationTime);
    }
}
