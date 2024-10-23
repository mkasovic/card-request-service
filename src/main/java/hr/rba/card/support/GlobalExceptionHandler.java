package hr.rba.card.support;

import hr.rba.card.service.solicitation.CardRequestAlreadyExistsException;
import hr.rba.card.service.solicitation.CardRequestNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCardRequestNotFoundException(HttpServletRequest request, CardRequestNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(ex), ErrorResponseCodes.ERR404.toString()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardRequestAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCardRequestAlreadyExistsException(HttpServletRequest request, CardRequestAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(ex), ErrorResponseCodes.ERR400.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(ex), ErrorResponseCodes.ERR400.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(e), ErrorResponseCodes.ERR405.toString()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(e), ErrorResponseCodes.ERR400.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(HandlerMethodValidationException ex) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(ex), ErrorResponseCodes.ERR400.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(getErrorDescription(ex), ErrorResponseCodes.ERR500.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected String getErrorDescription(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect( Collectors.joining("; ") );
    }

    protected String getErrorDescription(HandlerMethodValidationException ex) {
        return ex.getReason();
    }

    protected String getErrorDescription(Exception ex) {
        return ex.getMessage();
    }
}
