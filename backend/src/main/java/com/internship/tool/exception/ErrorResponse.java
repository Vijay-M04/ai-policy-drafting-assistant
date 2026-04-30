package com.internship.tool.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> 181653b (Day 9 — Implemented File Upload & Download with Validation and UUID Storage)

@Data // Lombok: getters/setters
@AllArgsConstructor // constructor
public class ErrorResponse {

    private LocalDateTime timestamp; // time of error
    private int status;              // HTTP status code
    private String error;            // error type
    private Object message;          // message (String or Map for validation)
}