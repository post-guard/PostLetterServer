package top.rrricardo.postletterserver.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.models.User;
import top.rrricardo.postletterserver.services.JwtService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${user.jwt.lifetime}")
    private long ttl;

    @Value("${user.jwt.issuer}")
    private String issuer;

    private final Logger logger;
    private final Key key;

    public JwtServiceImpl(@Value("${user.jwt.secret}") String secret) {
        logger = LoggerFactory.getLogger(JwtServiceImpl.class);
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateJwtToken(User user) {
        var data = new Date();
        var expireData = new Date(data.getTime() + ttl * 24 * 3600 * 1000);

        var token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getNickname())
                .setIssuer(issuer)
                .setIssuedAt(data)
                .setExpiration(expireData)
                .claim("userId", user.getId())
                .claim("emailAddress", user.getEmailAddress())
                .signWith(key)
                .compact();

        // Jwt令牌前面添加"Bearer "作为标志
        return tokenPrefix + token;
    }

    @Override
    public Claims parseJwtToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(tokenPrefix, ""))
                    .getBody();
        } catch (JwtException e) {
            logger.info("Validate jwt token failed: " + e.getMessage());
            throw e;
        }
    }
}

