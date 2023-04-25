package com.jalife.apigatewayjava.controller;

import com.jalife.apigatewayjava.dto.LoginDTO;
import com.jalife.apigatewayjava.exception.UserLoggedInError;
import com.jalife.apigatewayjava.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for users to login and logout.
 */
@AllArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    /**
     * Endpoint for login.
     * Receives a login form with username and password.
     * Returns a JSON object with the credentials.
     *
     * @param request http request
     * @param form    Login form
     * @return Login and user information.
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(ServerHttpRequest request,
                                                           @RequestBody LoginDTO form) {
        return loginService.login(request, form)
                .map(ResponseEntity::ok)
                .onErrorResume(UserLoggedInError.class,
                        userLoggedInError -> Mono.just(ResponseEntity.accepted().body(getLoggedResponse())))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(getErrorResponse())));
    }

    private Map<String, Object> getErrorResponse() {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", "authentication failed");
        return errors;
    }

    private Map<String, Object> getLoggedResponse() {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", "Usuario ya logeado.");
        return errors;
    }
}