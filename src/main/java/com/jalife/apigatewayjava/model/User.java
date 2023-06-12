package com.jalife.apigatewayjava.model;

import com.jalife.apigatewayjava.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    @Transient
    private Role role;

    public User(UserDTO userDTO, Role role) {
        this.userId = userDTO.getId();
        this.name = userDTO.getName();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.email = userDTO.getEmail();
        this.role = role;
    }
}