package br.com.booksaas.book_reader.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ReferencedException.class)
    public ResponseEntity<Object> handleReferencedException(ReferencedException ex) {
        Object[] argsArray = ex.getParams() != null ? ex.getParams().toArray() : null;
        String userMessage = messageSource.getMessage(
                ex.getKey(),
                argsArray,
                ex.getKey(),
                LocaleContextHolder.getLocale()
        );


        assert userMessage != null;
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", userMessage,
                "code", ex.getKey()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}