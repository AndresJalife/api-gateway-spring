package com.jalife.apigatewayjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.util.ArrayList;
import java.util.Collection;

@Table("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column("user_id")
    private Long userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private String isActive;

    @Transient
    private Collection<Role> roles = new ArrayList<>();
}