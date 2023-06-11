package com.jalife.apigatewayjava.service;

import com.jalife.apigatewayjava.model.User;
import com.jalife.apigatewayjava.repository.RoleRepository;
import com.jalife.apigatewayjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Service that handles the user requests
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepository;

    /**
     * Receives a username and returns a User saved in the database
     *
     * @param username Username
     * @return User
     */
    @Transactional(readOnly = true)
    public Mono<User> getUser(String username) {
        return userRepo.findByUsername(username)
                .flatMap(user -> userRepo.findByUsername(username));
    }

    /**
     * Gets the user details from a user.
     *
     * @param user User
     * @return UserDetails
     */
    @Transactional(readOnly = true)
    public Mono<UserDetails> getUserDetails(User user) {
        return Mono.just(user)
                .flatMap(_user -> {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
                    return Mono.just(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities));
                });
    }
}
