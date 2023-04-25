package com.jalife.apigatewayjava.repository;

import com.jalife.apigatewayjava.model.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
}