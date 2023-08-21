package study.neo.deal.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(NotFoundException notFoundException) {
        log.warn(notFoundException.getMessage());
        return new ErrorResponse(notFoundException.getMessage());
    }

    @ExceptionHandler(ConflictDataRequest.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictExceptionHandler(ConflictDataRequest conflictDataRequest) {
        log.warn(conflictDataRequest.getMessage());
        return new ErrorResponse(conflictDataRequest.getMessage());
    }

    @Data
    @RequiredArgsConstructor
    private static class ErrorResponse {
        private final String error;
        private String description;
    }
}
