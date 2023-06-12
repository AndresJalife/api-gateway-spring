package com.jalife.apigatewayjava.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for password update.
 */
@Data
@AllArgsConstructor
public class PasswordDTO {
    @JsonProperty("user_id")
    private Long id;
    @JsonProperty("old_password")
    private String oldPassword;
    @JsonProperty("new_password")
    private String newPassword;
}