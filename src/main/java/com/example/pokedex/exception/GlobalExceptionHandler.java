package com.example.pokedex.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.net.ssl.SSLHandshakeException;
import java.util.Date;

/**
 * @apiNote class to capture  all the Pokemon Exceptions globally
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException exception, WebRequest request){

        PokemonException pokemonException = new PokemonException(new Date(),exception.getStatusText(),request.getDescription(false));

        return new ResponseEntity(pokemonException, exception.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        PokemonException pokemonException = new PokemonException(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<Object>(pokemonException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleNullPointerException(IllegalStateException exception, WebRequest request) {

        PokemonException pokemonException = new PokemonException(new Date(), exception.getMessage(), request.getDescription(false));

        return new ResponseEntity(pokemonException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SSLHandshakeException.class)
    public ResponseEntity<Object> handleSSLHandshakeException(SSLHandshakeException exception, WebRequest request) {

        PokemonException pokemonException = new PokemonException(new Date(), exception.getMessage(), request.getDescription(false));

        return new ResponseEntity(pokemonException, HttpStatus.UNAUTHORIZED);
    }

}
