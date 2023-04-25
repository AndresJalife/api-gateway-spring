package com.jalife.apigatewayjava.repository;

import com.jalife.apigatewayjava.model.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long> {

    Flux<UserRole> findAllByUserId(Long userId);
}
