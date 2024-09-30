package com.tinyurl.api.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.token}")
    private String secretToken;

    public String generateToken(String email) {
        Jwts
    }


}
