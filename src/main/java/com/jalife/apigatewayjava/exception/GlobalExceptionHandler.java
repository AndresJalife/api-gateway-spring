package com.jalife.apigatewayjava.exception;

import com.jalife.apigatewayjava.Util.ResponseWritter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * Clase encargada de manejar las excepciones.
 */

@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * Método encargado de manejar una excepción que surga en una llamada.
     *
     * @param exchange
     * @param err
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable err) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        HashMap<String, String> error = new HashMap<>();
        error.put("error", err.getMessage());

        return ResponseWritter.write(exchange.getResponse(), error);
    }
}
