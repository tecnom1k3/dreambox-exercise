package com.miguel.dreambox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "word not found")
public class WordNotFoundException extends RuntimeException {
    /*
    should be thrown when a word was not found in the main dictionary, and there are no suggestions available
     */
}
