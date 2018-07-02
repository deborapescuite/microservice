package com.dpgb.microservice.security;

import com.dpgb.microservice.exception.ExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    static final String SECRET = "cGFzc3dvcmQxMjM0NTY3ODk=";

    private long validityInMilliseconds = 1800000; // 30min

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    @Autowired
    private MyUserDetails myUserDetails;

    public String createToken(String username, String userType) {
        logger.info("Creating token for user: " + username);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", userType);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        logger.info("Getting authentication.");
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUsername(String token) {
        logger.info("Getting user name from token.");
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        logger.info("Resolving token.");
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        logger.info("Validating token.");
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer", ""));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new ExpiredTokenException("Expired or invalid JWT token");

        }
    }

}
