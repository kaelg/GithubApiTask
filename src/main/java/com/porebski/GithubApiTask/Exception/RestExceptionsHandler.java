package com.porebski.GithubApiTask.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsHandler extends Exception {
    @ExceptionHandler(GitHubUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponse> handleGitHubUserNotFoundException(
            GitHubUserNotFoundException exception
    ) {
        return new ResponseEntity<>(new ErrorResponse("404", exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAcceptableTypeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ResponseEntity<ErrorResponse> handleNotAcceptableTypeException(
            NotAcceptableTypeException exception
    ) {
        return new ResponseEntity<>(new ErrorResponse("404", exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
