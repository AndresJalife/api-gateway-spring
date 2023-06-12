package com.jalife.apigatewayjava.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for receiving and sending user data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private RoleDTO role;
}
