package com.jalife.apigatewayjava.repository;

import com.jalife.apigatewayjava.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

}