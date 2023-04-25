package com.jalife.apigatewayjava.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class TokenService {
    @Value("${spring.security.token.access.expiration.milis}")
    public Integer accessTokenExpiration;

    @Value("${spring.security.token.refresh.expiration.milis}")
    public Integer refreshTokenExpiration;

    @Value("${spring.security.token.expiration.tolerance}")
    public Integer expirationTolerance;

    @Value("${spring.security.algorithm.seceretword}")
    public String securityWord;

    private Algorithm algorithm;


    public Map<String, Object> createTokens(String username, String url, List<String> roleClaims) {
        algorithm = Algorithm.HMAC256(securityWord.getBytes());
        Map<String, Object> accesTokenMap = createAccessTokenMap(username, url, roleClaims);
        Map<String, Object> refreshTokenMap = createRefreshToken(username, url);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", accesTokenMap);
        tokens.put("refresh_token", refreshTokenMap);

        return tokens;
    }

    private Map<String, Object> createAccessTokenMap(String username, String url, List<String> roleClaims) {
        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .withIssuer(url)
                .withClaim("roles", roleClaims)
                .sign(algorithm);

        int accessExpiration = (accessTokenExpiration - expirationTolerance);
        Map<String, Object> accesTokenMap = new HashMap<>();
        accesTokenMap.put("token", accessToken);
        accesTokenMap.put("expiration_timeout", accessExpiration);

        return accesTokenMap;
    }

    private Map<String, Object> createRefreshToken(String username, String url) {
        String refreshToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .withIssuer(url)
                .sign(algorithm);
        Map<String, Object> refreshTokenMap = new HashMap<>();
        refreshTokenMap.put("token", refreshToken);
        refreshTokenMap.put("expiration_timeout", refreshTokenExpiration);

        return refreshTokenMap;
    }
}
