package top.rrricardo.postletterserver.services;

import io.jsonwebtoken.Claims;
import top.rrricardo.postletterserver.models.User;

public interface JwtService {
    String Header = "Authorization";
    String tokenPrefix = "Bearer ";

    String generateJwtToken(User user);

    Claims parseJwtToken(String token);
}
