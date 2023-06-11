package com.jalife.apigatewayjava.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jalife.apigatewayjava.Util.ResponseWritter;
import com.jalife.apigatewayjava.dto.RefreshTokenDTO;
import com.jalife.apigatewayjava.service.TokenService;
import com.jalife.apigatewayjava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TokenController {
    private final UserService userService;
    private final TokenService tokenService;

    @Value("${spring.security.algorithm.seceretword}")
    public String securityWord;

    /**
     * Refreshes a soon-to-expire token.
     * Receives a refresh token and returns a new access token.
     *
     * @return New access token and refresh token.
     */
    @PostMapping("/token/refresh")
    public Mono<Void> refreshToken(ServerHttpRequest request,
                                   ServerHttpResponse response, @RequestBody RefreshTokenDTO tokensForm) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(securityWord.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(tokensForm.getRefreshToken());
            String username = decodedJWT.getSubject();
            return userService.getUser(username)
                    .flatMap(user -> {
                        List<String> roleClaims = new ArrayList<>();
                        roleClaims.add(user.getRole().getName());
                        Map<String, Object> tokens = tokenService.createTokens(username,
                                request.getPath().toString(), roleClaims);
                        response.getHeaders().add("Content-Type", "application/json");
                        return ResponseWritter.write(response, tokens);
                    });
        } catch (Exception exception) {
            Map<String, String> errorResponse = setErrorToResponse(exception, response);
            return ResponseWritter.write(response, errorResponse);
        }
    }

    private Map<String, String> setErrorToResponse(Exception exception, ServerHttpResponse response) {
        log.error("Error logging in: {}", exception.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getMessage());
        response.getHeaders().add("Content-Type", "application/json");
        response.getHeaders().add("error", exception.getMessage());
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return errorResponse;
    }
}

