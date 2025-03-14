package org.sarahwdt.arthub.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sarahwdt.arthub.exception.AuthorizationWrappedException;
import org.sarahwdt.arthub.exception.BusinessLogicException;
import org.sarahwdt.arthub.exception.ValidationException;
import org.sarahwdt.arthub.util.I18nPlaceholders;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        throw ValidationException.create(e);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationWrapperException(HttpServletRequest request,
                                                                   HttpServletResponse response,
                                                                   ValidationException exception) {
        WebRequest webRequest = new ServletWebRequest(request, response);
        ResponseEntity<Object> problemDetailsResponse = handleExceptionInternal(
                exception, null, exception.getHeaders(), exception.getStatusCode(), webRequest);
        if (problemDetailsResponse != null && getMessageSource() != null
                && exception.getBindingResult().hasErrors()
                && problemDetailsResponse.getBody() instanceof ProblemDetail problemDetail) {
            MessageSource messageSource = getMessageSource();

            Map<String, List<String>> errors = new HashMap<>();
            exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                    errors.computeIfAbsent(fieldError.getField(), s -> new ArrayList<>())
                            .add(messageSource.getMessage(fieldError, request.getLocale())));
            problemDetail.setProperty("errors", errors);

            List<String> objectErrors = exception.getBindingResult().getGlobalErrors().stream()
                    .map(objectError -> messageSource.getMessage(objectError, request.getLocale()))
                    .toList();
            problemDetail.setProperty("objectErrors", objectErrors);
            return problemDetailsResponse;
        }
        throw exception;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                AuthenticationException exception) {
        return handleAuthorizationWrappedException(request, response,
                AuthorizationWrappedException.wrap(exception), exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              AccessDeniedException exception) {
        return handleAuthorizationWrappedException(request, response,
                AuthorizationWrappedException.wrap(exception), exception);
    }

    public ResponseEntity<Object> handleAuthorizationWrappedException(HttpServletRequest request,
                                                                      HttpServletResponse response,
                                                                      AuthorizationWrappedException wrappedException,
                                                                      RuntimeException exception) {
        WebRequest webRequest = new ServletWebRequest(request, response);
        ResponseEntity<Object> problemDetailsResponse = handleExceptionInternal(
                wrappedException, null, wrappedException.getHeaders(), wrappedException.getStatusCode(), webRequest);
        if (problemDetailsResponse != null) {
            return problemDetailsResponse;
        }
        throw exception;
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Object> handleBusinessLogicException(BusinessLogicException ex,
                                                               WebRequest request) {
        ResponseEntity<Object> problemDetailsResponse = handleExceptionInternal(ex, null,
                ex.getHeaders(), ex.getStatusCode(), request);
        if (problemDetailsResponse != null) {
            return problemDetailsResponse;
        }
        throw ex;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ProblemDetail> fallbackHandler(Throwable e, HttpServletRequest request) {
        log.error("Unexpected error occurred", e);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        String title = "Something went wrong...";
        String detail = "Please try again later.";
        if (getMessageSource() != null) {
            title = getMessageSource().getMessage(I18nPlaceholders.Error.UNEXPECTED_ERROR_TITLE, null, request.getLocale());
            detail = getMessageSource().getMessage(I18nPlaceholders.Error.UNEXPECTED_ERROR_DETAIL, null, request.getLocale());
        }
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
