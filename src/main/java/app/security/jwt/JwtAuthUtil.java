package app.security.jwt;

import app.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtAuthUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expired}")
    private int jwtExpired;

    public String generateJwt(Authentication authentication) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return Jwts.builder().setSubject((loggedInUser.getName())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpired))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            throw new JwtAuthenticationException("JWT is expired or invalid");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserNameFromJwt(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }
}
