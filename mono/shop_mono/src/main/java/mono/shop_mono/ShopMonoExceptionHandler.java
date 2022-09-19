package mono.shop_mono;

import lombok.extern.slf4j.Slf4j;
import mono.shop_mono.dto.exception.BadRequestException;
import mono.shop_mono.dto.exception.ForbiddenException;
import mono.shop_mono.dto.exception.InternalServerErrorException;
import mono.shop_mono.dto.exception.NotFoundException;
import mono.shop_mono.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ShopMonoExceptionHandler{

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        log.error("BadRequestException occurred", ex);

        return ResponseEntity
                .badRequest()
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(ForbiddenException ex) {
        log.error("ForbiddenException occurred", ex);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(NotFoundException ex) {
        log.error("NotFoundException occurred", ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("ValidationException occurred", ex);

        return ResponseEntity
                .badRequest()
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred", ex);

        return ResponseEntity
                .badRequest()
                .body(createErrorResponse(ex.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining("\n"))));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerErrorException ex) {
        log.error("Unknown exception occurred", ex);

        return ResponseEntity
                .badRequest()
                .body(createErrorResponse("Internal Server Error"));
    }

    private static ErrorResponse createErrorResponse(String message) {
        return new ErrorResponse(Instant.now(), message);
    }
}
