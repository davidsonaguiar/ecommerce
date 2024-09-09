package davidson.com.ecommerce.exceptions.handlers;

import davidson.com.ecommerce.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.DateTimeException;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> handleException(ResourceNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ContentConflictException.class)
    public ResponseEntity<StandardException> handleException(ContentConflictException exception) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardException> handleException(UnauthorizedException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardException> handleException(ForbiddenException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardException> handleException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getAllErrors().stream()
                .map(errorObject -> errorObject.getDefaultMessage())
                .collect(Collectors.joining(", ")));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardException> handleException(Exception exception) {
        exception.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardException> handleException(NoResourceFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Resource not found");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardException> handleException(NullPointerException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Internal server error");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardException> handleException(BadCredentialsException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Email or password invalid.");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<StandardException> handleException(UnprocessableException exception) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardException> handleException(IllegalArgumentException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<StandardException> handleException(DateTimeException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Invalid date format.");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("JSON Invalid data.");

        return ResponseEntity.status(status).body("Invalid data.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardException> handleException(IllegalStateException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Internal server error.");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<StandardException> handleException(TransactionSystemException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Invalid data.");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<StandardException> handleException(NoContentException exception) {
        HttpStatus status = HttpStatus.NO_CONTENT;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardException> handleException(MethodArgumentTypeMismatchException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Invalid parameter type.");

        return ResponseEntity.status(status).body(error);
    }
}
