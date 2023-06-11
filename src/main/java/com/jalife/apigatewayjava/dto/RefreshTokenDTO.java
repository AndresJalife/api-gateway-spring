package com.jalife.apigatewayjava.dto;

import lombok.Data;

/**
 * Clase para transferir datos de un token.
 */

@Data
public class RefreshTokenDTO {
    private String refreshToken;
}
