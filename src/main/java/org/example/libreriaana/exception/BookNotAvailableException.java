package org.example.libreriaana.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotAvailableException extends RuntimeException {
    
    public BookNotAvailableException(Long libroId) {
        super(String.format("El libro con id %d no está disponible para préstamo", libroId));
    }
}