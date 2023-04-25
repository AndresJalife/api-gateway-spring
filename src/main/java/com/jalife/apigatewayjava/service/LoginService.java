package com.jalife.apigatewayjava.service;

import com.jalife.apigatewayjava.dto.LoginDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service in charge of login.
 */
@AllArgsConstructor
@Service
@Slf4j
public class LoginService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Login method that returns the credentials if the user exists and the password is correct.
     *
     * @param request http request
     * @param form Login form
     * @return Login and user information.
     */
    public Mono<Map<String, Object>> login(ServerHttpRequest request, LoginDTO form) {
        return userService.getUser(form.getUsername())
                .flatMap(user -> userService.getUserDetails(user)
                        .filter(userDetails -> passwordEncoder.matches(form.getPassword(), userDetails.getPassword()))
                        .flatMap(userDetails -> getLoginCredentials(request,
                                        userDetails)))
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("User not found");
                    return Mono.error(new UsernameNotFoundException("Username not found"));
                }));
    }

    /**
     * Creates the credentials for the user.
     *
     * @param request http request
     * @param userDetails user details
     * @return Login and user information.
     */
    private Mono<Map<String, Object>> getLoginCredentials(ServerHttpRequest request, UserDetails userDetails) {
        List<String> roleClaims = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> credentials = tokenService.createTokens(userDetails.getUsername(),
                request.getPath().toString(),
                roleClaims);
        return Mono.just(credentials);
    }
}
