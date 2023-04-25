package com.jalife.apigatewayjava.service;

import com.jalife.apigatewayjava.model.User;
import com.jalife.apigatewayjava.repository.RoleRepository;
import com.jalife.apigatewayjava.repository.UserRepository;
import com.jalife.apigatewayjava.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepo;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Mono<User> getUser(String username) {
        return userRepo.findByUsername(username)
                .flatMap(user -> userRoleRepository.findAllByUserId(user.getUserId())
                        .flatMap(userRoleSignal -> roleRepository.findById(userRoleSignal.getRoleId()))
                        .collectList()
                        .doOnNext(user::setRoles)
                        .thenReturn(user));
    }

    @Transactional(readOnly = true)
    public Mono<UserDetails> getUserDetails(User user) {
        return Mono.just(user)
                .flatMap(_user -> {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
                    return Mono.just(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities));
                });
    }
}
