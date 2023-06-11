package com.jalife.apigatewayjava.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * Clase estática encargada de escribir un objeto en una respuesta HTTP.
 */
public class ResponseWritter {


    private ResponseWritter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Método encargado de escribir en una respuesta ServerHttpResponse un objeto cualquiera.
     */
    public static Mono<Void> write(ServerHttpResponse response, Object body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(body));
            return response.writeWith(Mono.just(dataBuffer));
        } catch (Exception e) {
            return Mono.empty().then();
        }
    }
}
