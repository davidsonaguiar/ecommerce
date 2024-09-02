package davidson.com.ecommerce.exceptions.handlers;

import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ForbiddenException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerError  {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleException(ResourceNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ContentConflictException.class)
    public ResponseEntity<StandardError> handleException(ContentConflictException exception) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleException(UnauthorizedException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardError> handleException(ForbiddenException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getAllErrors().stream()
                .map(errorObject -> errorObject.getDefaultMessage())
                .collect(Collectors.joining(", ")));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception exception) {
        exception.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardError error = new StandardError();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(status).body(error);
    }
}
