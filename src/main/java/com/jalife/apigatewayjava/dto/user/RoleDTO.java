package com.jalife.apigatewayjava.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object to send role data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    @JsonProperty("role_id")
    private Long roleId;
    private String name;
    private String description;
}
