package bo.gob.dgac.rbs.dna.common.exception;

import bo.gob.dgac.rbs.dna.common.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .exito(false)
                .mensaje(ex.getMessage())
                .datos(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .exito(false)
                .mensaje(ex.getMessage())
                .datos(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleIllegalState(IllegalStateException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .exito(false)
                .mensaje(ex.getMessage())
                .datos(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}